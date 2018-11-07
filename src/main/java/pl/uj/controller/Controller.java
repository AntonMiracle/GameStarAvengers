package pl.uj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import pl.uj.converter.UserToUserDetails;
import pl.uj.model.Score;
import pl.uj.model.User;
import pl.uj.repositories.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
public class Controller {
    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/user/new")
    public ModelAndView createUser(@RequestParam("username") String username
            , @RequestParam("password") String password) {
        if (username.length() == 0 || password.length() == 0 || userRepository.findByUsername(username) != null) {
            return new ModelAndView("/registration").addObject("error", "1");
        }
        User user = new User(username, password, new HashSet<>());
        user.setRole(User.Role.USER);
        userRepository.save(user);
        return new ModelAndView("/login").addObject("registration", "1");
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

    @RequestMapping("/index")
    public ModelAndView goToAdminPage() {
        Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (object != null && !(object instanceof String) && ((UserToUserDetails) object).getUser().getRole() == User.Role.ADMIN) {
            Set<User> users = userRepository.findAllWithRole(User.Role.USER);
            return new ModelAndView("/admin").addObject("users", users.toArray());
        }
        if (object != null && !(object instanceof String) && ((UserToUserDetails) object).getUser().isBanned()) {
            return new ModelAndView("/login").addObject("banned", "1");
        }
        return new ModelAndView("/home");
    }

    @RequestMapping("/ban/{id}")
    public ModelAndView banUser(@PathVariable(value = "id") long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setBanned(true);
            userRepository.save(user);
        }
        return goToAdminPage();
    }

    @RequestMapping("/unban/{id}")
    public ModelAndView unbanUser(@PathVariable(value = "id") long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setBanned(false);
            userRepository.save(user);
        }
        return goToAdminPage();
    }

    @RequestMapping("/delete/{id}")
    public ModelAndView deleteUser(@PathVariable(value = "id") long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            userRepository.delete(user);
        }
        return goToAdminPage();
    }
}
