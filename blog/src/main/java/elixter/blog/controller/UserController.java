package elixter.blog.controller;

import elixter.blog.domain.user.User;
import elixter.blog.dto.user.GetUserResponseDto;
import elixter.blog.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

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
    public ResponseEntity<GetUserResponseDto> GetUserByLoginIdHandler(
            @PathVariable String loginId
    ) {
        GetUserResponseDto result = new GetUserResponseDto();
        HttpStatus statusCode = HttpStatus.OK;

        log.debug(loginId);

        try {
            User foundUser = service.findUserByLoginId(loginId);
            result.Mapping(foundUser);
        } catch (NoSuchElementException e) {
            throw new UserNotFoundException("User not found with login id : " + loginId);
        }

        return new ResponseEntity<>(result, statusCode);
    }
}
