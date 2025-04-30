package cardvault.CardVault.enums;

public enum CardStatus {
    //('ACTIVE', 'BLOCKED', 'EXPIRED')
    ACTIVE("ACTIVE"),
    BLOCKED("BLOCKED"),
    EXPIRED("EXPIRED");

    private final String status;
    private CardStatus(String s) {
        status = s;
    }
}
