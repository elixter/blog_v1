package elixter.blog.controller;

import elixter.blog.dto.hashtag.SearchHashtagInterface;
import elixter.blog.service.hashtag.HashtagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Transactional
@Slf4j
@RequestMapping(value = "/api/hashtags")
public class HashtagController {
    private final HashtagService hashtagService;

    @Autowired
    public HashtagController(HashtagService hashtagService) {
        this.hashtagService = hashtagService;
    }

    @GetMapping
    ResponseEntity<List<SearchHashtagInterface>> GetSearchHashtags(
            @RequestParam(value = "search") String search,
            @RequestParam(value = "curPage", required = false) Long curPage,
            @RequestParam(value = "pageSize", required = false) Long pageSize
    ) {
        log.debug("search : {}, curPage : {}, pageSize : {}", search, curPage, pageSize);

        List<SearchHashtagInterface> searchHashtags = hashtagService.searchHashtagsByTag(search);

        return ResponseEntity.ok(searchHashtags);
    }
}
