package StudentRiskTracker.StudentRiskTracker.repository;

import StudentRiskTracker.StudentRiskTracker.model.Behavior;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BehaviorRepository extends JpaRepository<Behavior, UUID> {
    List<Behavior> findByStudentIdAndSemester(UUID studentId, String semester);
}