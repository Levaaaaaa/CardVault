package cardvault.CardVault.core.cards.mappers;

import cardvault.CardVault.core.cards.dto.CardResponse;
import cardvault.CardVault.core.cards.entity.CardEntity;
import org.springframework.stereotype.Component;

@Component
public interface CardMapper {

    CardResponse entityToDTO(CardEntity entity);

}
