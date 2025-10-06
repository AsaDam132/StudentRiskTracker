package StudentRiskTracker.StudentRiskTracker.config;

import StudentRiskTracker.StudentRiskTracker.auth.JwtAuthFilter;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;
    // Assuming PasswordEncoder is available as a bean elsewhere (e.g., PasswordConfig)

    // --- Core Spring Security Beans ---

    @Bean
    public DaoAuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // --- Security Filter Chain ---

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Public Endpoints (Authentication and API Docs)
                        .requestMatchers("/api/auth/login", "/api/auth/register", "/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // Rule for Interventions: Allowed for TEACHER, ADMIN, and STUDENT roles
                        .requestMatchers("/api/interventions/**").hasAnyRole("TEACHER", "ADMIN","STUDENT")

                        // Rule for Performance: Allowed for TEACHER, ADMIN, PARENT, and STUDENT roles
                        .requestMatchers("/api/performance/**").hasAnyRole("TEACHER", "ADMIN", "PARENT", "STUDENT")

                        // All other requests must be authenticated
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // FIX: Using the injected parameter instead of the direct method call
                .authenticationProvider(authenticationProvider(null)) // NOTE: 'null' here requires PasswordEncoder to be set correctly via DI for 'authenticationProvider'
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // --- OpenAPI/Swagger Configuration ---

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Swagger Auth Token Example API")
                        .description("Demo for centralized token authentication using Swagger @SecurityRequirement.")
                        .version("1.0.0"))
                .addSecurityItem(new SecurityRequirement().addList("authorization"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("authorization",
                                new SecurityScheme()
                                        .name("authorization")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(SecurityScheme.In.HEADER)
                                        .description("JWT auth description")));
    }
}