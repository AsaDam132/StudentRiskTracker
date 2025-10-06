
package StudentRiskTracker.StudentRiskTracker.service;

import StudentRiskTracker.StudentRiskTracker.dto.StudentRiskAssessment;
import StudentRiskTracker.StudentRiskTracker.repository.AcademicPerformanceRepository;
import StudentRiskTracker.StudentRiskTracker.repository.AttendanceRepository;
import StudentRiskTracker.StudentRiskTracker.repository.BehaviorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RiskAssessmentService {

    private final AcademicPerformanceRepository academicRepo;
    private final AttendanceRepository attendanceRepo;
    private final BehaviorRepository behaviorRepo;

    public StudentRiskAssessment calculateRiskScore(String studentId, String semester) {
        UUID id = UUID.fromString(studentId);
        int score = 0;

        // Academic
        var academics = academicRepo.findByStudentIdAndSemester(id, semester);
        for (var ap : academics) {
            if (ap.getGrade() != null && ap.getGrade() < 70) score += 25;
            if (ap.getStateAssessmentEla() != null && ap.getStateAssessmentEla() < 500) score += 15;
            if (ap.getStateAssessmentMath() != null && ap.getStateAssessmentMath() < 500) score += 15;
        }

        // Attendance
        var attendance = attendanceRepo.findByStudentIdAndSemester(id, semester);
        for (var att : attendance) {
            if (att.getAttendanceRate() != null && att.getAttendanceRate() < 90) score += 20;
            if (att.getAbsentDays() != null && att.getAbsentDays() > 10) score += 10;
            if (att.getTardyDays() != null && att.getTardyDays() > 5) score += 10;
        }

        // Behavior
        var behaviors = behaviorRepo.findByStudentIdAndSemester(id, semester);
        for (var b : behaviors) {
            if (b.getDisciplinaryActions() != null && b.getDisciplinaryActions() > 2) score += 15;
            if (b.getSuspensions() != null && b.getSuspensions() > 0) score += 5;
        }

        // Determine risk level
        StudentRiskAssessment.RiskLevel level = StudentRiskAssessment.RiskLevel.LOW;
        if (score >= 70) {
            level = StudentRiskAssessment.RiskLevel.HIGH;
        } else if (score >= 40) {
            level = StudentRiskAssessment.RiskLevel.MEDIUM;
        }

        // Convert score to percentage (0–1 scale if needed)
        double normalizedScore = Math.min(1.0, score / 100.0);

        return new StudentRiskAssessment(studentId, semester, (int) normalizedScore, level);
    }
}
