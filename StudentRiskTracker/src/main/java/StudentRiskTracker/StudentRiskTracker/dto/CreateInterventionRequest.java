package StudentRiskTracker.StudentRiskTracker.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class CreateInterventionRequest {
    private String studentId;
    private String interventionType;
    private LocalDate startDate;
    private LocalDate targetCompletionDate;
    private double startScore;
    private double goalScore;
}
