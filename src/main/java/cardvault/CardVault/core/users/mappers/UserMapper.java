package cardvault.CardVault.core.users.mappers;

import cardvault.CardVault.core.users.dto.UserDTO;
import cardvault.CardVault.core.users.entities.UserEntity;
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
