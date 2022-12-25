package com.backend.todolist.backend.model.user;

import org.springframework.stereotype.Component;

@Component
public class UserConverter {
    public User inputToEntity(UserInput input) {
        User user = new User();
        user.setId(input.getId());
        user.setUsername(input.getUsername());
        user.setPassword(input.getPassword());
        return user;
    }

    public UserOutput entityToOutput(User user) {
    	UserOutput output = new UserOutput();
        output.setId(user.getId());
        output.setUsername(user.getUsername());
        output.setSubscriptionDate(user.getSubscriptionDate().toString());
    	return output;
    }

}
