package cardvault.CardVault.security.auth.services;

import cardvault.CardVault.security.auth.dto.login.LoginRequestDTO;
import cardvault.CardVault.security.auth.dto.register.RegisterRequestDTO;
import org.springframework.stereotype.Service;

@Service
public interface UserAuthService {
    public String registerUser(RegisterRequestDTO request);
    public String authenticateUser(LoginRequestDTO request);
}
