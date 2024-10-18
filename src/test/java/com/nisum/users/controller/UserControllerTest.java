package com.nisum.users.controller;

import com.nisum.users.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ContextConfiguration(classes = {UserController.class})
@WebMvcTest()
@AutoConfigureMockMvc(addFilters = false)

class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private String userRq;

    @BeforeEach
    void setUp() {
        userRq = "{\n" +
                "    \"name\": \"Juan nievesk\",\n" +
                "    \"email\": \"juan4509@gmail.com\",\n" +
                "    \"password\": \"Hunter26*\",\n" +
                "    \"phones\": [\n" +
                "        {\n" +
                "            \"number\": \"567\",\n" +
                "            \"cityCode\": \"2\",\n" +
                "            \"countryCode\": \"57\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"number\": \"777\",\n" +
                "            \"cityCode\": \"2\",\n" +
                "            \"countryCode\": \"57\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }


    @Test
    void createUserTest () throws Exception {

        MvcResult result = mockMvc.perform(
                post("/api/user")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRq))
                .andReturn();

        assertEquals(HttpStatus.CREATED.value(),result.getResponse().getStatus());
    }

    @Test
    void getUserTest () throws Exception {

        MvcResult result = mockMvc.perform(
                        get("/api/user")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                )
                .andReturn();

        assertEquals(HttpStatus.OK.value(),result.getResponse().getStatus());
    }

    @Test
    void getUserIdTest () throws Exception {

        UUID id = UUID.randomUUID();

        MvcResult result = mockMvc.perform(
                        get("/api/user/" + id)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn();

        assertEquals(HttpStatus.OK.value(),result.getResponse().getStatus());
    }

    @Test
    void updateUserTest () throws Exception {

        UUID id = UUID.randomUUID();

        MvcResult result = mockMvc.perform(
                        put("/api/user/" + id)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userRq))
                .andReturn();

        assertEquals(HttpStatus.OK.value(),result.getResponse().getStatus());
    }

    @Test
    void disableUserTest () throws Exception {

        UUID id = UUID.randomUUID();

        MvcResult result = mockMvc.perform(
                        delete("/api/user/disable/" + id)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn();

        assertEquals(HttpStatus.NO_CONTENT.value(),result.getResponse().getStatus());
    }

    @Test
    void enableUserTest () throws Exception {

        UUID id = UUID.randomUUID();

        MvcResult result = mockMvc.perform(
                        delete("/api/user/enable/" + id)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn();

        assertEquals(HttpStatus.NO_CONTENT.value(),result.getResponse().getStatus());
    }
}
