package cardvault.CardVault.persistence.mappers;

import cardvault.CardVault.dto.crud.CardDTO;
import cardvault.CardVault.dto.crud.CardResponse;
import cardvault.CardVault.persistence.entities.CardEntity;
import cardvault.CardVault.security.encryption.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class CardMapperImpl implements CardMapper{

    @Autowired
    private EncryptionService encryptionService;

    @Autowired
    private UserMapper userMapper;

    @Override
    public CardResponse entityToDTO(CardEntity card) {
        return CardResponse.builder()
                    .maskedCardNumber(
                            encryptionService.masked(
                                    encryptionService.decrypt(card.getCardNumber())
                            )
                    )
                    .cardUuid(card.getId())
                    .validityPeriod(card.getValidityPeriod())
                    .cardOwner(userMapper.entityToDto(card.getCardOwner()))
                    .status(card.getStatus())
                    .amount(card.getBalance())
                    .createdAt(card.getCreatedAt())
                    .build();
    }

    @Override
    public CardEntity dtoToEntity(CardDTO dto) {
        return null;
    }
}
