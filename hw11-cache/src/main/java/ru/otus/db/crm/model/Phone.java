package ru.otus.db.crm.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "phone")
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "phoneid")
    private Long phoneId;

    @Column(name = "number")
    private String number;

    @ManyToOne
    @JoinColumn(name = "clientid")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    Client client;

    public Phone(Long phoneId, String number) {
        this.phoneId = phoneId;
        this.number = number;
    }
}
