package com.backend.todolist.backend;

import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration(classes = {CoreTestConfiguration.class})
public abstract class BackendApplicationTests {
	
    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected WebApplicationContext wac;

    public MockMvc mockMvc;
    
    public static final String ACCESS_TOKEN = "todolistaccesstoken";
    public static final String AUTHORIZATION = "Authorization";
    public static final String URL_PREFIX = "/todolist";
    public static final String AUTH_ENDPOINT = "/auth";
    public static final String ITEM_ENDPOINT = "/item";
	public static final String USER_ID = "6346b0e20b271a31ca488e50";
	public static final String USERNAME = "tm5gaTgH9ND1";
	public static final String ITEM_ID = "634aa05f94a18e42356e84ad";
	
	@BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }
	
    public String getRandomString() {
        String asciiUpperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String asciiLowerCase = asciiUpperCase.toLowerCase();
        String digits = "1234567890";
        String asciiChars = asciiUpperCase + asciiLowerCase + digits;
        return generateRandomString(asciiChars);
    }

    private static String generateRandomString(String seedChars) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        Random rand = new Random();
        while (i < 12) {
            sb.append(seedChars.charAt(rand.nextInt(seedChars.length())));
            i++;
        }
        return sb.toString();
    }
}
