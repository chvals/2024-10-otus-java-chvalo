package ru.otus.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "address")
public record Address(
        @Id @Column("addressid") Long addressId, @Column("clientid") Long clientid, @Column("street") String street) {}
