package com.backend.todolist.backend.service;

import static com.backend.todolist.backend.utils.Constants.END;
import static com.backend.todolist.backend.utils.Constants.START;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.backend.todolist.backend.model.item.Item;
import com.backend.todolist.backend.model.user.User;
import com.backend.todolist.backend.repository.ItemRepository;
import com.backend.todolist.backend.utils.StateEnum;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ItemService {

	private final ItemRepository itemRepository;

	public ItemService(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	public Item saveUsersItems(Item item) {
		log.info(START + " saveUsersItemsService {} ", this.getClass().getSimpleName());
		item.setState(StateEnum.TODO.name());
		item.setCreationTime(LocalDateTime.now());
		item.setLastUpdateTime(LocalDateTime.now());
		return itemRepository.save(item);
	}

	public List<Item> getUsersItem(User user) {
		log.info(START + " getUsersItem {} ", this.getClass().getSimpleName());
		List<Item> list = itemRepository.findItemByUser(user);
		log.info(END + " getUsersItem {} ", this.getClass().getSimpleName());
		return list;
	}

	public Item itemToNextStep(Item input) throws Exception {
		log.info(START + " itemToNextStep {} ", this.getClass().getSimpleName());
		Optional<Item> op = itemRepository.findById(input.getId());
		if (!op.isEmpty()) {
			Item item = op.get();
			StateEnum itemState = StateEnum.valueOf(item.getState());
			switch (itemState) {
			case TODO:
				item.setState(StateEnum.IN_PROGRESS.name());
				break;
			case IN_PROGRESS:
				item.setState(StateEnum.DONE.name());
				break;
			case DONE:
				break;
			}
			item.setLastUpdateTime(LocalDateTime.now());
			log.info(END + " itemToNextStep {} ", this.getClass().getSimpleName());
			return itemRepository.save(item);
		} else
			throw new Exception("Not found");
	}

	public Item itemToPreviousStep(Item input) throws Exception {
		log.info(START + " itemToPreviousStep {} ", this.getClass().getSimpleName());
		Optional<Item> op = itemRepository.findById(input.getId());
		if (!op.isEmpty()) {
			Item item = op.get();
			StateEnum itemState = StateEnum.valueOf(item.getState());
			switch (itemState) {
			case TODO:
				break;
			case IN_PROGRESS:
				item.setState(StateEnum.TODO.name());
				break;
			case DONE:
				item.setState(StateEnum.IN_PROGRESS.name());
				break;
			}
			item.setLastUpdateTime(LocalDateTime.now());
			log.info(END + " itemToPreviousStep {} ", this.getClass().getSimpleName());
			return itemRepository.save(item);
		} else
			throw new Exception("Not found");
	}

	public Item editItemText(Item input) throws Exception {
		log.info(START + " editItemText {} ", this.getClass().getSimpleName());
		Optional<Item> op = itemRepository.findById(input.getId());
		if (!op.isEmpty()) {
			Item item = op.get();
			item.setText(input.getText());
			log.info(END + " editItemText {} ", this.getClass().getSimpleName());
			return itemRepository.save(item);
		} else
			throw new Exception("Not found");

	}

	public boolean deleteItem(String itemId) throws Exception {
		log.info(START + " deleteItem {} ", this.getClass().getSimpleName());
		try {
			boolean deleted = false;
			itemRepository.deleteById(itemId);
			deleted = true;
			return deleted;
		} catch (Exception e) {
			throw new Exception("Not found");
		}
	}
}
