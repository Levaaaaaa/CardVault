package cardvault.CardVault.persistence.repositories;

import cardvault.CardVault.enums.CardStatus;
import cardvault.CardVault.persistence.entities.CardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<CardEntity, UUID> {
    @Query("SELECT card from CardEntity card WHERE " +
            "(:owner IS null OR :owner = card.cardOwner.id)")
    public Page<CardEntity> searchCards(@Param("owner") UUID owner, Pageable pageable);
    public Optional<CardEntity> findByCardNumberHash(byte[] hashedNumber);
}
