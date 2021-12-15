package com.comodo.todolistspring.repository;

import com.comodo.todolistspring.document.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends MongoRepository<Group, String> {
    List<Group> findAllByUserId(String id);
}
