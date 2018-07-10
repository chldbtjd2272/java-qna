package codesquad.web;

import codesquad.domain.ErrorMessage;
import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import codesquad.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Optional;


@Controller
@RequestMapping("/questions")
public class QuestionController {
    @Autowired
    QuestionRepository questionRepository;

    @GetMapping("")
    public String list(Model model) {
        model.addAttribute("questions", questionRepository.findAll());
        return "/index";
    }

    @GetMapping("/new")
    public String createForm(HttpSession session) {
        if (!WebUtil.isAlive(session)) {
            return "redirect:/user/login";
        }
        return "redirect:/qna/form";
    }

    @PostMapping("")
    public String create(Question question, HttpSession session) {
        if (!WebUtil.isAlive(session)) {
            return "/user/login";
        }
        question.setWriter(WebUtil.fromSession(session));
        questionRepository.save(question);
        return "redirect:/questions";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model) {
        model.addAttribute("questions", findQuestionWithId(id, this.questionRepository));
        return "/qna/show";
    }

    @GetMapping("/{id}/update")
    public String findQuestion(@PathVariable Long id, HttpSession session, Model model) {
        if (!WebUtil.isAlive(session)) {
            return "/user/login";
        }

        Question question = findQuestionWithId(id, this.questionRepository);
        if (question.matchWriter(WebUtil.fromSession(session))) {
            model.addAttribute("question", question);
            return "/qna/updateForm";
        }
        model.addAttribute("error", new ErrorMessage("권한이 없습니다."));
        return "/error";
    }

    @PutMapping("")
    public String update(Question question, Model model, HttpSession session) {
        Question original = findQuestionWithId(question.getId(), questionRepository);
        if (original.isCorrectPassword(question)) {
            original.update(question);
            questionRepository.save(original);
            return "redirect:/questions/" + question.getId();
        }
        model.addAttribute("error", new ErrorMessage("글 비밀번호가 다릅니다."));
        return "/error";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, HttpSession session, Model model) {
        if (!WebUtil.isAlive(session)) {
            return "/user/login";
        }

        Question question = findQuestionWithId(id, this.questionRepository);
        if (question.matchWriter(WebUtil.fromSession(session))) {
            questionRepository.deleteById(id);
            return "redirect:/";
        }
        model.addAttribute("error", new ErrorMessage("권한이 없습니다."));
        return "/error";
    }


    static Question findQuestionWithId(Long id, QuestionRepository questionRepository) {

        Optional<Question> questionOptional = questionRepository.findById(id);
        questionOptional.orElseThrow(() -> new IllegalArgumentException("No question found with id " + id));
        return questionOptional.get();
    }
}
