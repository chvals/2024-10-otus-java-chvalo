package ru.otus.db.crm.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.List;

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
        Address addressClone = null;
        if (address != null) {
            addressClone = new Address(this.address.getAddressId(), this.address.getStreet());
        }
        Client clientClone =  new Client(this.clientId, this.name, addressClone, new ArrayList<>());
        if (phones != null) {
            List<Phone> phoneList = new ArrayList<>();
            for (Phone phone : this.phones) {
                phoneList.add(new Phone(phone.getPhoneId(), phone.getNumber(), clientClone));
            }
            clientClone.setPhones(phoneList);
        }
        return clientClone;
    }
}
