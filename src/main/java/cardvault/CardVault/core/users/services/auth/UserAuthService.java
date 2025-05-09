package cardvault.CardVault.core.users.services.auth;

import cardvault.CardVault.core.users.dto.LoginRequestDTO;
import cardvault.CardVault.core.users.dto.RegisterRequestDTO;
import org.springframework.stereotype.Service;

@Service
public interface UserAuthService {
    public String registerUser(RegisterRequestDTO request);
    public String authenticateUser(LoginRequestDTO request);
}
