package cardvault.CardVault.controllers;

import cardvault.CardVault.dto.login.LoginRequestDTO;
import cardvault.CardVault.dto.register.RegisterRequestDTO;
import cardvault.CardVault.services.auth.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserAuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO request) {
        try {
            String token = authService.registerUser(request);
            return ResponseEntity.status(OK).body(token);
        }
        catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
        try {
            String token = authService.authenticateUser(request);
            return ResponseEntity.status(OK).body(token);
        }
        catch (Exception e) {
            return ResponseEntity.status(UNAUTHORIZED).body(e.getMessage());
        }
    }
}
