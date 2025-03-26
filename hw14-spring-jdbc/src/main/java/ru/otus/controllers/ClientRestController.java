package ru.otus.controllers;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import ru.otus.domain.Client;
import ru.otus.services.ClientService;

@RestController
public class ClientRestController {

    private final ClientService clientService;

    public ClientRestController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/api/client")
    public List<Client> getClientList() {
        return clientService.findAll();
    }

    @PostMapping("/api/client")
    public Client saveClient(@RequestBody Client client) {
        client.setNew(true);
        return clientService.save(client);
    }
}
