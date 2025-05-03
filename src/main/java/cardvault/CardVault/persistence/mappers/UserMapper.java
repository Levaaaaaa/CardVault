package cardvault.CardVault.persistence.mappers;

import cardvault.CardVault.dto.crud.UserDTO;
import cardvault.CardVault.persistence.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
//@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO entityToDto(UserEntity entity);

//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "userRole", ignore = true)
//    @Mapping(target = "password", ignore = true)
    UserEntity dtoToEntity(UserDTO dto);
}
