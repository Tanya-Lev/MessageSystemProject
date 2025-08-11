package com.example.messagesystemproject.repository;

import com.example.messagesystemproject.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

//todo  везде ObjectId (в сущности и репозитории) MongoRepository<Message, ObjectId>
public interface MessageRepository extends MongoRepository<Message, String> {
    // Тут мы используем Pageable для управления номером страницы и размером, а Page<Message> вернёт уже нужный кусок с сортировкой
    Page<Message> findByDialogId(String dialogId, Pageable pageable);

    // Вернёт самое последнее сообщение в диалоге (по дате)
    Optional<Message> findFirstByDialogIdOrderByDateTimeDesc(String dialogId);
}
