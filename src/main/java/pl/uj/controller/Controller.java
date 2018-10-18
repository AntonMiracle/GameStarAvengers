package pl.uj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.uj.model.Score;
import pl.uj.model.User;
import pl.uj.repositories.ScoreRepository;
import pl.uj.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@RestController
public class Controller {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ScoreRepository scoreRepository;

    @RequestMapping("/user/add")
    public String addUser(@RequestParam(name = "username") String username
            , @RequestParam(name = "password") String password) {
        User user = new User(username, password, new HashSet<>());
        userRepository.save(user);
        return user.toString();
    }

    @RequestMapping("/user/new")
    public String createUser(@RequestBody User user) {
        System.out.println("Before new User :" + user.toString());
        user = new User(user.getUsername(), user.getPassword(), new HashSet<>());
        System.out.println("After new User :" + user.toString());
        userRepository.save(user);
        return "/login";
    }

    @RequestMapping("/user/list")
    public String userList() {
        StringBuilder result = new StringBuilder();
        for (User r : userRepository.findAll()) {
            result.append(r.toString()).append(System.lineSeparator());
        }
        return result.toString();
    }

    @RequestMapping("/score/add")
    public String addScore(@RequestParam(name = "username") String username
            , @RequestParam(name = "score") long score) {
        User user = userRepository.findByUsername(username);
        Score scoreData = new Score(score, LocalDateTime.now());
        user.getScores().add(scoreData);
        userRepository.save(user);
        return user.toString();
    }

    @RequestMapping("/score/list")
    @ResponseBody
    public List<Score> scores() {
        List<Score> scores = new ArrayList<>();
        for (Score score : scoreRepository.findAll()) {
            scores.add(score);
        }
        return scores;
    }
}
