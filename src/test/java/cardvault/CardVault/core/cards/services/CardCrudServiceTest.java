package cardvault.CardVault.core.cards.services;

import cardvault.CardVault.core.cards.CardStatus;
import cardvault.CardVault.core.cards.dto.CardResponse;
import cardvault.CardVault.core.cards.dto.CreateCardRequest;
import cardvault.CardVault.core.cards.entity.CardEntity;
import cardvault.CardVault.core.cards.mappers.CardMapper;
import cardvault.CardVault.core.cards.repositories.CardRepository;
import cardvault.CardVault.core.users.UserRole;
import cardvault.CardVault.core.users.entities.UserEntity;
import cardvault.CardVault.core.users.mappers.UserMapper;
import cardvault.CardVault.core.users.services.GetCurrentUserService;
import cardvault.CardVault.security.encryption.EncryptionService;
import cardvault.CardVault.security.hash.HashService;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardCrudServiceTest {

    @InjectMocks
    private CardCrudService cardCrudService;

    @Mock
    private CardRepository cardRepository;
    @Mock
    private HashService hashService;
    @Mock
    private EncryptionService encryptionService;
    @Mock
    private GetCurrentUserService getCurrentUserService;
    @Mock
    private CardMapper cardMapper;
    @Mock
    private UserMapper userMapper;

    private UserEntity user;
    private UserEntity admin;
    private CardEntity card;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setId(UUID.randomUUID());
        user.setUserRole(UserRole.ROLE_USER);

        admin = new UserEntity();
        admin.setId(UUID.randomUUID());
        admin.setUserRole(UserRole.ROLE_ADMIN);

        card = CardEntity.builder()
                .id(UUID.randomUUID())
                .cardNumber("1234".getBytes())
                .cardNumberHash("hashed".getBytes())
                .cardOwner(user)
                .validityPeriod(new java.sql.Date(System.currentTimeMillis() + 100000))
                .balance(new BigDecimal("100.00"))
                .status(CardStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void createCard_success() {
        CreateCardRequest req = new CreateCardRequest("1234", new java.sql.Date(System.currentTimeMillis()), new BigDecimal("100.00"));

        when(hashService.hash("1234")).thenReturn("hashed".getBytes());
        when(cardRepository.findByCardNumberHash(any())).thenReturn(Optional.empty());
        when(encryptionService.encrypt("1234")).thenReturn("1234".getBytes());
        when(getCurrentUserService.getCurrentUser()).thenReturn(user);
        when(cardMapper.entityToDTO(any())).thenReturn(new CardResponse());

        CardResponse response = cardCrudService.create(req);

        assertNotNull(response);
        verify(cardRepository).save(any());
    }

    @Test
    void createCard_alreadyExists_throwsException() {
        when(hashService.hash("1234")).thenReturn("hashed".getBytes());
        when(cardRepository.findByCardNumberHash(any())).thenReturn(Optional.of(card));

        CreateCardRequest req = new CreateCardRequest("1234", new java.sql.Date(System.currentTimeMillis()), new BigDecimal("100.00"));

        assertThrows(EntityExistsException.class, () -> cardCrudService.create(req));
    }

    @Test
    void getCards_asUser_returnsOwnCards() {
        when(getCurrentUserService.getCurrentUser()).thenReturn(user);
        when(cardRepository.searchCards(eq(user.getId()), any())).thenReturn(Page.empty());

        Page<CardResponse> result = cardCrudService.getCards(Pageable.unpaged());
        assertNotNull(result);
    }

    @Test
    void getCards_asAdmin_returnsAllCards() {
        when(getCurrentUserService.getCurrentUser()).thenReturn(admin);
        when(cardRepository.searchCards(isNull(), any())).thenReturn(Page.empty());

        Page<CardResponse> result = cardCrudService.getCards(Pageable.unpaged());
        assertNotNull(result);
    }

    @Test
    void getCardByUUID_accessGranted() {
        when(getCurrentUserService.getCurrentUser()).thenReturn(user);
        when(cardRepository.findById(card.getId())).thenReturn(Optional.of(card));
        when(cardMapper.entityToDTO(card)).thenReturn(new CardResponse());

        CardResponse response = cardCrudService.getCardByUUID(card.getId().toString());

        assertNotNull(response);
    }

    @Test
    void getCardByUUID_accessDenied() {
        when(getCurrentUserService.getCurrentUser()).thenReturn(admin); // другой пользователь
        when(cardRepository.findById(card.getId())).thenReturn(Optional.of(card));

        assertThrows(AccessDeniedException.class, () -> cardCrudService.getCardByUUID(card.getId().toString()));
    }

    @Test
    void activateCard_asAdmin_success() {
        when(getCurrentUserService.getCurrentUser()).thenReturn(admin);
        when(cardRepository.findById(card.getId())).thenReturn(Optional.of(card));
        when(cardMapper.entityToDTO(card)).thenReturn(new CardResponse());

        CardResponse result = cardCrudService.activateCard(card.getId());
        assertEquals(CardStatus.ACTIVE, card.getStatus());
        assertNotNull(result);
    }

    @Test
    void activateCard_asUser_forbidden() {
        when(getCurrentUserService.getCurrentUser()).thenReturn(user);

        assertThrows(AccessDeniedException.class, () -> cardCrudService.activateCard(card.getId()));
    }

    @Test
    void blockCard_asOwner_success() {
        when(getCurrentUserService.getCurrentUser()).thenReturn(user);
        when(cardRepository.findById(card.getId())).thenReturn(Optional.of(card));
        when(cardMapper.entityToDTO(card)).thenReturn(new CardResponse());

        CardResponse result = cardCrudService.blockCard(card.getId());
        assertEquals(CardStatus.BLOCKED, card.getStatus());
        assertNotNull(result);
    }

    @Test
    void blockCard_ifExpired_throwsException() {
        card.setStatus(CardStatus.EXPIRED);
        when(getCurrentUserService.getCurrentUser()).thenReturn(admin);
        when(cardRepository.findById(card.getId())).thenReturn(Optional.of(card));

        assertThrows(IllegalStateException.class, () -> cardCrudService.blockCard(card.getId()));
    }

    @Test
    void deleteCard_asAdmin_success() {
        when(getCurrentUserService.getCurrentUser()).thenReturn(admin);
        when(cardRepository.findById(card.getId())).thenReturn(Optional.of(card));

        assertDoesNotThrow(() -> cardCrudService.deleteCard(card.getId().toString()));
        verify(cardRepository).delete(card);
    }

    @Test
    void deleteCard_asUser_throwsAccessDenied() {
        when(getCurrentUserService.getCurrentUser()).thenReturn(user);
        when(cardRepository.findById(card.getId())).thenReturn(Optional.of(card));

        assertThrows(AccessDeniedException.class, () -> cardCrudService.deleteCard(card.getId().toString()));
    }
}
