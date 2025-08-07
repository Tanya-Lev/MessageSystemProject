package com.example.messagesystemproject.repository;

import com.example.messagesystemproject.entity.Dialog;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DialogRepository extends MongoRepository<Dialog, String> {
    //Ищет документы, у которых поле usersIdList содержит указанный userId.
    List<Dialog> findAllByUsersIdListContaining(String userId);


}
