package com.debrief2.pulsa.member.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;


@SpringBootTest
public class UserTest {

    private static final long ID = 1;
    private static final String NAME = "Abigail";
    private static final String EMAIL = "abigail@dana.id";
    private static final String PHONE = "082272068810";
    private static final String PIN = "123456";
    private static final LocalDateTime CREATED_AT = LocalDateTime.now();
    private static final LocalDateTime NEW_CREATED_AT = LocalDateTime.now().plusHours(1);
    private static final LocalDateTime UPDATED_AT = LocalDateTime.now();
    private static final LocalDateTime NEW_UPDATED_AT = LocalDateTime.now().plusHours(1);

    @Test
    public void constructorTest() {
        User user = new User(ID, NAME, EMAIL, PHONE, PIN, CREATED_AT, UPDATED_AT);
        assert user.getName().equals(NAME);
        assert user.getEmail().equals(EMAIL);
        assert user.getUsername().equals(PHONE);
        assert user.getPin().equals(PIN);
        assert user.getCreatedAt().equals(CREATED_AT);
        assert user.getUpdatedAt().equals(UPDATED_AT);
    }

    @Test
    public void constructorEmptyTest() {
        new User();
    }

    @Test
    public void setterId() {
        User user = new User(ID, NAME, EMAIL, PHONE, PIN, CREATED_AT, UPDATED_AT);

        user.setId(2);
        user.getId();
    }

    @Test
    public void setterName() {
        User user = new User(ID, NAME, EMAIL, PHONE, PIN, CREATED_AT, UPDATED_AT);

        user.setName("Abigail Anderson");
        assert user.getName().equals("Abigail Anderson");
    }

    @Test
    public void setterEmail() {
        User user = new User(ID, NAME, EMAIL, PHONE, PIN, CREATED_AT, UPDATED_AT);

        user.setEmail("anderson@dana.id");
        assert user.getEmail().equals("anderson@dana.id");
    }

    @Test
    public void setterUsername() {
        User user = new User(ID, NAME, EMAIL, PHONE, PIN, CREATED_AT, UPDATED_AT);

        user.setUsername("08227206820");
        assert user.getUsername().equals("08227206820");
    }

    @Test
    public void setterPin() {
        User user = new User(ID, NAME, EMAIL, PHONE, PIN, CREATED_AT, UPDATED_AT);

        user.setPin("787890");
        assert user.getPin().equals("787890");
    }

    @Test
    public void setterCreatedAt() {
        User user = new User(ID, NAME, EMAIL, PHONE, PIN, CREATED_AT, UPDATED_AT);

        user.setCreatedAt(NEW_CREATED_AT);
        assert user.getCreatedAt().equals(NEW_CREATED_AT);
    }

    @Test
    public void setterUpdatedAt() {
        User user = new User(ID, NAME, EMAIL, PHONE, PIN, CREATED_AT, UPDATED_AT);

        user.setUpdatedAt(NEW_UPDATED_AT);
        assert user.getUpdatedAt().equals(NEW_UPDATED_AT);
    }

}
