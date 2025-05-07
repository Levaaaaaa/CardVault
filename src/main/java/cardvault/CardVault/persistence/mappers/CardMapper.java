package cardvault.CardVault.persistence.mappers;

import cardvault.CardVault.dto.crud.CardDTO;
import cardvault.CardVault.dto.crud.CardResponse;
import cardvault.CardVault.persistence.entities.CardEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
public interface CardMapper {

    CardResponse entityToDTO(CardEntity entity);

    CardEntity dtoToEntity(CardDTO dto);
}
