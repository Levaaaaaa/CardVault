package cardvault.CardVault.persistence.mappers;

import cardvault.CardVault.dto.crud.UserDTO;
import cardvault.CardVault.enums.UserRole;
import cardvault.CardVault.persistence.entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
class UserMapperImpl implements UserMapper{
    @Override
    public UserDTO entityToDto(UserEntity entity) {
        return UserDTO.builder()
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .enabled(entity.isEnabled())
                .updatedAt(entity.getUpdatedAt())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    @Override
    public UserEntity dtoToEntity(UserDTO dto) {
        return UserEntity.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .createdAt(dto.getCreatedAt())
                .build();
    }
}
