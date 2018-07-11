package codesquad.domain;

import codesquad.Exception.RedirectException;
import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Lob
    private String contents;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_answer_writer"))
    private User writer;

    @ManyToOne
    private Question question;
    private boolean deleted;

    public Answer() {
        this.deleted = false;
    }

    public void validateWriter(User user) {
        if (!writer.equals(user)) {
            throw new RedirectException("권한이 없습니다.");
        }
    }

    public boolean matchId(long answerId) {
        return this.id == answerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return id == answer.id &&
                Objects.equals(contents, answer.contents) &&
                Objects.equals(writer, answer.writer) &&
                Objects.equals(question, answer.question);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, contents, writer, question);
    }
}
