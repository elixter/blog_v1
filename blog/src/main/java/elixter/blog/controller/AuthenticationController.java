package elixter.blog.controller;

import elixter.blog.domain.user.User;
import elixter.blog.domain.user.UserInfo;
import elixter.blog.dto.auth.PostLoginRequestDto;
import elixter.blog.dto.user.GetUserResponseDto;
import elixter.blog.message.Message;
import elixter.blog.service.user.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@Transactional
public class AuthenticationController {
    @Resource
    private UserInfo userinfo;

    private final AuthenticationService service;

    @Autowired
    public AuthenticationController(AuthenticationService service) {
        this.service = service;
    }

    @PostMapping("/signin")
    public ResponseEntity<Message> PostSigninHandler(
            @RequestBody PostLoginRequestDto requestBody
    ) {
        User loginUser = service.login(requestBody);

        userinfo.setUserId(loginUser.getId());
        userinfo.setUserLoginId(loginUser.getLoginId());

        GetUserResponseDto responseData = new GetUserResponseDto();
        responseData.mapping(loginUser);
        Message responseBody = new Message(HttpStatus.OK, "authenticated", responseData);

        return ResponseEntity.ok(responseBody);
    }
}
