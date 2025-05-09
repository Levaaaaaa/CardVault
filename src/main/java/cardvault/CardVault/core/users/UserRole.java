package cardvault.CardVault.core.users;

public enum UserRole {
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN");

    private final String role;

    private UserRole(String s) {
        role = s;
    }
}
