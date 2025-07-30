package com.example.messagesystemproject.repository;

import com.example.messagesystemproject.entity.Dialog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DialogRepository extends MongoRepository<Dialog,String> {
}
