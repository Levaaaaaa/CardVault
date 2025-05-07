package cardvault.CardVault.card.services;

import cardvault.CardVault.card.CardSpecification;
import cardvault.CardVault.card.dto.CardFilterDTO;
import cardvault.CardVault.card.dto.CardResponse;
import cardvault.CardVault.card.mappers.CardMapper;
import cardvault.CardVault.card.repositories.CardRepository;
import cardvault.CardVault.persistence.entities.UserEntity;
import cardvault.CardVault.security.GetCurrentUserService;
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
