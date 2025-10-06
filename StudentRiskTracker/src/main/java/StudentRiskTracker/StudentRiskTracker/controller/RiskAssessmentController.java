package StudentRiskTracker.StudentRiskTracker.controller;


import StudentRiskTracker.StudentRiskTracker.dto.StudentRiskAssessment;
import StudentRiskTracker.StudentRiskTracker.service.RiskAssessmentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/risk-assessment")
@SecurityRequirement(name = "authorization")
@RequiredArgsConstructor
public class RiskAssessmentController {

    private final RiskAssessmentService riskAssessmentService;

    @GetMapping("/students/{studentId}")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public ResponseEntity<StudentRiskAssessment> getStudentRiskAssessment(
            @PathVariable String studentId,
            @RequestParam String semester) {
        return ResponseEntity.ok(riskAssessmentService.calculateRiskScore(studentId, semester));
    }
}
