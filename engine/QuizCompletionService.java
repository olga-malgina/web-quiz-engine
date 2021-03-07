package engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class QuizCompletionService {

    @Autowired
    QuizCompletionRepository quizCompletionRepository;

    public Page<QuizCompletion> getQuizCompletion(int pageNo, int pageSize, String sortBy, Users user) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        return quizCompletionRepository.getQuizCompletionByUser(user, paging);
    }

    public QuizCompletion saveQuizCompletion(QuizCompletion completion) {
        return quizCompletionRepository.save(completion);
    }
}
