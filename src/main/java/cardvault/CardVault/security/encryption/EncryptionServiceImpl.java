package cardvault.CardVault.security.encryption;

import jakarta.annotation.PostConstruct;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

@Service
@Setter
class EncryptionServiceImpl implements EncryptionService{

    @Value("${app.encrypt.card_number.algorithm}")
    private String ALGORITHM;

    @Value("${app.encrypt.card_number.secret}")
    private String SECRET;

    private SecretKeySpec secretKeySpec;

    @PostConstruct
    public void init() {
        this.secretKeySpec = new SecretKeySpec(SECRET.getBytes(), ALGORITHM);
    }
    @Override
    public byte[] encrypt(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            return cipher.doFinal(plainText.getBytes());
        }
        catch (Exception e) {
            throw new RuntimeException("Encryption failed!");
        }
    }

    @Override
    public String decrypt(byte[] cipherText) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            return new String(cipher.doFinal(cipherText));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Decryption failed!");
        }
    }

    @Override
    public String masked(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 8)
        {
            return "****";
        }
        String last4 = cardNumber.substring(cardNumber.length() - 4);
        return "**** **** **** " + last4;
    }
}
