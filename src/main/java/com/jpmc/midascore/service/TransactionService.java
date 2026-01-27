package com.jpmc.midascore.service;

import org.springframework.stereotype.Service;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class TransactionService {

    private final TransactionRecordRepository transactionRepo;
    private final UserRepository userRepo;

    public TransactionService(TransactionRecordRepository transactionRepo, UserRepository userRepo) {
        this.transactionRepo = transactionRepo;
        this.userRepo = userRepo;
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

        sender.setBalance(sender.getBalance() - (float) amount);
        recipient.setBalance(recipient.getBalance() + (float) amount);

        userRepo.save(sender);
        userRepo.save(recipient);
        if(sender.getName().equals("waldorf") || recipient.getName().equals("waldorf")) {
            System.out.println("Waldorf balance now: " + sender.getBalance() + " / " + recipient.getBalance());
        }

        TransactionRecord record = new TransactionRecord(sender, recipient, amount);
        transactionRepo.save(record);
        System.out.println("Processed transaction: sender=" + sender.getName() + 
                ", recipient=" + recipient.getName() + 
                ", amount=" + amount);

        return true;
    }
}
