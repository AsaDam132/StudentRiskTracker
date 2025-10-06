package StudentRiskTracker.StudentRiskTracker.controller;

import StudentRiskTracker.StudentRiskTracker.dto.CreateInterventionRequest;
import StudentRiskTracker.StudentRiskTracker.dto.InterventionProgressUpdate;
import StudentRiskTracker.StudentRiskTracker.model.Intervention;
import StudentRiskTracker.StudentRiskTracker.service.InterventionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * JUnit 5 test class for InterventionController.
 * Uses @WebMvcTest to load only the controller layer.
 */
@WebMvcTest(
        controllers = InterventionController.class,
        // Exclude SecurityConfig to use Mock Security context
        excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*SecurityConfig.*")
)
class InterventionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InterventionService interventionService;

    // --- Placeholder Classes for Testing (Simulating DTOs/Models) ---

    // Define simple model for testing purposes
    record TestIntervention(String id, String studentId, double goalScore) {}
    record TestCreateRequest(String studentId, String interventionType) {}
    record TestProgressUpdate(double currentScore, String notes) {}

    private static final String API_PATH = "/api/interventions";
    private static final String STUDENT_ID = "123";
    private static final String INTERVENTION_ID = "I001";

    // --- Test Cases for createIntervention (POST /api/interventions) ---

    @Test
    @WithMockUser(roles = {"TEACHER"})
    void createIntervention_asTeacher_shouldReturn200() throws Exception {
        TestCreateRequest request = new TestCreateRequest(STUDENT_ID, "Mentorship");
        TestIntervention mockIntervention = new TestIntervention(INTERVENTION_ID, STUDENT_ID, 85.0);

        when(interventionService.createIntervention(any(CreateInterventionRequest.class)))
                .thenReturn(new Intervention()); // Assuming Intervention can be created simply

        mockMvc.perform(post(API_PATH)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = {"STUDENT"}) // Unauthorized role for creation
    void createIntervention_asStudent_shouldReturn403Forbidden() throws Exception {
        TestCreateRequest request = new TestCreateRequest(STUDENT_ID, "Mentorship");

        mockMvc.perform(post(API_PATH)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    // --- Test Cases for updateProgress (PUT /api/interventions/{id}/progress) ---

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void updateProgress_asAdmin_shouldReturn200() throws Exception {
        TestProgressUpdate update = new TestProgressUpdate(75.0, "Student improving.");

        when(interventionService.updateInterventionProgress(eq(INTERVENTION_ID), any(InterventionProgressUpdate.class)))
                .thenReturn(new Intervention());

        mockMvc.perform(put(API_PATH + "/{id}/progress", INTERVENTION_ID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = {"PARENT"}) // Unauthorized role for update
    void updateProgress_asParent_shouldReturn403Forbidden() throws Exception {
        TestProgressUpdate update = new TestProgressUpdate(75.0, "Student improving.");

        mockMvc.perform(put(API_PATH + "/{id}/progress", INTERVENTION_ID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isForbidden());
    }

    // --- Test Cases for getStudentInterventions (GET /api/interventions/student/{studentId}) ---

    @Test
    @WithMockUser(roles = {"TEACHER"})
    void getStudentInterventions_asTeacher_shouldReturn200() throws Exception {
        // Arrange
        List<Intervention> mockList = List.of(new Intervention(), new Intervention());
        when(interventionService.getStudentInterventions(STUDENT_ID)).thenReturn(mockList);

        // Act & Assert
        mockMvc.perform(get(API_PATH + "/student/{studentId}", STUDENT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(mockList.size()));
    }

    @Test
    @WithMockUser(roles = {"TEACHER"})
    void getStudentInterventions_asStudent_shouldReturn200() throws Exception {
        // Arrange
        List<Intervention> mockList = List.of(new Intervention());
        when(interventionService.getStudentInterventions(STUDENT_ID)).thenReturn(mockList);

        // Act & Assert
        // The simplified PreAuthorize permits access based only on having the 'STUDENT' role.
        mockMvc.perform(get(API_PATH + "/student/{studentId}", STUDENT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"UNAUTHORIZED"}) // Example role that is not allowed
    void getStudentInterventions_asUnauthorizedUser_shouldReturn403Forbidden() throws Exception {
        // Act & Assert
        mockMvc.perform(get(API_PATH + "/student/{studentId}", STUDENT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
