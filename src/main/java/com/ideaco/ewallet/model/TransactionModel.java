package com.ideaco.ewallet.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "tab_transaction")
public class TransactionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private int transactionId;
    @Column(name = "transaction_amount")
    private int transactionAmount;
    @Column(name = "transaction_time")
    private LocalDateTime transactionTime;
    @Column(name = "transaction_status")
    private String transactionStatus;
    @Column(name = "transaction_sender")
    private int transactionSender;
    @Column(name = "transaction_receiver")
    private int transactionReceiver;
    @Column(name = "transaction_type")
    private String transactionType;
}
