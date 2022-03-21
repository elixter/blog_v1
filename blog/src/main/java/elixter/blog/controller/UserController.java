package elixter.blog.controller;

import elixter.blog.domain.user.User;
import elixter.blog.dto.user.GetUserResponseDto;
import elixter.blog.message.Message;
import elixter.blog.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

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

        try {
            User foundUser = service.findUserByLoginId(loginId);
            result.Mapping(foundUser);
        } catch (NoSuchElementException e) {
            msg = "User " + loginId + " is not found.";
            statusCode = HttpStatus.NOT_FOUND;
            result = null;
        }

        Message responseMsg = new Message(statusCode, msg, result);

        return new ResponseEntity<>(responseMsg, statusCode);
    }
}
