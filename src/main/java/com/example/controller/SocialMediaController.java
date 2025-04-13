package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

import java.util.Optional;
import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

@RestController
public class SocialMediaController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageService messageService;

    // GET

    @GetMapping("messages")
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    @GetMapping("messages/{messageId}")
    public Message getMessageById(@PathVariable int messageId) {
        return messageService.getMessageById(messageId);
    }

    @GetMapping("accounts/{accountId}/messages")
    public List<Message> getAllMessagesByAccountId(@PathVariable int accountId) {
        return messageService.getAllMessageByPostedBy(accountId);
    }

    // POST

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody Account account) {
        if(account != null && account.getUsername() != null && !account.getUsername().isBlank() && account.getPassword().length() >= 4) {
            Account newAccount = accountService.register(account);
            if(newAccount != null) 
                return ResponseEntity.ok().body(newAccount);
            else
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.badRequest().body("Invalid registration");
    }

    @PostMapping("login")
    public ResponseEntity<Account> login(@RequestBody Account account) {
        Optional<Account> accountOptional = accountService.login(account);
        if(accountOptional.isPresent()) {
            return ResponseEntity.ok().body(accountOptional.get());
        } else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

     @PostMapping("messages")
     public ResponseEntity<?> createMessage(@RequestBody Message message) {
        Message createdMessage = messageService.create(message);
        if(createdMessage != null) {
            return ResponseEntity.ok().body(createdMessage);
        }
        return ResponseEntity.badRequest().body("Invalid message");
     }


    // PATCH

    @PatchMapping("messages/{messageId}")
    public ResponseEntity<?> updateMessageById(@PathVariable int messageId, @RequestBody Message message) {
        int recordUpdated = messageService.updateMessageById(messageId, message);
        if(recordUpdated == 1) {
            return ResponseEntity.ok().body(1);
        }
        return ResponseEntity.badRequest().body("Invalid message");
    }

    // DELETE

    @DeleteMapping("messages/{messageId}")
    public ResponseEntity<?> deleteMessageById(@PathVariable int messageId) {
        int recordDeleted = messageService.deleteMessageById(messageId);
        if(recordDeleted == 1) {
            return ResponseEntity.ok().body(1);
        }
        return ResponseEntity.ok().build();
    }
}
