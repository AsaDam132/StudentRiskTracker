package StudentRiskTracker.StudentRiskTracker.service;

import StudentRiskTracker.StudentRiskTracker.dto.CreateInterventionRequest;
import StudentRiskTracker.StudentRiskTracker.dto.InterventionProgressUpdate;
import StudentRiskTracker.StudentRiskTracker.model.Intervention;

import StudentRiskTracker.StudentRiskTracker.repository.InterventionRepository;
import StudentRiskTracker.StudentRiskTracker.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InterventionService {

    private final InterventionRepository interventionRepo;
    private final StudentRepository studentRepo;

    public Intervention createIntervention(CreateInterventionRequest req) {
        var student = studentRepo.findById(UUID.fromString(req.getStudentId()))
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Intervention intervention = new Intervention();
        intervention.setStudent(student);
        intervention.setInterventionType(req.getInterventionType());
        intervention.setStartDate(req.getStartDate());
        intervention.setTargetCompletionDate(req.getTargetCompletionDate());
        intervention.setStartScore(req.getStartScore());
        intervention.setCurrentScore(req.getStartScore());
        intervention.setGoalScore(req.getGoalScore());
        intervention.setStatus(Intervention.Status.ON_TRACK);

        return interventionRepo.save(intervention);
    }

    public Intervention updateInterventionProgress(String interventionId, InterventionProgressUpdate update) {
        var intervention = interventionRepo.findById(UUID.fromString(interventionId))
                .orElseThrow(() -> new RuntimeException("Intervention not found"));

        intervention.setCurrentScore(update.getCurrentScore());

        if (update.getCurrentScore() >= intervention.getGoalScore()) {
            intervention.setStatus(Intervention.Status.COMPLETED);
        } else if (update.getCurrentScore() < intervention.getStartScore()) {
            intervention.setStatus(Intervention.Status.NOT_ON_TRACK);
        }

        return interventionRepo.save(intervention);
    }

    public List<Intervention> getStudentInterventions(String studentId) {
        return interventionRepo.findByStudentId(UUID.fromString(studentId));
    }
}
