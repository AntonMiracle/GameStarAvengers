package pl.uj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.uj.converter.UserToUserDetails;
import pl.uj.model.User;
import pl.uj.repositories.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null) throw new NullPointerException("username null");

        User user = userRepository.findByUsername(username);

        if (user == null) throw new UsernameNotFoundException("username not exist");
        return new UserToUserDetails(user);
    }
}
