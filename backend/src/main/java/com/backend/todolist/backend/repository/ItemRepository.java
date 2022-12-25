package com.backend.todolist.backend.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.backend.todolist.backend.model.item.Item;
import com.backend.todolist.backend.model.user.User;

public interface ItemRepository extends MongoRepository<Item, String> {

	List<Item> findItemByUser(User user);
}
