package es.udc.fic.csi2122.baserest.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity

public class Client {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String name;

    @Column
    private String surname;

    @Column
    private String dni;

    @Column
    private int age;

    @Column
    private String address;

    @Column
    private String paymentMethod;

    public Client() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {this.surname = surname;}

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {this.dni = dni;}

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAdress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPaymentMethod() {return paymentMethod;}

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Client(String name, String surname, int age, String dni, String address, String paymentMethod) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.dni = dni;
        this.address = address;
        this.paymentMethod = paymentMethod;
    }


    @Override
    public String toString() {
        return "Client [id=" + id + ", name=" + name + ", surname=" + surname + ", address=" + address + ", age=" + age
                + ", dni=" + dni + ", paymentMethod=" + paymentMethod + "]";
    }
}
