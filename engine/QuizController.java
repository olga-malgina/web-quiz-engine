package engine;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import javax.validation.*;

@RestController
public class QuizController {

    @Autowired
    private QuizService quizService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuizCompletionService quizCompletionService;


    public QuizController() {
    }

    // register a user
    @PostMapping(path = "/api/register", consumes = "application/json")
    public ResponseEntity<Users> registerUser(@Valid @RequestBody Users user) {
        if (userRepository.existsUserByEmail(user.getEmail())) {
            throw new UniqueViolationException("User with this email already exists");
        } else {
            String encodedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
            user.setPassword(encodedPassword);
            userRepository.save(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }

    public Users getUserFromContext() {
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(currentUserName);
    }

    // get all the quizzes
    @GetMapping(path = "/api/quizzes")
    public ResponseEntity<Page<Quiz>> getAllQuizzes(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        Page<Quiz> pageResult = quizService.getAllQuizzes(page, pageSize);
        return new ResponseEntity<>(pageResult, new HttpHeaders(), HttpStatus.OK);
    }

    // delete a particular quiz, authorized only for quizzes created by the user
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/api/quizzes/{id}")
    public void deleteById(@PathVariable(value = "id") Long quizId) {
        Quiz quiz = quizService.findQuizById(quizId).orElseThrow(() -> new UserNotFoundException("id: " + quizId));
        if (Objects.equals(getUserFromContext(), quiz.getUser())) {
            quizService.deleteQuizById(quizId);
        } else {
            throw new ForbiddenOperationException("Forbidden operation");
        }
    }

    public List<Integer> toInt(List<String> answer) {
        List<Integer> intAnswer = new ArrayList<>();
        for (String a : answer) {
            intAnswer.add(Integer.parseInt(a));
        }
        return intAnswer;
    }


    // create a new quiz
    @PostMapping(path = "/api/quizzes", consumes = "application/json")
    public Quiz createQuiz(@Valid @RequestBody Quiz quiz) {
        quiz.setUser(getUserFromContext());
        return quizService.saveQuiz(quiz);
    }

    // get a quiz by its id
    @GetMapping(path = "/api/quizzes/{id}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable(value = "id") Long quizId) throws UserNotFoundException {
         Quiz quiz = quizService.findQuizById(quizId).orElseThrow(() -> new UserNotFoundException("id: " + quizId));
         return ResponseEntity.ok().body(quiz);
    }

    // solve a quiz
    @PostMapping(path = "/api/quizzes/{id}/solve")
    public Solution solveQuiz(@PathVariable long id, @RequestBody UserAnswer answer) {
        Quiz quiz = quizService.findQuizById(id).orElseThrow(() -> new UserNotFoundException("id: " + id));

        List<Integer> userAnswer = toInt(answer.getAnswer());
        List<Integer> correctAnswer = quiz.getAnswers();

        if (userAnswer.size() != 0) {
            Collections.sort(userAnswer);
        }
        if (correctAnswer.size() != 0) {
            Collections.sort(correctAnswer);
        }

        Solution solution = new Solution();

        if (userAnswer.equals(correctAnswer)) {

            // create completion of the quiz and save it
            QuizCompletion completion = new QuizCompletion();
            completion.setId(quiz.getId());
            completion.setUser(getUserFromContext());
            quizCompletionService.saveQuizCompletion(completion);

            solution.setSuccess(true);
            solution.setFeedback("Congratulations, you're right!");
        } else {
            solution.setSuccess(false);
            solution.setFeedback("Wrong answer! Please, try again.");
        }

        return solution;

    }

    // get completed quizzes
    @GetMapping(path = "/api/quizzes/completed")
    public ResponseEntity<Page<QuizCompletion>> getCompletedQuizzes(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "completedAt") String sortBy) {

        Page<QuizCompletion> pageResult = quizCompletionService.getQuizCompletion(page, pageSize, sortBy, getUserFromContext());
        return new ResponseEntity<>(pageResult, new HttpHeaders(), HttpStatus.OK);
    }

}
