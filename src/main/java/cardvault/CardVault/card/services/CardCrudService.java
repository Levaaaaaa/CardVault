package cardvault.CardVault.card.services;

import cardvault.CardVault.card.repositories.CardRepository;
import cardvault.CardVault.card.dto.CreateCardRequest;
import cardvault.CardVault.card.dto.CardResponse;
import cardvault.CardVault.enums.CardStatus;
import cardvault.CardVault.enums.UserRole;
import cardvault.CardVault.card.entity.CardEntity;
import cardvault.CardVault.persistence.entities.UserEntity;
import cardvault.CardVault.card.mapper.CardMapper;
import cardvault.CardVault.persistence.mappers.UserMapper;
import cardvault.CardVault.security.GetCurrentUserService;
import cardvault.CardVault.security.encryption.EncryptionService;
import cardvault.CardVault.security.hash.HashService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class CardService {
    @Autowired
    private CardRepository cardRepository;

//    @Autowired
//    private CardMapper cardMapper;

    @Autowired
    private HashService hashService;

    @Autowired
    private EncryptionService encryptionService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private GetCurrentUserService getCurrentUserService;
    //todo check card's user existing in db!!!

    @Autowired
    private CardMapper cardMapper;
    //create
    public CardResponse create(CreateCardRequest cardDTO) {
        try {
            CardEntity cardEntity = getCardByNumber(cardDTO.getCardNumber());
        }
        catch (EntityNotFoundException e) {
            UserEntity currentUser = getCurrentUserService.getCurrentUser();
            CardEntity entity = CardEntity.builder().
                    cardNumber(encryptionService.encrypt(cardDTO.getCardNumber()))
                    .id(UUID.randomUUID())
                    .cardNumberHash(hashService.hash(cardDTO.getCardNumber()))
                    .createdAt(LocalDateTime.now())
                    .cardOwner(currentUser)
                    .status(CardStatus.ACTIVE)
                    .balance(cardDTO.getStartBalance())
                    .validityPeriod(cardDTO.getValidityPeriod())
                    .build();
            cardRepository.save(entity);
            return cardMapper.entityToDTO(entity);
        }
        throw new EntityExistsException("ERROR_CODE_5");
    }

    //read
    public Page<CardResponse> getCards(Pageable pageable) {
        UserEntity currentUser = getCurrentUserService.getCurrentUser();
        boolean isAdmin = currentUser.getUserRole().equals(UserRole.ROLE_ADMIN);
        UUID ownerId = isAdmin ? null : currentUser.getId();

        return cardRepository.searchCards(ownerId, pageable).map(
                cardMapper::entityToDTO
        );
    }

    public CardResponse getCardByUUID(String uuid) {
        UserEntity currentUser = getCurrentUserService.getCurrentUser();
        CardEntity cardEntity = getCardByUUID(UUID.fromString(uuid));

        if (currentUser.getId().equals(cardEntity.getCardOwner().getId())) {
            return cardMapper.entityToDTO(cardEntity);
        }
        throw new AccessDeniedException("ERROR_CODE_2");
    }

    public CardResponse activateCard(UUID cardUUID) throws EntityNotFoundException {
        if (getCurrentUserService.getCurrentUser().getUserRole().equals(UserRole.ROLE_USER)) {
            throw new AccessDeniedException("ERROR_CODE_2");
        }
        return updateCardStatus(cardUUID, CardStatus.ACTIVE);
    }

    public CardResponse blockCard(UUID cardUUID) throws EntityNotFoundException {
        return updateCardStatus(cardUUID, CardStatus.BLOCKED);
    }
    //update
    private CardResponse updateCardStatus(UUID cardUUID, CardStatus status) throws EntityNotFoundException{
        //user can only block own card
        //admin can block or activate any card

        UserEntity currentUser = getCurrentUserService.getCurrentUser();
        boolean isAdmin = currentUser.getUserRole().equals(UserRole.ROLE_ADMIN);

        CardEntity cardEntity = getCardByUUID(cardUUID);
        if (cardEntity.getStatus().equals(CardStatus.EXPIRED)) {
            throw new IllegalStateException("ERROR_CODE_3");
        }
        if (isAdmin) {
            cardEntity.setStatus(status);
            return cardMapper.entityToDTO(cardEntity);
        }

        if (cardEntity.getCardOwner().getId().equals(currentUser.getId()) && !status.equals(CardStatus.BLOCKED)){
            cardEntity.setStatus(status);
            return cardMapper.entityToDTO(cardEntity);
        }

        throw new AccessDeniedException("ERROR_CODE_2");
    }

    //delete
    public void deleteCard(String uuid) {
        UserEntity currentUser = getCurrentUserService.getCurrentUser();
        boolean isAdmin = currentUser.getUserRole().equals(UserRole.ROLE_ADMIN);

        CardEntity card = getCardByUUID(UUID.fromString(uuid));

        //has permission
        if (isAdmin) {
            cardRepository.delete(card);
            return;
        }
        throw new AccessDeniedException("ERROR_CODE_2");
    }

    private CardEntity getCardByNumber(String cardNumber) {
        Optional<CardEntity> optionalCard = cardRepository.findByCardNumberHash(hashService.hash(cardNumber));
        if (optionalCard.isEmpty()) {
            throw new EntityNotFoundException("ERROR_CODE_1");
        }
        return optionalCard.get();
    }

    private CardEntity getCardByUUID(UUID uuid) {
        Optional<CardEntity> optionalCard = cardRepository.findById(uuid);
        if (optionalCard.isEmpty()) {
            throw new EntityNotFoundException("ERROR_CODE_1");
        }
        return optionalCard.get();
    }
}
