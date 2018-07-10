package codesquad.web;

import codesquad.domain.User;
import org.springframework.data.repository.CrudRepository;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

public class WebUtil {

    public static boolean isAlive(HttpSession session) {
        Object maybeUser = session.getAttribute("sessionedUser");
        if (maybeUser == null) {
            return false;
        }
        return true;
    }
    public static User fromSession(HttpSession session) {
        return (User) session.getAttribute("sessionedUser");
    }

}
