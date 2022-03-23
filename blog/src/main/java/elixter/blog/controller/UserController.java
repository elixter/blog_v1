package elixter.blog.controller;

import elixter.blog.Constants;
import elixter.blog.domain.user.User;
import elixter.blog.dto.user.CreateUserRequestDto;
import elixter.blog.dto.user.GetUserResponseDto;
import elixter.blog.dto.user.UpdateUserRequestDto;
import elixter.blog.message.Message;
import elixter.blog.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

// TODO: Message 구조체를 하나 선언해서 ResponseEntity<Message> 형식으로 응답 내려주기.

@Slf4j
@RestController
@RequestMapping("/api/users")
@Transactional
public class UserController {
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/{loginId}")
    public ResponseEntity<Message> GetUserByLoginIdHandler(
            @PathVariable String loginId
    ) {
        GetUserResponseDto result = new GetUserResponseDto();
        String msg = "";
        HttpStatus statusCode = HttpStatus.OK;

        log.debug(loginId);

        List<User> foundUser = service.findUser("loginId", loginId);
        if (foundUser.isEmpty()) {
            msg = "User " + loginId + " is not found.";
            statusCode = HttpStatus.NOT_FOUND;
            result = null;
        } else {
            result.Mapping(foundUser.get(0));
        }

        Message responseMsg = new Message(statusCode, msg, result);

        return new ResponseEntity<>(responseMsg, statusCode);
    }

    @PostMapping
    public ResponseEntity<Message> PostCreateUserHandler(
            @Valid @RequestBody CreateUserRequestDto createUserRequestBody
    ) {
        String msg = "";
        Long result;
        HttpStatus statusCode = HttpStatus.CREATED;

        User createdUser = createUserRequestBody.mapping();
        result = service.createUser(createdUser);

        if (result.equals(Constants.userLoginIdAlreadyExist)) {
            statusCode = HttpStatus.CONFLICT;
            msg = "user id ( " + createdUser.getLoginId() + " ) is already existed";
            result = null;
        } else if (result.equals(Constants.userEmailAlreadyExist)) {
            statusCode = HttpStatus.CONFLICT;
            msg = "entry is duplicated";
            result = null;
        }

        Message responseMsg = new Message(statusCode, msg, result);

        return new ResponseEntity<>(responseMsg, statusCode);
    }

    @PutMapping
    public ResponseEntity<Message> PutUpdateUserHandler(
            @Valid @RequestBody UpdateUserRequestDto updateUserRequestBody
    ) {
        String msg = "";
        Long result;
        HttpStatus statusCode = HttpStatus.OK;

        User updatedUser = updateUserRequestBody.mapping();

        result = service.updateUser(updatedUser);
        if (result.equals(Constants.recordNotExist)) {
            log.debug("user id {} not exist", updatedUser.getId());
            msg = "user not exist";
            statusCode = HttpStatus.NOT_FOUND;
            result = null;
        }

        Message responseMsg = new Message(statusCode, msg, result);

        return new ResponseEntity<>(responseMsg, statusCode);
    }
}
