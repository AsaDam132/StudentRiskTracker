package StudentRiskTracker.StudentRiskTracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "academic_performance")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcademicPerformance {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    private String semester;
    private String course;
    private Double grade;
    private Integer stateAssessmentEla;
    private Integer stateAssessmentMath;

    // getters and setters
}
