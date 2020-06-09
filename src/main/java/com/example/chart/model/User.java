package com.example.chart.model;

import lombok.Data;

import javax.persistence.*;

@Table(name = "users")
@Entity
@Data
public class User {
    @Id
    @SequenceGenerator(name = "userSeq",sequenceName = "userseq",initialValue = 1,allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "userSeq")
    private Long id;
    private String username;
    private String password;

    @Column(columnDefinition = "varchar2(10)")
    private String role;

    private boolean enabled;

}