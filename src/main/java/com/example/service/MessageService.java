package com.example.service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class MessageService {

    MessageRepository messageRepository;
    AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * User Story 3 - Create message.
     * Validates text not blank, not over 255 chars, and that postedBy refers to a real Account.
     * Every failure here maps to the same 400 in the Controller, so a single RuntimeException type
     * is enough (IllegalArgumentException / NoSuchElementException both extend RuntimeException).
     */
    public Message createMessage(Message message) {
        if (message.getMessageText() == null || message.getMessageText().isBlank())
            throw new IllegalArgumentException("Message is either empty or invalid");

        if (message.getMessageText().length() > 255)
            throw new IllegalArgumentException("Message is too long");

        accountRepository.findById(message.getPostedBy())
                .orElseThrow(() -> new NoSuchElementException("Account not found"));

        return messageRepository.save(message);
    }

    /** User Story 4 - Get all messages. Empty list is a valid (non-error) result. */
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    /**
     * User Story 5 - Get message by id.
     * Returns Optional<Message> rather than throwing, since "not found" means 200 + empty body,
     * not an error - the Controller decides what to do with an empty Optional.
     */
    public Optional<Message> getMessageById(Integer messageId) {
        return messageRepository.findById(messageId);
    }

    /**
     * User Story 6 - Delete message by id.
     * DELETE must be idempotent, so "nothing to delete" is not an error - it just returns 0 instead
     * of throwing, letting the Controller return 200 with an empty body either way.
     */
    public int deleteMessageById(Integer messageId) {
        if (messageRepository.existsById(messageId)) {
            messageRepository.deleteById(messageId);
            return 1;
        }
        return 0;
    }

    /**
     * User Story 7 - Update message text by id.
     * Validates the id exists, and that the new text is non-blank and <= 255 chars. Any failure
     * throws (mapped to 400 in the Controller); success updates and saves the existing row and
     * returns 1 (one row updated), matching what the README expects in the response body.
     */
    public int updateMessageText(Integer messageId, String newMessageText) {
        if (newMessageText == null || newMessageText.isBlank())
            throw new IllegalArgumentException("Message is either empty or invalid");

        if (newMessageText.length() > 255)
            throw new IllegalArgumentException("Message is too long");

        if (messageRepository.existsById(messageId)) {
            Message existingMessage = messageRepository.findById(messageId).get();
            existingMessage.setMessageText(newMessageText);
            messageRepository.save(existingMessage);
            return 1;
        }

        throw new IllegalArgumentException("Message not found");
    }

    /** User Story 8 - Get all messages by account id. Empty list is a valid (non-error) result. */
    public List<Message> getMessagesByAccountId(Integer accountId) {
        return messageRepository.findByPostedBy(accountId);
    }
}