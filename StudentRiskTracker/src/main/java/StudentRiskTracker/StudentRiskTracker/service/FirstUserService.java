package StudentRiskTracker.StudentRiskTracker.service;
import StudentRiskTracker.StudentRiskTracker.model.FirstUser;
import StudentRiskTracker.StudentRiskTracker.repository.FirstUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FirstUserService {

    @Autowired
    private FirstUserRepository firstUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public FirstUser registerUser(FirstUser firstUser) {
        // Encode the password before saving
        firstUser.setPassword(passwordEncoder.encode(firstUser.getPassword()));
        return firstUserRepository.save(firstUser);
    }

    public Optional<FirstUser> findByUsername(String username) {
        return firstUserRepository.findByUsername(username);
    }
    
}
