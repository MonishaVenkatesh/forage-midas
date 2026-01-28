package com.jpmc.midascore.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class TransactionService {

    private final TransactionRecordRepository transactionRepo;
    private final UserRepository userRepo;
    private final RestTemplate restTemplate; 

    public TransactionService(TransactionRecordRepository transactionRepo, UserRepository userRepo, RestTemplate restTemplate) {
        this.transactionRepo = transactionRepo;
        this.userRepo = userRepo;
        this.restTemplate = restTemplate;
    }

    @Transactional
    public boolean processTransaction(Long senderId, Long recipientId, double amount) {

    	UserRecord sender = userRepo.findById(senderId).orElse(null);
    	UserRecord recipient = userRepo.findById(recipientId).orElse(null);


        if (sender == null || recipient == null) {
            return false;
        }

        if (sender.getBalance() < amount) {
            return false;
        }
        
        Transaction transaction = new Transaction();
        transaction.setSenderId(senderId);
        transaction.setRecipientId(recipientId);
        transaction.setAmount((float) amount);
        Incentive incentive = restTemplate.postForObject("http://localhost:8080/incentive", transaction, Incentive.class);

        double incentiveAmount = incentive != null ? incentive.getAmount() : 0;
        
        sender.setBalance(sender.getBalance() - (float) amount);
        recipient.setBalance(recipient.getBalance() + (float) amount);

        userRepo.save(sender);
        userRepo.save(recipient);

        TransactionRecord record = new TransactionRecord(sender, recipient, amount, incentiveAmount);
        transactionRepo.save(record);
   
        return true;
    }
}
