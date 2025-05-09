package cardvault.CardVault.core.transactions.services;

import cardvault.CardVault.core.transactions.dto.TransferRequest;
import cardvault.CardVault.core.transactions.dto.TransferResponse;
import org.springframework.stereotype.Service;

@Service
public interface TransferService {
    public TransferResponse transfer(TransferRequest request);
}
