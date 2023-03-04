package com.driver.model;


import javax.persistence.*;

@Entity
@Table(name = "connection")
public class Connection
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //Connect ServiceProvider(Parent) to Connection(Child) --> One : Many
    @ManyToOne
    @JoinColumn
    private ServiceProvider serviceProvider;

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }


    //Connect User(Parent) to Connection(Child) --> One : Many
    @ManyToOne
    @JoinColumn
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public Connection() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
