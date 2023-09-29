package four.group.jahadi.Security;

import four.group.jahadi.Models.User;
import four.group.jahadi.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;


@Service
public class MyUserDetails implements UserDetailsService {

    private static HashMap<String, User> cached = new HashMap<>();

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = cached.get(username);

        if(user == null) {

            Optional<User> u = userRepository.findByNID(username);

            if (u.isEmpty())
                throw new UsernameNotFoundException("User '" + username + "' not found");
            else
                cached.put(username, u.get());

            user = u.get();
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .authorities(user.getAccesses())
                .password(user.getPassword())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();

    }

}