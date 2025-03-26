package ru.otus.services;

import java.util.List;
import java.util.Optional;
import ru.otus.domain.Client;

public interface ClientService {
    List<Client> findAll();

    Optional<Client> findById(long id);

    Client save(Client client);
}
