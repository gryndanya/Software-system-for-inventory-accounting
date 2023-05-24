package com.example.demo.model;

import javax.persistence.*;

@Entity
@Table(name = "items_event")
public class ItemsEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "quantity_on_event", nullable = false)
    private int quantity_on_event;

    @Column(name = "name_event", nullable = false)
    private String name_event;

    @Column(name = "person", nullable = false)
    private String person;

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity_on_event() {
        return quantity_on_event;
    }

    public void setQuantity_on_event(int quantity_on_event) {
        this.quantity_on_event = quantity_on_event;
    }

    public String getName_event() {
        return name_event;
    }

    public void setName_event(String name_event) {
        this.name_event = name_event;
    }
}
