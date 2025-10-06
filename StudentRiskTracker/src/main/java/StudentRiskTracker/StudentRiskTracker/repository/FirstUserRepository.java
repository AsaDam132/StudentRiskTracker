package StudentRiskTracker.StudentRiskTracker.repository;

import StudentRiskTracker.StudentRiskTracker.model.FirstUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FirstUserRepository extends JpaRepository<FirstUser, Long> {
    Optional<FirstUser> findByUsername(String username);
}

