package ru.otus.db.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.MyCache;
import ru.otus.db.core.repository.DataTemplate;
import ru.otus.db.core.sessionmanager.TransactionManager;
import ru.otus.db.crm.model.Client;

import java.util.List;
import java.util.Optional;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final DataTemplate<Client> clientDataTemplate;
    private final TransactionManager transactionManager;
    private final HwCache<String, Client> myCache;

    public DbServiceClientImpl(TransactionManager transactionManager, DataTemplate<Client> clientDataTemplate, HwCache<String, Client> myCache) {
        this.transactionManager = transactionManager;
        this.clientDataTemplate = clientDataTemplate;
        this.myCache = myCache;
    }

    @Override
    public Client saveClient(Client client) {
        Client newClient = transactionManager.doInTransaction(session -> {
            var clientCloned = client.clone();
            if (client.getClientId() == null) {
                var savedClient = clientDataTemplate.insert(session, clientCloned);
                log.info("created client: {}", clientCloned);
                return savedClient;
            }
            var savedClient = clientDataTemplate.update(session, clientCloned);
            log.info("updated client: {}", savedClient);
            return savedClient;
        });
        myCache.put(getKeyName(newClient.getClientId()), newClient.clone());
        return newClient;
    }

    @Override
    public Optional<Client> getClient(long id) {
        Optional<Client> cacheClient = myCache.get(getKeyName(id));
        if (!cacheClient.isPresent()) {
            Optional<Client> optionalClient = transactionManager.doInReadOnlyTransaction(session -> {
                var clientOptional = clientDataTemplate.findById(session, id);
                log.info("client: {}", clientOptional);
                return clientOptional;
            });
            if (optionalClient.isPresent()) {
                myCache.put(getKeyName(id), optionalClient.get().clone());
            }
            return optionalClient;
        } else {
            return Optional.of(cacheClient.get().clone());
        }
    }

    @Override
    public List<Client> findAll() {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientList = clientDataTemplate.findAll(session);
            log.info("clientList:{}", clientList);
            return clientList;
        });
    }

    @Override
    public void remove(Client client) {
        transactionManager.doInTransaction(session -> {
            clientDataTemplate.remove(session, client);
            log.info("remove client: {}", client);
            return null;
        });
        myCache.remove(getKeyName(client.getClientId()));
    }

    private String getKeyName(long val) {
        return String.valueOf(val);
    }
}
