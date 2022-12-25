package com.backend.todolist.backend.controller;

import static com.backend.todolist.backend.utils.Constants.END;
import static com.backend.todolist.backend.utils.Constants.EXCEPTION;
import static com.backend.todolist.backend.utils.Constants.START;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.backend.todolist.backend.model.item.Item;
import com.backend.todolist.backend.model.item.ItemConverter;
import com.backend.todolist.backend.model.item.ItemInput;
import com.backend.todolist.backend.model.item.ItemOutput;
import com.backend.todolist.backend.model.user.User;
import com.backend.todolist.backend.service.AuthenticationService;
import com.backend.todolist.backend.service.ItemService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@CrossOrigin("*")
@RequestMapping(ParentController.BASE_URI + "/item")
public class ItemController extends ParentController{

	@Autowired
	ItemService itemService;
	@Autowired
	AuthenticationService authService;
	@Autowired
	ItemConverter itemConverter;
	


	@PostMapping("/saveItem")
	public ResponseEntity<ItemOutput> saveUsersItems(@RequestBody ItemInput input, @RequestHeader("Authorization") String token) {
		log.info(START + " saveUsersItems {} ", this.getClass().getSimpleName());
		try {
			User user = authService.getUserByToken(token);
			input.setUser(user);
			Item item = itemService.saveUsersItems(itemConverter.inputToEntity(input));
			ItemOutput output = itemConverter.entityToOutput(item);
			log.info(END + " saveUsersItems {} ", this.getClass().getSimpleName());
			return ResponseEntity.ok(output);
		} catch (Exception e) {
			log.error(EXCEPTION, e, this.getClass().getSimpleName());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

    @GetMapping("/getUsersItem")
    public ResponseEntity<List<ItemOutput>> getUsersItem(@RequestHeader("Authorization") String token) {
        log.info(START + " getUsersItem {} ", this.getClass().getSimpleName());
        try {
            User user = authService.getUserByToken(token);
            List<Item> list = itemService.getUsersItem(user);
            List<ItemOutput> output = new ArrayList<>();
            list.forEach(el -> output.add(itemConverter.entityToOutput(el)));
            log.info(END + " getUsersItem {} ", this.getClass().getSimpleName());
            return ResponseEntity.ok(output);
        } catch (Exception e) {
        	log.error(EXCEPTION, e, this.getClass().getSimpleName());
        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping("/nextStep")
    public ResponseEntity<ItemOutput> itemToNextStep(@RequestBody ItemInput input) {
    	log.info(START + " itemToNextStep {} with input {}", this.getClass().getSimpleName(), input.toString());
    	try {
    		Item item = itemService.itemToNextStep(itemConverter.inputToEntity(input));
    		log.info(END + " itemToNextStep {} ", item.toString());
    		return ResponseEntity.ok(itemConverter.entityToOutput(item));
    	} catch(Exception e) {
    		log.error(EXCEPTION, e, this.getClass().getSimpleName());
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    	}
    }
    
    @PostMapping("/previousStep")
    public ResponseEntity<ItemOutput> itemToPreviousStep(@RequestBody ItemInput input) {
    	log.info(START + " itemToPreviousStep {} with input {}", this.getClass().getSimpleName(), input.toString());
    	try {
    		Item item = itemService.itemToPreviousStep(itemConverter.inputToEntity(input));
    		log.info(END + " itemToPreviousStep {} ", item.toString());
    		return ResponseEntity.ok(itemConverter.entityToOutput(item));
    	} catch(Exception e) {
    		log.error(EXCEPTION, e, this.getClass().getSimpleName());
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    	}
    }


    @PutMapping("/editText")
    public ResponseEntity<ItemOutput> editItemText(@RequestBody ItemInput input) {
    	log.info(START + " editItemText {} ", this.getClass().getSimpleName());
    	try {
    		Item item = itemService.editItemText(itemConverter.inputToEntity(input));
    		return ResponseEntity.ok(itemConverter.entityToOutput(item));
    	} catch(Exception e) {
    		log.error(EXCEPTION, e, this.getClass().getSimpleName());
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    	}
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Boolean> deleteItem(@PathVariable String itemId) {
    	log.info(START + " deleteItem {} ", this.getClass().getSimpleName());
    	try {
    		boolean deletedItem = itemService.deleteItem(itemId);
    		return ResponseEntity.ok(deletedItem);
    	} catch(Exception e) {
    		log.error(EXCEPTION, e, this.getClass().getSimpleName());
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    	}
    }
}
