package cardvault.CardVault;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

//todo delete exclude when database will connected
@SpringBootApplication
public class CardVaultApplication {

	public static void main(String[] args) {
		SpringApplication.run(CardVaultApplication.class, args);
	}

}
