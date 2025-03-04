package ru.otus.db.demo;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.HwListener;
import ru.otus.cachehw.MyCache;
import ru.otus.db.core.repository.DataTemplateHibernate;
import ru.otus.db.core.repository.HibernateUtils;
import ru.otus.db.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.db.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.db.crm.model.Address;
import ru.otus.db.crm.model.Client;
import ru.otus.db.crm.model.Phone;
import ru.otus.db.crm.service.DbServiceClientImpl;

public class DbServiceDemo {

    private static final Logger log = LoggerFactory.getLogger(DbServiceDemo.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        ///
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        ///
        HwCache<String, Client> myCache = new MyCache<>();
        HwListener<String, Client> listener = new HwListener<String, Client>() {
            @Override
            public void notify(String key, Client value, String action) {
                log.info("key:{}, value:{}, action: {}", key, value, action);
            }
        };
        myCache.addListener(listener);
        ///
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate, myCache);

        dbServiceClient.saveClient(new Client("dbServiceFirst"));

        var clientSecond = dbServiceClient.saveClient(new Client("dbServiceSecond"));
        var clientSecondSelected = dbServiceClient
                .getClient(clientSecond.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getClientId()));
        log.info("clientSecondSelected:{}", clientSecondSelected);
        System.gc();
        log.info("Start GC");
        var clientSecondSelected1 = dbServiceClient
                .getClient(clientSecond.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getClientId()));
        log.info("clientSecondSelected1:{}", clientSecondSelected1);
        var clientSecondSelected2 = dbServiceClient
                .getClient(clientSecond.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getClientId()));
        log.info("clientSecondSelected2:{}", clientSecondSelected2);
        ///
        dbServiceClient.saveClient(new Client(clientSecondSelected.getClientId(), "dbServiceSecondUpdated"));
        var clientUpdated = dbServiceClient
                .getClient(clientSecondSelected.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecondSelected.getClientId()));
        log.info("clientUpdated:{}", clientUpdated);

        dbServiceClient.remove(clientUpdated);

        log.info("All clients");
        dbServiceClient.findAll().forEach(client -> log.info("client:{}", client));
    }
}
