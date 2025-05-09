package cardvault.CardVault.core.transactions.controllers;

import cardvault.CardVault.core.transactions.dto.TransferRequest;
import cardvault.CardVault.core.transactions.dto.TransferResponse;
import cardvault.CardVault.core.transactions.services.TransferService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private TransferService transferService;

    @PostMapping
    public ResponseEntity<TransferResponse> transferAmount(@RequestBody @Valid TransferRequest request) {
        return ResponseEntity.status(CREATED).body(transferService.transfer(request));
    }
}
