package engine;

import javax.persistence.*;

@Entity(name = "Option")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OptionID")
    private long id;

    @Column(name = "OptionText")
    private String text;

    @ManyToOne
    @JoinColumn(name = "QuizID")
    private Quiz quiz;

    public Option(String text, Quiz quiz) {
        this.text = text;
        this.quiz = quiz;
    }

    public String getText() {
        return this.text;
    }

    public long getId() {
        return this.id;
    }
}
