package StudentRiskTracker.StudentRiskTracker.repository;

import StudentRiskTracker.StudentRiskTracker.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {
    List<Attendance> findByStudentIdAndSemester(UUID studentId, String semester);
}