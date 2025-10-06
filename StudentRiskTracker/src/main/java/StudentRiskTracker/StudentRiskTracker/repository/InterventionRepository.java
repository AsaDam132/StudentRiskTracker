package StudentRiskTracker.StudentRiskTracker.repository;

import StudentRiskTracker.StudentRiskTracker.model.Intervention;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InterventionRepository extends JpaRepository<Intervention, UUID> {
    List<Intervention> findByStudentId(UUID studentId);
}