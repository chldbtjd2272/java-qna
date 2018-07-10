package codesquad.web;

import codesquad.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.text.html.Option;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;


    @PostMapping("/login")
    public String login(User user, HttpSession session) {
        Optional<User> maybeUser = userRepository.findByUserId(user.getUserId());
        if (!maybeUser.isPresent()) {
            throw new IllegalArgumentException();
        }
        maybeUser.filter(u -> u.isCorrectPassword(user))
                .orElseThrow(() -> new IllegalArgumentException("아이디 혹은 비밀번호가 틀렸어요"));
        session.setAttribute("sessionedUser", maybeUser.get());
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("sessionedUser");
        return "redirect:/";
    }

    @PostMapping("")
    public String create(User user) {
        userRepository.save(user);
        return "redirect:/users";
    }

    @GetMapping("")
    public String list(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "/user/list";
    }

    @GetMapping("/{index}")
    public String show(@PathVariable Long index, Model model) {
        User user = userRepository.findById(index).get();
        model.addAttribute("user", user);
        return "/user/profile";
    }

    @GetMapping("/{id}/form")
    public String findUser(@PathVariable long id, HttpSession session, Model model) {
        if (!WebUtil.isAlive(session)) {
            return "/user/login";
        }

        User user = WebUtil.fromSession(session);
        if (user.matchId(id)) {
            model.addAttribute("user", user);
            return "/user/updateForm";
        }
        model.addAttribute("error", new ErrorMessage("접근 권한이 없습니다."));
        return "/error";
    }

    @PutMapping("")
    public String updateUser(User user, Model model, HttpSession session) {
        User original = findUserWithId(WebUtil.fromSession(session).getId(), userRepository);
        if (original.isCorrectPassword(user)) {
            original.update(user);
            userRepository.save(original);
            return "redirect:/users";
        }
        model.addAttribute("error", new ErrorMessage("비밀번호가 틀렸습니다"));
        return "/error";
    }


    static User findUserWithId(Long id, UserRepository userRepository) {

        Optional<User> userOptional = userRepository.findById(id);
        userOptional.orElseThrow(() -> new IllegalArgumentException("No user found with id " + id));
        return userOptional.get();
    }


    //
//    @PostMapping("/users")
//    public ModelAndView create2(String userId,
//                                String password,
//                                String name,
//                                String email, Model model){
//        User user = new User(userId,password,name,email);
//        users.add(user);
//        ModelAndView mav = new ModelAndView("/user/list");
//        mav.addObject("users",users);
//        return mav;
//    }
//


    //    @PostMapping("/users")
//    public String create(String userId,
//                                String password,
//                                String name,
//                                String email, Model model){
//        User user = new User(userId,password,name,email);
//        users.add(user);
//
//        model.addAttribute("users",users);
//        return "/user/list";
//    }
}
