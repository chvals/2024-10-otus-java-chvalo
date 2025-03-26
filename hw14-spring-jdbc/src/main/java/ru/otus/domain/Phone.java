package ru.otus.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "phone")
public record Phone(
        @Id @Column("phoneid") Long phoneId, @Column("clientid") Long clientid, @Column("number") String number) {}
