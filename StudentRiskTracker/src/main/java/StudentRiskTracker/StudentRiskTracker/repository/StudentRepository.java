package StudentRiskTracker.StudentRiskTracker.repository;

import StudentRiskTracker.StudentRiskTracker.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StudentRepository  extends JpaRepository<Student, UUID> {


}
