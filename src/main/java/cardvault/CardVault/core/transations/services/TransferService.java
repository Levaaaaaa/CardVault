package cardvault.CardVault.services.transfer;

import cardvault.CardVault.dto.transfer.TransferRequest;
import cardvault.CardVault.dto.transfer.TransferResponse;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public interface TransferService {
    public TransferResponse transfer(TransferRequest request);
}
