package cardvault.CardVault.error_handler.error_factory;

import cardvault.CardVault.error_handler.Error;
import org.springframework.stereotype.Component;

@Component
public interface ErrorFactory {
    public Error buildError(String errorCode);
}
