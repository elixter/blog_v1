package elixter.blog.controller;

import elixter.blog.constants.SessionConstants;
import elixter.blog.domain.user.User;
import elixter.blog.dto.user.*;
import elixter.blog.exception.auth.UnauthorizedException;
import elixter.blog.exception.user.EmailAlreadyUseException;
import elixter.blog.exception.user.UserAlreadyExistException;
import elixter.blog.exception.user.UserNotFoundException;
import elixter.blog.service.user.UserSearchType;
import elixter.blog.service.user.VerifyService;
import elixter.blog.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final VerifyService verifyService;

    @Autowired
    public UserController(UserService userService, VerifyService verifyService) {
        this.userService = userService;
        this.verifyService = verifyService;
    }

    @GetMapping
    public ResponseEntity<GetUserResponseDto> GetSessionUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new UnauthorizedException();
        }

        GetUserResponseDto result = userService.findSessionUser(session);
        if (result.isEmpty()) {
            Map<String, String> searchKey = new HashMap<>();
            searchKey.put("session", "");
            throw new UserNotFoundException(searchKey);
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{loginId}")
    public ResponseEntity<Object> GetUserByLoginIdHandler(
            @PathVariable String loginId
    ) {
        log.info("finding user with login id = [{}]", loginId);

        User foundUser = userService.findUser(UserSearchType.USER_SEARCH_TYPE_LOGIN_ID, loginId).get(0);
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
            HttpServletRequest request,
            @Valid @RequestBody CreateUserRequestDto createUserRequestBody
    ) {

        User newUser = createUserRequestBody.mapping();
        HttpSession session = request.getSession(false);
        if (session != null) {
            boolean emailVerified = (boolean) session.getAttribute(SessionConstants.EMAIL_VERIFY);
            if (emailVerified) {
                newUser.setEmailVerified(true);
                userService.createUser(newUser);
                session.removeAttribute(SessionConstants.EMAIL_VERIFY);
            }
        } else {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Object> PutUpdateUserHandler(
            @Valid @RequestBody UpdateUserRequestDto updateUserRequestBody
    ) {

        User updateUser = userService.updateUser(updateUserRequestBody);
        if (updateUser.isEmpty()) {
            log.debug("User update failed : user id {} not exist", updateUserRequestBody.getId());
            Map<String, String> cause = new HashMap<>();
            cause.put("email", updateUserRequestBody.getEmail());
            throw new UserNotFoundException(cause);
        }

        return ResponseEntity.noContent().build();
    }


    @GetMapping("/emailVerify")
    public ResponseEntity<Object> PostEmailVerifyHandler(@RequestBody PostEmailVerifyRequestDto requestBody) {

        String email = requestBody.getEmail();
        List<User> user = userService.findUser(UserSearchType.USER_SEARCH_TYPE_EMAIL, email);
        if (!user.get(0).isEmpty() || user.size() > 1) {
            log.info("Email={} already exist", email);
            throw new EmailAlreadyUseException();
        }

        String verificationCode = verifyService.generateEmailVerificationCode(email);
        verifyService.sendVerifyEmail(email, verificationCode);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/emailVerify")
    public ResponseEntity<Object> GetEmailCheckHandler(HttpServletRequest request, @RequestBody EmailCheckRequestDto requestBody) {

        boolean verified = verifyService.validateEmailByCode(requestBody.getEmail(), requestBody.getCode());

        if (verified) {
            HttpSession session = request.getSession();
            session.setAttribute(SessionConstants.EMAIL_VERIFY, true);

            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/loginIdValidate")
    public ResponseEntity<Object> GetLoginIdValidate(@RequestBody String loginId) {

        List<User> user = userService.findUser(UserSearchType.USER_SEARCH_TYPE_LOGIN_ID, loginId);
        if (!user.get(0).isEmpty()) {
            throw new UserAlreadyExistException();
        }

        return ResponseEntity.ok().build();
    }
}
