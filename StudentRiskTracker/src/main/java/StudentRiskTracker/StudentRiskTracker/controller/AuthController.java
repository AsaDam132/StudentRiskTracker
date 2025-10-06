package StudentRiskTracker.StudentRiskTracker.controller;


import StudentRiskTracker.StudentRiskTracker.model.FirstUser;
import StudentRiskTracker.StudentRiskTracker.service.FirstUserService;
import StudentRiskTracker.StudentRiskTracker.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private FirstUserService firstUserService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody FirstUser user) {
        return ResponseEntity.ok(firstUserService.registerUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody FirstUser loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails authenticatedUser = (UserDetails) authentication.getPrincipal();
        String token = jwtUtils.generateToken(authenticatedUser.getUsername());

        return ResponseEntity.ok().body("Bearer " + token);
    }
}
