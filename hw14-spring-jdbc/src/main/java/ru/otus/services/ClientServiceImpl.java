package ru.otus.services;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.domain.Client;
import ru.otus.repostory.ClientRepository;
import ru.otus.sessionmanager.TransactionManager;

@Service
public class ClientServiceImpl implements ClientService {
    private static final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);
    private final ClientRepository clientRepository;
    private final TransactionManager transactionManager;

    public ClientServiceImpl(ClientRepository clientRepository, TransactionManager transactionManager) {
        this.clientRepository = clientRepository;
        this.transactionManager = transactionManager;
    }

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public Optional<Client> findById(long id) {
        return clientRepository.findById(id);
    }

    @Override
    public Client save(Client client) {
        return transactionManager.doInTransaction(() -> {
            var savedClient = clientRepository.save(client);
            log.info("saved client: {}", savedClient);
            return savedClient;
        });
    }
}
