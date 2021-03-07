package engine;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.bind.annotation.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;

@Entity(name = "Quiz")
@Table(name = "Quizzes")
public class Quiz {

    @NotEmpty
    @Column(name = "QuizTitle")
    private String title;

    @NotEmpty
    @Column(name = "QuizText")
    private String text;

    @NotNull
    @Size(min = 2)
    @ElementCollection
    @CollectionTable(name = "Options", joinColumns = @JoinColumn(name = "QuizID"))
    private List<String> options;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ElementCollection
    @CollectionTable(name = "Answers", joinColumns = @JoinColumn(name = "QuizID"))
    private List<Integer> answers;

    @Id
    @Column(name = "QuizID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private Users user;

    public Quiz() {
    }

    public String getTitle() {
        return this.title;
    }

    public String getText() {
        return this.text;
    }

    public String[] getOptions() {
        String[] options = new String[this.options.size()];
        int i = 0;
        for (String o : this.options) {
            options[i] = o;
            ++i;
        }
        return options;
    }


    public List<Integer> getAnswers() {
        if (this.answers == null) {
            return new ArrayList<>();
        } else {
            return this.answers;
        }
    }

    public long getId() {
        return this.id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setOptions(String[] options) {
        this.options = new ArrayList<String>();
        this.options.addAll(Arrays.asList(options));
    }


    public void setAnswer(List<Integer> answers) {
        this.answers = new ArrayList<Integer>();
        this.answers.addAll(answers);
    }

    public void setId(int id) {
        this.id = id;
    }

    public Users getUser() {
        return this.user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

}
