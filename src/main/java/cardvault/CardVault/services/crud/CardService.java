package cardvault.CardVault.services.crud;

import cardvault.CardVault.dto.crud.CreateCardRequest;
import cardvault.CardVault.dto.crud.CardResponse;
import cardvault.CardVault.enums.CardStatus;
import cardvault.CardVault.enums.UserRole;
import cardvault.CardVault.persistence.entities.CardEntity;
import cardvault.CardVault.persistence.entities.UserEntity;
import cardvault.CardVault.persistence.mappers.UserMapper;
import cardvault.CardVault.persistence.repositories.CardRepository;
import cardvault.CardVault.persistence.repositories.UserRepository;
import cardvault.CardVault.security.encryption.EncryptionService;
import cardvault.CardVault.security.hash.HashService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class CardService {
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserRepository userRepository;
//    @Autowired
//    private CardMapper cardMapper;

    @Autowired
    private HashService hashService;

    @Autowired
    private EncryptionService encryptionService;

    @Autowired
    private UserMapper userMapper;

    //todo check card's user existing in db!!!

    //create
    public CardResponse create(CreateCardRequest cardDTO) {
        try {
            CardEntity cardEntity = getCardByNumber(cardDTO.getCardNumber());
        }
        catch (EntityNotFoundException e) {
            UserEntity currentUser = getCurrentUser();
            CardEntity entity = CardEntity.builder().
                    cardNumber(encryptionService.encrypt(cardDTO.getCardNumber()))
                    .id(UUID.randomUUID())
                    .cardNumberHash(hashService.hash(cardDTO.getCardNumber()))
                    .createdAt(LocalDateTime.now())
                    .cardOwner(currentUser)
                    .status(CardStatus.ACTIVE)
                    .balance(BigDecimal.ZERO)
                    .validityPeriod(cardDTO.getValidityPeriod())
                    .build();
            cardRepository.save(entity);
            return buildCardResponse(entity);
        }
        throw new EntityExistsException("ERROR_CODE_5");
    }

    //read
    public Page<CardResponse> getCards(Pageable pageable) {
        UserEntity currentUser = getCurrentUser();
        boolean isAdmin = currentUser.getUserRole().equals(UserRole.ROLE_ADMIN);
        UUID ownerId = isAdmin ? null : currentUser.getId();

        return cardRepository.searchCards(ownerId, pageable).map(
                this::buildCardResponse
        );
    }

    public CardResponse getCardByUUID(String uuid) {
        UserEntity currentUser = getCurrentUser();
        CardEntity cardEntity = getCardByUUID(UUID.fromString(uuid));

        if (currentUser.getId().equals(cardEntity.getCardOwner().getId())) {
            return buildCardResponse(cardEntity);
        }
        throw new AccessDeniedException("ERROR_CODE_2");
    }
    //update
//    public CardResponse updateCardStatus(CreateCardRequest request) throws EntityNotFoundException{
//        //user can only block own card
//        //admin can block or activate any card
//
//        UserEntity currentUser = getCurrentUser();
//        boolean isAdmin = currentUser.getUserRole().equals(UserRole.ROLE_ADMIN);
//
//        CardEntity cardEntity = getCardByNumber(request.getCardNumber());
//        if (cardEntity.getStatus().equals(CardStatus.EXPIRED)) {
//            throw new IllegalStateException("ERROR_CODE_3");
//        }
//        if (isAdmin) {
//            cardEntity.setStatus(request.isBlocked() ? CardStatus.BLOCKED : CardStatus.ACTIVE);
//            return buildCardResponse(cardEntity);
//        }
//
//        if (cardEntity.getCardOwner().getId().equals(currentUser.getId()) && request.isBlocked()){
//            cardEntity.setStatus(CardStatus.BLOCKED);
//            return buildCardResponse(cardEntity);
//        }
//
//        throw new AccessDeniedException("ERROR_CODE_2");
//    }

    //delete
    public void deleteCard(String uuid) {
        UserEntity currentUser = getCurrentUser();
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
    private UserEntity getCurrentUser() {
        Optional<UserEntity> optional = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (optional.isEmpty()) {
            throw new EntityNotFoundException("ERROR_CODE_4");
        }
        return optional.get();
    }

    private CardResponse buildCardResponse(CardEntity card) {
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
                .createdAt(card.getCreatedAt())
                .build();
    }
}
