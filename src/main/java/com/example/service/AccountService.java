package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    public Account register(Account account) {
        //check if account already exists
        Optional<Account> accountOptional = accountRepository.findByUsername(account.getUsername());
        if(accountOptional.isPresent()) {
            return null;
        } else {
            Account accountCreated = accountRepository.save(account);
            return accountCreated;
        }   
    } 
}
