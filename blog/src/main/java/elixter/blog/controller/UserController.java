package elixter.blog.controller;

import elixter.blog.constants.RecordStatus;
import elixter.blog.constants.RecordErrorConstants;
import elixter.blog.domain.user.User;
import elixter.blog.dto.user.CreateUserRequestDto;
import elixter.blog.dto.user.GetUserResponseDto;
import elixter.blog.dto.user.UpdateUserRequestDto;
import elixter.blog.exception.user.UserNotFoundException;
import elixter.blog.message.Message;
import elixter.blog.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/{loginId}")
    public ResponseEntity<Object> GetUserByLoginIdHandler(
            @PathVariable String loginId
    ) {
        log.info("finding user with login id = [{}]", loginId);

        User foundUser = service.findUser("loginId", loginId).get(0);
        if (foundUser.isEmpty()) {
            log.info("login id [{}] user is not found", loginId);
            Map<String, String> cause = new HashMap<>();
            cause.put("login_id", loginId);
            throw new UserNotFoundException(cause);
        }

        return ResponseEntity.ok(new GetUserResponseDto(foundUser));
    }

    @PostMapping
    public ResponseEntity<Object> PostCreateUserHandler(
            @Valid @RequestBody CreateUserRequestDto createUserRequestBody
    ) {
        service.createUser(createUserRequestBody.mapping());

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Object> PutUpdateUserHandler(
            @Valid @RequestBody UpdateUserRequestDto updateUserRequestBody
    ) {

        User updateUser = service.updateUser(updateUserRequestBody);
        if (updateUser.isEmpty()) {
            log.debug("User update failed : user id {} not exist", updateUserRequestBody.getId());
            Map<String, String> cause = new HashMap<>();
            cause.put("email", updateUserRequestBody.getEmail());
            throw new UserNotFoundException(cause);
        }

        return ResponseEntity.noContent().build();
    }
}
