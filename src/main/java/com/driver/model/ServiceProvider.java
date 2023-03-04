package com.driver.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "serviceProvider")
public class ServiceProvider
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    //Connect Admin(Parent) to ServiceProvider(Child) --> One : Many
    @ManyToOne
    @JoinColumn
    private Admin admin;

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }


    //Connect ServiceProvider(Parent) to Connection(Child) --> One : Many
    @OneToMany(mappedBy = "serviceProvider", cascade = CascadeType.ALL)
    private List<Connection> connectionList;

    public List<Connection> getConnectionList() {
        return connectionList;
    }

    public void setConnectionList(List<Connection> connectionList) {
        this.connectionList = connectionList;
    }



    //Connect ServiceProvider(Parent) to Country(Child) --> One : Many
    @OneToMany(mappedBy = "serviceProvider", cascade = CascadeType.ALL)
    private List<Country> countryList;

    public List<Country> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<Country> countryList) {
        this.countryList = countryList;
    }


    //Connect User(Parent) to ServiceProvider(Child) --> Many : Many
    @ManyToMany
    @JoinTable(name = "service_user", joinColumns = @JoinColumn(name = "service_id", referencedColumnName = "id")
    , inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private List<User> users;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public ServiceProvider() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
