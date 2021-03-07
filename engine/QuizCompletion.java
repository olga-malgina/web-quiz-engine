package engine;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table
public class QuizCompletion {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private long completionId;

    // to store the id of the completed quiz
    @Column
    private long id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private Users user;

    @Column
    private Instant completedAt;

    public QuizCompletion() {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        this.completedAt = time.toInstant();
    }

    public long getCompletionId() {
        return this.completionId;
    }

    public void setCompletionId(long id) {
        this.completionId = id;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Users getUser() {
        return this.user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Instant getCompletedAt() {
        return this.completedAt;
    }
}
