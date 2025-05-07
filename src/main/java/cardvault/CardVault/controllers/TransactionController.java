package cardvault.CardVault.controllers;

import cardvault.CardVault.dto.transfer.TransferRequest;
import cardvault.CardVault.dto.transfer.TransferResponse;
import cardvault.CardVault.services.transfer.TransferService;
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
