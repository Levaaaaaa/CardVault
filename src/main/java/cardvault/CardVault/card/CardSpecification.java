package cardvault.CardVault.search;

import cardvault.CardVault.persistence.entities.CardEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CardSpecification {
    public static Specification<CardEntity> doFilter(CardFilterDTO filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filter.getCardNumber() != null) {
                predicates.add(cb.equal(root.get("cardNumber"), filter.getCardNumber().getBytes()));
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
