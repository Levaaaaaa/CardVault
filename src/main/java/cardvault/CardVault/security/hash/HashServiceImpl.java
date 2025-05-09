package cardvault.CardVault.security.hash;

import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
class HashServiceImpl implements HashService{
    @Override
    public byte[] hash(String input) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            return messageDigest.digest(input.getBytes());
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 is not supported!");
        }
    }
}
