package StudentRiskTracker.StudentRiskTracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "interventions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Intervention {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    private String interventionType;
    private LocalDate startDate;
    private LocalDate targetCompletionDate;

    private Double startScore;
    private Double currentScore;
    private Double goalScore;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum Status {
        ON_TRACK, NOT_ON_TRACK, COMPLETED
    }

    // getters and setters
}
