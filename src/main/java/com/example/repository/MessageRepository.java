package com.example.repository;

import com.example.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Extends JpaRepository<Message, Integer> (Message.messageId is an Integer).
 *
 * findByPostedBy is a derived query used by MessageService.getMessagesByAccountId() for User Story 8:
 * "retrieve all messages written by a particular user" (GET /accounts/{accountId}/messages).
 */
public interface MessageRepository extends JpaRepository<Message, Integer> {

    List<Message> findByPostedBy(Integer postedBy);

}