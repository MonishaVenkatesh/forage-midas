package com.jpmc.midascore.entity;

import jakarta.persistence.*;

@Entity
public class TransactionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserRecord sender;

    @ManyToOne
    private UserRecord recipient;

    private double amount;

    public TransactionRecord() {}

    public TransactionRecord(UserRecord sender, UserRecord recipient, double amount) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
    }

    public Long getId() { return id; }

    public UserRecord getSender() { return sender; }
    public void setSender(UserRecord sender) {   
        this.sender = sender;
    }

    public UserRecord getRecipient() { return recipient; }
    public void setRecipient(UserRecord recipient) {  
        this.recipient = recipient;
    }

    public double getAmount() { return amount; }
    public void setAmount(double amount) {
        this.amount = amount;
    }
}
