package StudentRiskTracker.StudentRiskTracker.service;

import StudentRiskTracker.StudentRiskTracker.model.FirstUser;
import StudentRiskTracker.StudentRiskTracker.repository.FirstUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final FirstUserRepository firstUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserDetailsServiceImpl(FirstUserRepository firstUserRepository, PasswordEncoder passwordEncoder) {
        this.firstUserRepository = firstUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        FirstUser user = firstUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );
    }

    public FirstUser registerUser(FirstUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return firstUserRepository.save(user);
    }
}
