package StudentRiskTracker.StudentRiskTracker.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "students")
@Data
public class Student {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private String grade;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // getters and setters
}
