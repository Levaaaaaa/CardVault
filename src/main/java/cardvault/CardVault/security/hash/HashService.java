package cardvault.CardVault.security.hash;

import org.springframework.stereotype.Service;

@Service
public interface HashService {
    public byte[] hash(String input);
}
