package cardvault.CardVault.security.encryption;

import org.springframework.stereotype.Service;

@Service
public interface EncryptionService {
    public byte[] encrypt(String plainText);
    public String decrypt(byte[] cipherText);
    public String masked(String cardNumber);
}
