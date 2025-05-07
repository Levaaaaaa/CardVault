package cardvault.CardVault.card;

import cardvault.CardVault.card.dto.CardFilterDTO;
import cardvault.CardVault.card.entity.CardEntity;
import cardvault.CardVault.security.encryption.EncryptionService;
import cardvault.CardVault.security.hash.HashService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CardSpecification {
    @Autowired
    private HashService hashService;

    public Specification<CardEntity> doFilter(CardFilterDTO filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filter.getCardNumber() != null) {
                predicates.add(cb.equal(root.get("cardNumberHash"), hashService.hash(filter.getCardNumber())));
            }
            if (filter.getCardOwnerId() != null) {
                predicates.add(cb.equal(root.get("cardOwner").get("id"), filter.getCardOwnerId()));
            }
            if (filter.getValidityFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("validityPeriod"), filter.getValidityFrom()));
            }
            if (filter.getValidityFrom() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("validityPeriod"), filter.getValidityTo()));
            }
            if (filter.getBalanceMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("balance"), filter.getBalanceMin()));
            }
            if (filter.getBalanceMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("balance"), filter.getBalanceMax()));
            }
            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }
            if (filter.getCreateAfter() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), filter.getCreateAfter()));
            }
            if (filter.getCreateBefore() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), filter.getCreateBefore()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
