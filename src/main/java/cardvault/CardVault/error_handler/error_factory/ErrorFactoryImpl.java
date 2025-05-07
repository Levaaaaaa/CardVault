package cardvault.CardVault.exceptions.error_factory;

import cardvault.CardVault.exceptions.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:error_codes.properties")
class ErrorFactoryImpl implements ErrorFactory{
    @Autowired
    private Environment environment;

    @Override
    public Error buildError(String errorCode) {
        return new Error(errorCode, environment.getProperty(errorCode));
    }
}
