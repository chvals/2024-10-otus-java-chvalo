package ru.otus;

import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.otus.domain.Address;
import ru.otus.domain.Client;
import ru.otus.domain.Phone;
import ru.otus.services.ClientService;

@Component("actionDemo")
public class ActionDemo implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(ActionDemo.class);

    private final ClientService clientService;

    public ActionDemo(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public void run(String... args) {
        /// создаем Client
        var client1 = new Client(
                null,
                "Vasya",
                new Address(null, null, "AnyStreet"),
                Set.of(new Phone(null, null, "13-555-22"), new Phone(null, null, "14-666-333")),
                true);
        var client2 = new Client(
                null,
                "Andrey",
                new Address(null, null, "AnyStreet"),
                Set.of(new Phone(null, null, "13-555-11"), new Phone(null, null, "14-666-444")),
                true);
        clientService.save(client1);
        clientService.save(client2);
    }
}
