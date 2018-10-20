package pl.uj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import pl.uj.model.Score;
import pl.uj.model.User;
import pl.uj.repositories.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashSet;

@RestController
public class Controller {
    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/user/new")
    public RedirectView createUser(@RequestParam("username") String username
            , @RequestParam("password") String password
            , HttpServletRequest request) {

        if (username.length() == 0 || password.length() == 0 || userRepository.findByUsername(username) != null)
            return new RedirectView("/registration?error");

        User user = new User(username, password, new HashSet<>());
        userRepository.save(user);
        return new RedirectView("/login?registration");
    }

    @RequestMapping("/score/save")
    public RedirectView saveScores(@RequestParam("score") String points) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User user = userRepository.findByUsername(username);
        Score score = new Score(Long.valueOf(points), LocalDateTime.now());
        user.getScores().add(score);

        userRepository.save(user);
        return new RedirectView("/index");
    }

    @RequestMapping("/score/all")
    public RedirectView getScores(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User user = userRepository.findByUsername(username);

        request.getSession().setAttribute("scores", user.getScores());
        return new RedirectView("/score");
    }

    @RequestMapping("/exit")
    public RedirectView exit() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        return new RedirectView("/logout?logout");
    }
}
