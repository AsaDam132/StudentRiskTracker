package StudentRiskTracker.StudentRiskTracker.repository;

import StudentRiskTracker.StudentRiskTracker.model.AcademicPerformance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AcademicPerformanceRepository extends JpaRepository<AcademicPerformance, UUID> {
    List<AcademicPerformance> findByStudentIdAndSemester(UUID studentId, String semester);
}