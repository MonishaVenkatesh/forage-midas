package com.jpmc.midascore.kafka;

import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.service.TransactionService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TransactionListener {

    private final TransactionService transactionService;

    public TransactionListener(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @KafkaListener(topics = "${general.kafka-topic}")
    public void listen(Transaction transaction) {

        System.out.println("Received Transaction: " + transaction);

        transactionService.processTransaction(
                transaction.getSenderId(),
                transaction.getRecipientId(),
                transaction.getAmount()
        );
    }
}


