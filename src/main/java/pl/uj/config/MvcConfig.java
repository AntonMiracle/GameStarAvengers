package pl.uj.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pl.uj.model.User;
import pl.uj.repositories.UserRepository;

import javax.annotation.PostConstruct;
import java.util.HashSet;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("login");
        registry.addViewController("/registration").setViewName("registration");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/logout").setViewName("login");
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/score").setViewName("score");
        registry.addViewController("/admin").setViewName("admin");
    }

    @PostConstruct
    public void init() {
        initUsers();
    }

    private void initUsers() {
        if (userRepository.findByUsername("admin") == null) {
            User user = new User("admin", "admin", new HashSet<>());
            user.setRole(User.Role.ADMIN);
            user.setBanned(false);
            userRepository.save(user);
        }
        for (int i = 0; i < 10; ++i) {
            String username = "user" + i;
            if (userRepository.findByUsername(username) == null) {
                User user = new User(username, username, new HashSet<>());
                user.setRole(User.Role.USER);
                userRepository.save(user);
            }
        }
    }
}

