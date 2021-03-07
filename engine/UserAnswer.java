package engine;

import java.util.*;

public class UserAnswer {
    private List<String> answer;

    public UserAnswer() {
    }

    public List<String> getAnswer() {
        if (this.answer == null) {
            return new ArrayList<>();
        }
        return this.answer;
    }

    public void setAnswer(List<String> answer) {
        this.answer = new ArrayList<>(answer);
    }
}
