package cardvault.CardVault.exceptions.error_factory;

import cardvault.CardVault.exceptions.Error;
import org.springframework.stereotype.Component;

@Component
public interface ErrorFactory {
    public Error buildError(String errorCode);
}
