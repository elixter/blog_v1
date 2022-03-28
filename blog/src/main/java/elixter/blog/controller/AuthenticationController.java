package elixter.blog.controller;

import elixter.blog.constants.SessionConstants;
import elixter.blog.domain.user.User;
import elixter.blog.domain.user.SessionUser;
import elixter.blog.dto.auth.PostLoginRequestDto;
import elixter.blog.dto.user.GetUserResponseDto;
import elixter.blog.message.Message;
import elixter.blog.service.auth.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@Transactional
public class AuthenticationController {
    private final AuthenticationService service;

    @Autowired
    public AuthenticationController(AuthenticationService service) {
        this.service = service;
    }

    @PostMapping("/signin")
    public ResponseEntity<Message> PostSigninHandler(
            @RequestBody PostLoginRequestDto requestBody,
            HttpServletRequest request
    ) {
        User loginUser = service.login(requestBody);

        SessionUser signinUser = new SessionUser(loginUser.getId(), loginUser.getLoginId());

        HttpSession session = request.getSession();
        session.setAttribute(SessionConstants.AUTHENTICATION, signinUser);
        session.setMaxInactiveInterval(SessionConstants.SESSION_TIMEOUT);

        log.debug("UserInfo : {} try to signin", signinUser);

        GetUserResponseDto responseData = new GetUserResponseDto();
        responseData.mapping(loginUser);
        Message responseBody = new Message(HttpStatus.OK, "authenticated", responseData);

        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/signout")
    public ResponseEntity<Message> GetSignoutHandler(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        service.logout(session);

        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<Message> get(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        User currentUser = service.getSessionUser(session);

        return new ResponseEntity<>(new Message(HttpStatus.OK, "Authenticated", currentUser), HttpStatus.OK);
    }

}
