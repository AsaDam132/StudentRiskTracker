package StudentRiskTracker.StudentRiskTracker.controller;

import StudentRiskTracker.StudentRiskTracker.dto.StudentRiskAssessment;
import StudentRiskTracker.StudentRiskTracker.service.RiskAssessmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * JUnit 5 test class for RiskAssessmentController.
 * Uses @WebMvcTest to load only the controller layer for efficient testing.
 */
@WebMvcTest(
        controllers = RiskAssessmentController.class,
        // Exclude SecurityConfig to use Mock Security context
        excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*SecurityConfig.*")
)
class RiskAssessmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RiskAssessmentService riskAssessmentService;

    // --- Placeholder Classes for Testing (Simulating DTOs/Models) ---
    // Note: StudentRiskAssessment must be defined elsewhere, this is for mocking.

    // Define constants
    private static final String API_PATH = "/api/risk-assessment/students/{studentId}";
    private static final String STUDENT_ID = "S456";
    private static final String SEMESTER = "FALL2025";

    // --- Test Cases for getStudentRiskAssessment (GET /api/risk-assessment/students/{studentId}) ---

    @Test
    @WithMockUser(roles = {"TEACHER"})
    void getStudentRiskAssessment_asTeacher_shouldReturn200AndRiskScore() throws Exception {
        // Arrange
        StudentRiskAssessment mockAssessment = new StudentRiskAssessment(STUDENT_ID, "0.85");
        when(riskAssessmentService.calculateRiskScore(anyString(), anyString()))
                .thenReturn(mockAssessment);

        // Act & Assert
        mockMvc.perform(get(API_PATH, STUDENT_ID)
                        .param("semester", SEMESTER)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Verify the riskScore field exists and matches the mocked data
                .andExpect(jsonPath("$.riskScore").value(0.85));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void getStudentRiskAssessment_asAdmin_shouldReturn200AndRiskScore() throws Exception {
        // Arrange
        StudentRiskAssessment mockAssessment = new StudentRiskAssessment(STUDENT_ID, "0.85");
        when(riskAssessmentService.calculateRiskScore(anyString(), anyString()))
                .thenReturn(mockAssessment);

        // Act & Assert
        mockMvc.perform(get(API_PATH, STUDENT_ID)
                        .param("semester", SEMESTER)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // Verify the risk score field exists and matches the mocked data
                .andExpect(jsonPath("$.riskScore").value(0.92));
    }

    @Test
    @WithMockUser(roles = {"STUDENT"}, username = STUDENT_ID) // Unauthorized role
    void getStudentRiskAssessment_asStudent_shouldReturn403Forbidden() throws Exception {
        // Act & Assert
        mockMvc.perform(get(API_PATH, STUDENT_ID)
                        .param("semester", SEMESTER)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"PARENT"}) // Unauthorized role
    void getStudentRiskAssessment_asParent_shouldReturn403Forbidden() throws Exception {
        // Act & Assert
        mockMvc.perform(get(API_PATH, STUDENT_ID)
                        .param("semester", SEMESTER)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void getStudentRiskAssessment_withoutUser_shouldReturn401Unauthorized() throws Exception {
        // Act & Assert
        mockMvc.perform(get(API_PATH, STUDENT_ID)
                        .param("semester", SEMESTER)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
