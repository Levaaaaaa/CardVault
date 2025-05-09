package cardvault.CardVault.core.cards.services;


import cardvault.CardVault.core.cards.CardSpecification;
import cardvault.CardVault.core.cards.dto.CardFilterDTO;
import cardvault.CardVault.core.cards.dto.CardResponse;
import cardvault.CardVault.core.cards.entity.CardEntity;
import cardvault.CardVault.core.cards.mappers.CardMapper;
import cardvault.CardVault.core.cards.repositories.CardRepository;
import cardvault.CardVault.core.users.entities.UserEntity;
import cardvault.CardVault.core.users.services.GetCurrentUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardFindServiceTest {

    @InjectMocks
    private CardFindService cardFindService;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private CardMapper cardMapper;

    @Mock
    private GetCurrentUserService getCurrentUserService;

    @Mock
    private CardSpecification cardSpecification;

    private UserEntity user;
    private UserEntity admin;
    private UUID userId;
    private UUID adminId;

    private Specification<CardEntity> mockSpec;
    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        adminId = UUID.randomUUID();

        user = new UserEntity();
        user.setId(userId);

        admin = new UserEntity();
        admin.setId(adminId);

        mockSpec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
    }

    @Test
    void findCards_adminCanSearchAnyCards() {
        CardFilterDTO filter = new CardFilterDTO();
        Pageable pageable = Pageable.unpaged();

        when(getCurrentUserService.getCurrentUser()).thenReturn(admin);
        when(getCurrentUserService.isAdmin()).thenReturn(true);
        when(cardSpecification.doFilter(filter)).thenReturn(mockSpec);
        when(cardRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(Page.empty());

        Page<CardResponse> result = cardFindService.findCards(filter, pageable);
        assertNotNull(result);
    }

    @Test
    void findCards_userCanSearchOwnCards_whenOwnerIdOmitted() {
        CardFilterDTO filter = new CardFilterDTO(); // ownerId == null
        Pageable pageable = Pageable.unpaged();

        when(getCurrentUserService.getCurrentUser()).thenReturn(user);
        when(getCurrentUserService.isAdmin()).thenReturn(false);
        when(cardSpecification.doFilter(filter)).thenReturn(mockSpec);
        when(cardRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(Page.empty());

        Page<CardResponse> result = cardFindService.findCards(filter, pageable);

        assertNotNull(result);
        assertEquals(userId, filter.getCardOwnerId()); // Проверка, что id пользователя был подставлен
    }

    @Test
    void findCards_userCanSearchOwnCards_whenOwnerIdMatches() {
        CardFilterDTO filter = new CardFilterDTO();
        filter.setCardOwnerId(userId);
        Pageable pageable = Pageable.unpaged();

        when(getCurrentUserService.getCurrentUser()).thenReturn(user);
        when(getCurrentUserService.isAdmin()).thenReturn(false);
        when(cardSpecification.doFilter(filter)).thenReturn(mockSpec);
        when(cardRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(Page.empty());

        Page<CardResponse> result = cardFindService.findCards(filter, pageable);
        assertNotNull(result);
    }

    @Test
    void findCards_userTriesToSearchOtherUserCards_throwsAccessDenied() {
        CardFilterDTO filter = new CardFilterDTO();
        filter.setCardOwnerId(UUID.randomUUID()); // чужой ID
        Pageable pageable = Pageable.unpaged();

        when(getCurrentUserService.getCurrentUser()).thenReturn(user);
        when(getCurrentUserService.isAdmin()).thenReturn(false);

        assertThrows(AccessDeniedException.class, () ->
                cardFindService.findCards(filter, pageable));
    }
}
