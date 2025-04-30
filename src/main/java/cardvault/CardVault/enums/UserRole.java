package cardvault.CardVault.enums;

public enum UserRole {
    USER ("USER"),
    ADMIN ("ADMIN");

    private final String role;

    private UserRole(String s) {
        role = s;
    }
}
