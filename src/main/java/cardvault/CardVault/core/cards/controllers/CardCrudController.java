package cardvault.CardVault.card.controllers;

import cardvault.CardVault.card.dto.CardFilterDTO;
import cardvault.CardVault.card.dto.CreateCardRequest;
import cardvault.CardVault.card.dto.CardResponse;
import cardvault.CardVault.card.services.CardCrudService;
import cardvault.CardVault.card.services.CardFindService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/cards")
@Validated
public class CardCrudController {
    @Autowired
    private CardCrudService cardCrudService;

    @Autowired
    private CardFindService cardFindService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid CreateCardRequest cardDTO) {
        return ResponseEntity.status(OK).body(cardCrudService.create(cardDTO));
    }

    @GetMapping
    public ResponseEntity<List<CardResponse>> read(
            @PageableDefault(size = 10) Pageable pageable) {
        Page<CardResponse> response = cardCrudService.getCards(pageable);
        return ResponseEntity.status(OK).body(response.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardResponse> getCardByUUID(
            @PathVariable("id")
            @Pattern(
                    regexp = "^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$",
                    message = "ERROR_CODE_19")
            String uuid
    ) {
        return ResponseEntity.status(OK).body(cardCrudService.getCardByUUID(uuid));
    }

    @GetMapping("/search")
    public ResponseEntity<List<CardResponse>> getCardByParameters(CardFilterDTO cardFilter, Pageable pageable) {
        return ResponseEntity.status(OK).body(cardFindService.findCards(cardFilter, pageable).getContent());
    }

    @PatchMapping("/activate/{uuid}")
    public ResponseEntity<CardResponse> activateCard(
            @PathVariable("uuid")
            @Pattern(
                    regexp = "^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$",
                    message = "ERROR_CODE_19")
            String uuid
    ) {
        return ResponseEntity.status(OK).body(cardCrudService.activateCard(UUID.fromString(uuid)));
    }

    @PatchMapping("/block/{uuid}")
    public ResponseEntity<CardResponse> blockCard(
            @PathVariable("id")
            @Pattern(
                    regexp = "^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$",
                    message = "ERROR_CODE_19")
            String uuid
    ) {
        return ResponseEntity.status(OK).body(cardCrudService.blockCard(UUID.fromString(uuid)));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable("id")
            @Pattern(
                    regexp = "^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$",
                    message = "ERROR_CODE_19")
            String uuid
    ) {
        cardCrudService.deleteCard(uuid);
        return ResponseEntity.status(OK).body("The card with UUID + " + uuid + " + was deleted successfully!");
    }
}
