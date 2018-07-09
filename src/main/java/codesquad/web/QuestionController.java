package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class QuestionController {
    private List<Question> questions = new ArrayList<>();

    @Autowired
    QuestionRepository questionRepository;

    @PostMapping("/questions")
    public String create(Question question) {
//        question.setIndex(questions.size()+1);
//        questions.add(question);
        questionRepository.save(question);
        return "redirect:/";
    }


    @GetMapping("/")
    public String list(Model model) {
        model.addAttribute("questions", questionRepository.findAll());
        return "/index";
    }

    @GetMapping("/questions/{id}")
    public String show(@PathVariable Long id, Model model) {
        model.addAttribute("questions", questionRepository.findById(id).get());
        return "/qna/show";
    }

    @GetMapping("/questions/{id}/update")
    public String findQuestion(@PathVariable Long id, Model model) {
        model.addAttribute("question", questionRepository.findById(id).get());
        return "/qna/updateForm";
    }

    @PutMapping("questions/update")
    public String update(Question question) {
        questionRepository.save(question);
        return "redirect:/questions/" + question.getId();
    }

    @DeleteMapping("questions/{id}/delete")
    public String delete(@PathVariable Long id) {
        questionRepository.deleteById(id);
        return "redirect:/";
    }

}
