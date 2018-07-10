package codesquad.domain;

import codesquad.web.WebUtil;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.*;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String title;
    private String contents;


    public Question() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getWriter() {
        return writer;
    }

    public void setWriter(User writer) {
        this.writer = writer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public boolean isCorrectPassword(Question question) {
        return this.password.equals(question.getPassword());
    }

    public boolean matchWriter(User user) {
        return writer.equals(user);
    }

    public void update(Question question) {
        this.title = question.title;
        this.contents = question.contents;
    }
}

