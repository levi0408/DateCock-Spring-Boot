package com.example.DateCock.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "DATECOCKGUEST")
@SequenceGenerator(
        name="guest",
        sequenceName = "DATECOCKGUEST_SEQ",
        initialValue = 1000,
        allocationSize = 1
)
public class GuestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "guest")
    @Column
    Long id;

    @Column
    String name;

    @Column
    String memo;

    @Column
    Timestamp todays;

}
