package StudentRiskTracker.StudentRiskTracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "behavior")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Behavior {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    private String semester;
    private Integer disciplinaryActions = 0;
    private Integer suspensions = 0;

    // getters and setters
}
