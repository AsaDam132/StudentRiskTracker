package StudentRiskTracker.StudentRiskTracker.controller;

import StudentRiskTracker.StudentRiskTracker.dto.CreateInterventionRequest;
import StudentRiskTracker.StudentRiskTracker.dto.InterventionProgressUpdate;

import StudentRiskTracker.StudentRiskTracker.model.Intervention;
import StudentRiskTracker.StudentRiskTracker.service.InterventionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/interventions")
@SecurityRequirement(name = "authorization")
@RequiredArgsConstructor
public class InterventionController {

    private final InterventionService interventionService;

    @PostMapping
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public ResponseEntity<Intervention> createIntervention(
            @Valid @RequestBody CreateInterventionRequest request) {
        return ResponseEntity.ok(interventionService.createIntervention(request));
    }

    @PutMapping("/{id}/progress")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public ResponseEntity<Intervention> updateProgress(
            @PathVariable String id,
            @Valid @RequestBody InterventionProgressUpdate update) {
        return ResponseEntity.ok(interventionService.updateInterventionProgress(id, update));
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN','PARENT','STUDENT')")
    public ResponseEntity<List<Intervention>> getStudentInterventions(@PathVariable String studentId) {
        return ResponseEntity.ok(interventionService.getStudentInterventions(studentId));
    }
}
