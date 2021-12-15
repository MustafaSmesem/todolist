package com.comodo.todolistspring.repository;

import com.comodo.todolistspring.document.Todo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends MongoRepository<Todo, String> {
    List<Todo> findAllByUserId(String id);
}
