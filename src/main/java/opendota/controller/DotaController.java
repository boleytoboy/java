package opendota.controller;

import opendota.service.DotaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DotaController {
    private final DotaService service;
    public DotaController(DotaService service) {
        this.service = service;
    }
    @GetMapping(value="/info", produces="application/json")
    public ResponseEntity<String> playerData (@RequestParam("id") String accountId) {
        return ResponseEntity.ok(service.getAccountData(accountId));
    }
}
