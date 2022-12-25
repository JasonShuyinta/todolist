package com.backend.todolist.backend.model.item;

import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component
public class ItemConverter {

	public Item inputToEntity(ItemInput input) {
		Item item = new Item();
		item.setId(input.getId());
		item.setText(input.getText());
		item.setState(input.getState());
		item.setUser(input.getUser());
		return item;
	}

	public ItemOutput entityToOutput(Item item) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		ItemOutput output = new ItemOutput();
		output.setId(item.getId());
		output.setText(item.getText());
		output.setState(item.getState());
		output.setUser(item.getUser());
		output.setCreationTime(item.getCreationTime().format(format));
		output.setLastUpdateTime(item.getLastUpdateTime().format(format));
		return output;
	}

}
