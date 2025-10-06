package StudentRiskTracker.StudentRiskTracker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FirstUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    // ✅ Add version for optimistic locking to prevent StaleObjectStateException
    @Version
    private Long version;
}
