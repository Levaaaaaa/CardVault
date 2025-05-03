package cardvault.CardVault.controllers;

import cardvault.CardVault.dto.crud.CreateCardRequest;
import cardvault.CardVault.dto.crud.CardResponse;
import cardvault.CardVault.services.crud.CardService;
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

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/cards")
@Validated
public class CardCrudController {
    @Autowired
    private CardService cardService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid CreateCardRequest cardDTO) {
        return ResponseEntity.status(OK).body(cardService.create(cardDTO));
    }

    @GetMapping
    public ResponseEntity<List<CardResponse>> read(
            @PageableDefault(size = 10) Pageable pageable) {
        Page<CardResponse> response = cardService.getCards(pageable);
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
        return ResponseEntity.status(OK).body(cardService.getCardByUUID(uuid));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable("id")
            @Pattern(
                    regexp = "^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$",
                    message = "ERROR_CODE_19")
            String uuid
    ) {
        cardService.deleteCard(uuid);
        return ResponseEntity.status(OK).body("The card with UUID + " + uuid + " + was deleted successfully!");
    }
}
