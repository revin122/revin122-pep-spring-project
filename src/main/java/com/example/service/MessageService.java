package com.example.service;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    
    @Autowired
    MessageRepository messageRepository;

    @Autowired
    AccountService accountService;

    public Message create(Message message) {
        if(message != null && !message.getMessageText().isBlank() && message.getMessageText().length() <= 255) {
            Optional<Account> userAccount = accountService.findById(message.getPostedBy());
            if(userAccount.isPresent()) {
                return messageRepository.save(message);
            }
        }
        return null;
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageById(Integer id) {
        Optional<Message> message = messageRepository.findById(id);
        if(message.isPresent())
            return message.get();
        return null;
    }

    public int deleteMessageById(Integer id) {
        Optional<Message> message = messageRepository.findById(id);
        if(message.isPresent()) {
            messageRepository.deleteById(id);
            return 1;
        }
        return 0;
    }

    public int updateMessageById(Integer id, Message message) {
        if(message != null && message.getMessageText() != null && !message.getMessageText().isBlank() && message.getMessageText().length() <= 255) {
            Optional<Message> messageOptional = messageRepository.findById(id);
            if(messageOptional.isPresent()) {
                Message messageToUpdate = messageOptional.get();
                
                //already checked so no need to recheck
                messageToUpdate.setMessageText(message.getMessageText());

                if(message.getPostedBy() != null) {
                    messageToUpdate.setPostedBy(message.getPostedBy());
                }
                if(message.getTimePostedEpoch() != null) {
                    messageToUpdate.setTimePostedEpoch(message.getTimePostedEpoch());
                }
                messageRepository.save(messageToUpdate);
                return 1;
            }
        }
        return 0;
    }

    public List<Message> getAllMessageByPostedBy(Integer postedBy) {
        Optional<List<Message>> messageListOptional = messageRepository.findByPostedBy(postedBy);
        if(messageListOptional.isPresent())
            return messageListOptional.get();
        else
            return null;
    }
}
