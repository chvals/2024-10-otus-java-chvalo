package ru.otus.domain;

import java.io.Serializable;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@Table(name = "client")
public final class Client implements Persistable<Long>, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("clientid")
    private Long clientId;

    @Column("name")
    private String name;

    @MappedCollection(idColumn = "clientid")
    private Address address;

    @MappedCollection(idColumn = "clientid")
    private Set<Phone> phones;

    @Transient
    private boolean isNew;

    public Client(Long clientId, String name, Address address, Set<Phone> phones, boolean isNew) {
        this.clientId = clientId;
        this.name = name;
        this.address = address;
        this.phones = phones;
        this.isNew = isNew;
    }

    @PersistenceCreator
    private Client(Long clientId, String name, Address address, Set<Phone> phones) {
        this(clientId, name, address, phones, false);
    }

    @Override
    public Long getId() {
        return clientId;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }
}
