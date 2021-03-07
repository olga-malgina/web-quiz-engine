package engine;

import javax.persistence.*;

@Entity(name = "Answers")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AnswerID")
    private long id;

    @Column(name = "AnswerNumber")
    private int number;

    @ManyToOne
    @JoinColumn(name = "QuizID")
    private Quiz quiz;

    public Answer(int num, Quiz quiz) {
        this.number = num;
        this.quiz = quiz;
    }

    public int getNumber() {
        return this.number;
    }

    public long getId() {
        return this.id;
    }
}
