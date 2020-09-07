package com.velotio.demo1.domains;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Organization {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String name;

    public Organization(String name) {
        this.name = name;
    }

    public Organization() {

    }

    @Override
    public String toString() {
        return this.name;
    }
}
