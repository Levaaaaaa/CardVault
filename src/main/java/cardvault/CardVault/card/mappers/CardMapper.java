package cardvault.CardVault.card.mapper;

import cardvault.CardVault.card.dto.CardDTO;
import cardvault.CardVault.card.dto.CardResponse;
import cardvault.CardVault.card.entity.CardEntity;
import org.springframework.stereotype.Component;

@Component
public interface CardMapper {

    CardResponse entityToDTO(CardEntity entity);

    CardEntity dtoToEntity(CardDTO dto);
}
