package cardvault.CardVault.security.auth.controllers;

import cardvault.CardVault.security.auth.dto.login.LoginRequestDTO;
import cardvault.CardVault.security.auth.dto.register.RegisterRequestDTO;
import cardvault.CardVault.security.auth.services.UserAuthService;
import jakarta.validation.Valid;
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
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequestDTO request) {
        String token = authService.registerUser(request);
        return ResponseEntity.status(OK).body(token);

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO request) {
        String token = authService.authenticateUser(request);
        return ResponseEntity.status(OK).body(token);
    }
}
