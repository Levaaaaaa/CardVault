package cardvault.CardVault.core.transactions.services;
import cardvault.CardVault.core.cards.CardStatus;
import cardvault.CardVault.core.cards.entity.CardEntity;
import cardvault.CardVault.core.cards.mappers.CardMapper;
import cardvault.CardVault.core.cards.repositories.CardRepository;
import cardvault.CardVault.core.transactions.dto.TransferRequest;
import cardvault.CardVault.core.transactions.dto.TransferResponse;
import cardvault.CardVault.core.transactions.entities.TransactionEntity;
import cardvault.CardVault.core.transactions.repositories.TransactionRepository;
import cardvault.CardVault.core.users.entities.UserEntity;
import cardvault.CardVault.core.users.services.GetCurrentUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.access.AccessDeniedException;

import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransferServiceImplTest {

    @InjectMocks
    private TransferServiceImpl transferService;

    @Mock
    private GetCurrentUserService getCurrentUserService;
    @Mock
    private CardRepository cardRepository;
    @Mock
    private CardMapper cardMapper;
    @Mock
    private TransactionRepository transactionRepository;

    private UserEntity currentUser;
    private CardEntity producerCard;
    private CardEntity consumerCard;
    private TransferRequest request;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        UUID userId = UUID.randomUUID();
        currentUser = new UserEntity();
        currentUser.setId(userId);

        producerCard = new CardEntity();
        producerCard.setId(UUID.randomUUID());
        producerCard.setCardOwner(currentUser);
        producerCard.setBalance(new BigDecimal("1000"));
        producerCard.setStatus(CardStatus.ACTIVE);

        consumerCard = new CardEntity();
        consumerCard.setId(UUID.randomUUID());
        consumerCard.setCardOwner(new UserEntity());
        consumerCard.setBalance(new BigDecimal("500"));
        consumerCard.setStatus(CardStatus.ACTIVE);

        request = new TransferRequest();
        request.setProducer(producerCard.getId().toString());
        request.setConsumer(consumerCard.getId().toString());
        request.setAmount(new BigDecimal("300"));
    }

    @Test
    void transfer_SuccessfulTransfer_ReturnsResponse() {
        when(getCurrentUserService.getCurrentUser()).thenReturn(currentUser);
        when(cardRepository.findById(producerCard.getId())).thenReturn(Optional.of(producerCard));
        when(cardRepository.findById(consumerCard.getId())).thenReturn(Optional.of(consumerCard));
        when(cardMapper.entityToDTO(any())).thenAnswer(invocation -> {
            CardEntity card = invocation.getArgument(0);
            return new cardvault.CardVault.core.cards.dto.CardResponse(card.getId(), null, null, null, card.getBalance(), card.getStatus(), null);
        });

        TransferResponse response = transferService.transfer(request);

        assertNotNull(response);
        assertEquals(new BigDecimal("700"), producerCard.getBalance());
        assertEquals(new BigDecimal("800"), consumerCard.getBalance());
        verify(cardRepository, times(2)).save(any(CardEntity.class));
        verify(transactionRepository).save(any(TransactionEntity.class));
    }

    @Test
    void transfer_NotCardOwner_ThrowsAccessDenied() {
        producerCard.setCardOwner(new UserEntity()); // not current user
        when(getCurrentUserService.getCurrentUser()).thenReturn(currentUser);
        when(cardRepository.findById(producerCard.getId())).thenReturn(Optional.of(producerCard));
        when(cardRepository.findById(consumerCard.getId())).thenReturn(Optional.of(consumerCard));

        AccessDeniedException exception = assertThrows(AccessDeniedException.class,
                () -> transferService.transfer(request));

        assertEquals("ERROR_CODE_2", exception.getMessage());
    }

    @Test
    void transfer_ProducerNotFound_ThrowsEntityNotFound() {
        when(getCurrentUserService.getCurrentUser()).thenReturn(currentUser);
        when(cardRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> transferService.transfer(request));
    }

    @Test
    void transfer_ConsumerNotFound_ThrowsEntityNotFound() {
        when(getCurrentUserService.getCurrentUser()).thenReturn(currentUser);
        when(cardRepository.findById(UUID.fromString(request.getProducer()))).thenReturn(Optional.of(producerCard));
        when(cardRepository.findById(UUID.fromString(request.getConsumer()))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> transferService.transfer(request));
    }

    @Test
    void transfer_ProducerNotActive_ThrowsIllegalState() {
        producerCard.setStatus(CardStatus.BLOCKED);
        when(getCurrentUserService.getCurrentUser()).thenReturn(currentUser);
        when(cardRepository.findById(producerCard.getId())).thenReturn(Optional.of(producerCard));
        when(cardRepository.findById(consumerCard.getId())).thenReturn(Optional.of(consumerCard));

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> transferService.transfer(request));

        assertEquals("ERROR_CODE_24", exception.getMessage());
    }

    @Test
    void transfer_InsufficientBalance_ThrowsIllegalState() {
        request.setAmount(new BigDecimal("2000"));
        when(getCurrentUserService.getCurrentUser()).thenReturn(currentUser);
        when(cardRepository.findById(producerCard.getId())).thenReturn(Optional.of(producerCard));
        when(cardRepository.findById(consumerCard.getId())).thenReturn(Optional.of(consumerCard));

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> transferService.transfer(request));

        assertEquals("ERROR_CODE_23", exception.getMessage());
    }

    @Test
    void transfer_ProducerEqualsConsumer_ThrowsIllegalState() {
        request.setConsumer(request.getProducer());
        when(getCurrentUserService.getCurrentUser()).thenReturn(currentUser);
        when(cardRepository.findById(producerCard.getId())).thenReturn(Optional.of(producerCard));

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> transferService.transfer(request));

        assertEquals("ERROR_CODE_26", exception.getMessage());
    }
}
