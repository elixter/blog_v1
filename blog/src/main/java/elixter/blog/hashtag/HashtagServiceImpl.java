package elixter.blog.hashtag;

import elixter.blog.post.PostRepository;

import java.util.ArrayList;

public class HashtagServiceImpl implements HashtagService {

    private final HashtagRepository hashtagRepository;

    public HashtagServiceImpl(HashtagRepository hashtagRepository) {
        this.hashtagRepository = hashtagRepository;
    }

    @Override
    public void createHashtag(Hashtag hashtag) {
        hashtagRepository.save(hashtag);
    }

    @Override
    public Hashtag findHashtagById(Long id) {
        return hashtagRepository.findById(id);
    }

    @Override
    public Hashtag findHashtagByValue(String value) {
        return hashtagRepository.findByValue(value);
    }

    @Override
    public ArrayList<Hashtag> findHashtagsByPostId(Long postId) {
        return null;
    }

    @Override
    public ArrayList<Hashtag> findAllHashtags() {
        return hashtagRepository.findAll();
    }
}
