package com.example.jpaexample.model;

import javax.persistence.*;

@Entity
@Table(name="Address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String house;
    private String street;
    private String city;

    @OneToOne
    private Employee empl;



    public int getId() {
        return id;
    }

    public Employee getEmpl() {
        return empl;
    }

    public void setEmpl(Employee empl) {
        this.empl = empl;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Address(int id,String house, String street, String city) {
        this.id=id;
        this.house = house;
        this.street = street;
        this.city = city;
    }
    public  Address(){
        super();
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
