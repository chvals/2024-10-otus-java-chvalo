package ru.otus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.util.resource.PathResourceFactory;
import org.eclipse.jetty.util.resource.Resource;
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
import ru.otus.helpers.FileSystemHelper;
import ru.otus.webserver.server.UsersWebServer;
import ru.otus.webserver.server.UsersWebServerWithBasicSecurity;
import ru.otus.webserver.services.TemplateProcessor;
import ru.otus.webserver.services.TemplateProcessorImpl;

import java.net.URI;
import java.util.List;

/*
    Полезные для демо ссылки

    // Стартовая страница
    http://localhost:8080
*/
public class WebServerWithBasicSecurityDemo {
    private static final int WEB_SERVER_PORT = 8081;
    private static final String TEMPLATES_DIR = "/templates/";
    private static final String HASH_LOGIN_SERVICE_CONFIG_NAME = "realm.properties";
    private static final String REALM_NAME = "AnyRealm";

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    private static final Logger log = LoggerFactory.getLogger(WebServerWithBasicSecurityDemo.class);

    public static void main(String[] args) throws Exception {
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
        var client1 = new Client(
                null,
                "Vasya",
                new Address(null, "AnyStreet"),
                List.of(new Phone(null, "13-555-22"), new Phone(null, "14-666-333")));
        var client2 = new Client(
                null,
                "Andrey",
                new Address(null, "AnyStreet"),
                List.of(new Phone(null, "13-555-11"), new Phone(null, "14-666-444")));
        dbServiceClient.saveClient(client1);
        dbServiceClient.saveClient(client2);

        ObjectMapper mapper = JsonMapper.builder().build();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        String hashLoginServiceConfigPath =
                FileSystemHelper.localFileNameOrResourceNameToFullPath(HASH_LOGIN_SERVICE_CONFIG_NAME);
        PathResourceFactory pathResourceFactory = new PathResourceFactory();
        Resource configResource = pathResourceFactory.newResource(URI.create(hashLoginServiceConfigPath));

        LoginService loginService = new HashLoginService(REALM_NAME, configResource);

        UsersWebServer usersWebServer =
                new UsersWebServerWithBasicSecurity(WEB_SERVER_PORT, loginService, dbServiceClient, mapper, templateProcessor);

        usersWebServer.start();
        usersWebServer.join();
    }
}
