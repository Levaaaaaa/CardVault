package cardvault.CardVault.services.transfer;

import cardvault.CardVault.dto.transfer.TransferRequest;
import cardvault.CardVault.dto.transfer.TransferResponse;
import cardvault.CardVault.enums.CardStatus;
import cardvault.CardVault.card.entity.CardEntity;
import cardvault.CardVault.persistence.entities.TransactionEntity;
import cardvault.CardVault.persistence.entities.UserEntity;
import cardvault.CardVault.card.mappers.CardMapper;
import cardvault.CardVault.card.repositories.CardRepository;
import cardvault.CardVault.persistence.repositories.TransactionRepository;
import cardvault.CardVault.security.GetCurrentUserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
class TransferServiceImpl implements TransferService {
    @Autowired
    private GetCurrentUserService getCurrentUserService;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardMapper cardMapper;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    @Override
    public TransferResponse transfer(TransferRequest request) throws EntityNotFoundException, IllegalStateException {
        UserEntity currentUser = getCurrentUserService.getCurrentUser();
        CardEntity producer = getCardByUUID(UUID.fromString(request.getProducer()));
        CardEntity consumer = getCardByUUID(UUID.fromString(request.getConsumer()));
        if (currentUser.getId().equals(producer.getCardOwner().getId())) {
            //transfer

            try {
                check(request, producer, consumer);
            }
            catch (Exception e) {
                loggingError(request);
                throw e;
            }
            BigDecimal amount = request.getAmount();

            TransferResponse response = buildTransferResponse(request, consumer, producer);
            producer.setBalance(producer.getBalance().subtract(amount));
            consumer.setBalance(consumer.getBalance().add(amount));
            cardRepository.save(producer);
            cardRepository.save(consumer);

            TransactionEntity transactionEntity = TransactionEntity.builder()
                    .id(UUID.randomUUID())
                    .consumer(consumer)
                    .producer(producer)
                    .transactionDate(LocalDateTime.now())
                    .amount(amount)
                    .build();
            transactionRepository.save(transactionEntity);
            log.info("Transaction from card {} to card {} was successful. Amount - {}", producer.getId(), consumer.getId(), amount);
            return response;
        }

        throw new AccessDeniedException("ERROR_CODE_2");
    }

    private CardEntity getCardByUUID(UUID uuid) {
        return cardRepository.findById(uuid).orElseThrow(() -> {return new EntityNotFoundException("ERROR_CODE_1");});
    }

    private void check(TransferRequest request, CardEntity producer, CardEntity consumer) {
        if (producer.getId().equals(consumer.getId())) {
            throw new IllegalStateException("ERROR_CODE_26");
        }
        if (!producer.getStatus().equals(CardStatus.ACTIVE)) {
            throw new IllegalStateException("ERROR_CODE_24");
        }

        if (!consumer.getStatus().equals(CardStatus.ACTIVE)) {
            throw new IllegalStateException("ERROR_CODE_25");
        }
        if (request.getAmount().compareTo(producer.getBalance()) > 0) {
            throw new IllegalStateException("ERROR_CODE_23");
        }
    }

    private TransferResponse buildTransferResponse(TransferRequest request, CardEntity consumer, CardEntity producer) {
        return TransferResponse.builder()
                .consumer(cardMapper.entityToDTO(consumer))
                .producer(cardMapper.entityToDTO(producer))
                .amount(request.getAmount())
                .transferDate(LocalDateTime.now())
                .message("The operation was a success")
                .build();
    }

    private void loggingError(TransferRequest request) {
        log.error("Transaction wasn't successful! Producer - {}, consumer - {}, amount - {}", request.getProducer(), request.getConsumer(), request.getAmount());
    }
}
