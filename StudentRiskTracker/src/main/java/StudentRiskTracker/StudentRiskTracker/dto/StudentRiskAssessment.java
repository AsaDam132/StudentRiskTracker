package StudentRiskTracker.StudentRiskTracker.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentRiskAssessment {
    private String studentId;
    private String semester;
    private int riskScore;
    private RiskLevel riskLevel;

    public enum RiskLevel { LOW, MEDIUM, HIGH }
}
