package cardvault.CardVault.persistence.mappers;

import cardvault.CardVault.dto.crud.CardDTO;
import cardvault.CardVault.persistence.entities.CardEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

//@Component
@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface CardMapper {

//    @Mapping(target = "cardNumber", source = "cardNumber", qualifiedByName = "bytesToString")
    CardDTO entityToDTO(CardEntity entity);

//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "cardNumber", source = "cardNumber", qualifiedByName = "stringToBytes")
//    @Mapping(target = "cardNumberHash", ignore = true)
    CardEntity dtoToEntity(CardDTO dto);
}
