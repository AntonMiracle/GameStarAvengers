package pl.uj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.uj.model.User;
import pl.uj.repositories.UserRepository;

@RestController
public class Controller {
    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/user/add")
    public String addUser(@RequestParam(name = "username") String username
            , @RequestParam(name = "password") String password) {
        User user = new User(username, password, 0);
        userRepository.save(user);
        return user.toString();
    }

    @RequestMapping("/user/list")
    public String userList() {
        StringBuilder result = new StringBuilder();
        for (User r : userRepository.findAll()) {
            result.append(r.toString()).append(System.lineSeparator());
        }
        return result.toString();
    }

    @RequestMapping("/scores")
    public String scores() {

        return "";
    }
}
