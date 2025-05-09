package cardvault.CardVault.core.users.repositories;

import cardvault.CardVault.core.users.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    public Optional<UserEntity> findByEmail(String email);
}
