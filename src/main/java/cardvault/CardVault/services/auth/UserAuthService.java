package cardvault.CardVault.services.auth;

import cardvault.CardVault.dto.login.LoginRequestDTO;
import cardvault.CardVault.dto.register.RegisterRequestDTO;
import org.springframework.stereotype.Service;

@Service
public interface UserAuthService {
    public String registerUser(RegisterRequestDTO request);
    public String authenticateUser(LoginRequestDTO request);
}
