package ru.otus.crm.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Data
@NoArgsConstructor
@Entity
@Table(name = "client")
public final class Client implements Cloneable {
    @Id
    @SequenceGenerator(name = "client_gen", sequenceName = "client_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_gen")
    @Column(name = "clientid")
    private Long clientId;

    @Column(name = "name")
    private String name;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "addressid")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Address address;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "client", orphanRemoval = true)
    @Fetch(FetchMode.JOIN)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Phone> phones;

    public Client(String name) {
        this.name = name;
    }

    public Client(Long clientId, String name) {
        this.clientId = clientId;
        this.name = name;
    }

    public Client(Long clientId, String name, Address address, List<Phone> phones) {
        this.clientId = clientId;
        this.name = name;
        this.address = address;
        this.phones = phones;
        setPhoneClient();
    }

    private void setPhoneClient() {
        if (this.phones != null) {
            for (Phone phone : this.phones) {
                phone.setClient(this);
            }
        }
    }

    @Override
    @SuppressWarnings({"java:S2975", "java:S1182"})
    public Client clone() {
        Client clientClone =  new Client(this.clientId, this.name, new Address(this.address.getAddressId(), this.address.getStreet()), new ArrayList<>());
        List<Phone> phoneList = new ArrayList<>();
        for (Phone phone : this.phones) {
            phoneList.add(new Phone(phone.getPhoneId(), phone.getNumber(), clientClone));
        }
        clientClone.setPhones(phoneList);
        return clientClone;
    }
}
