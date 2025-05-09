package cardvault.CardVault.core.cards.services;

import cardvault.CardVault.core.cards.CardSpecification;
import cardvault.CardVault.core.cards.dto.CardFilterDTO;
import cardvault.CardVault.core.cards.dto.CardResponse;
import cardvault.CardVault.core.cards.mappers.CardMapper;
import cardvault.CardVault.core.cards.repositories.CardRepository;
import cardvault.CardVault.core.users.entities.UserEntity;
import cardvault.CardVault.core.users.services.GetCurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;


@Service
public class CardFindService {
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardMapper cardMapper;

    @Autowired
    private GetCurrentUserService getCurrentUserService;

    @Autowired
    private CardSpecification cardSpecification;

    public Page<CardResponse> findCards(CardFilterDTO cardFilter, Pageable pageable) {
        UserEntity currentUser = getCurrentUserService.getCurrentUser();
        boolean isAdmin = getCurrentUserService.isAdmin();
        //user can see only own cards
        if (!isAdmin && cardFilter.getCardOwnerId() == null) {
            cardFilter.setCardOwnerId(currentUser.getId());
        }

        if (
                isAdmin || currentUser.getId().equals(cardFilter.getCardOwnerId())
        ) {
            return cardRepository.findAll(cardSpecification.doFilter(cardFilter), pageable).map(card -> {return cardMapper.entityToDTO(card);});
        }
        throw new AccessDeniedException("ERROR_CODE_2");
    }
}
