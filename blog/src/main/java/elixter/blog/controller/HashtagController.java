package elixter.blog.controller;

import elixter.blog.domain.hashtag.Hashtag;
import elixter.blog.domain.hashtag.SearchHashtag;
import elixter.blog.service.hashtag.HashtagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    List<SearchHashtag> GetSearchHashtags(@RequestParam String search) {
        return hashtagService.searchHashtagsByTag(search);
    }
}
