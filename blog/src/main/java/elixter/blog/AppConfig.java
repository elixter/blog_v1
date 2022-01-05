package elixter.blog;

import elixter.blog.discount.DiscountPolicy;
import elixter.blog.discount.FixDiscountPolicy;
import elixter.blog.discount.RateDiscountPolicy;
import elixter.blog.member.MemberRepository;
import elixter.blog.member.MemberService;
import elixter.blog.member.MemberServiceImpl;
import elixter.blog.member.MemoryMemberRepository;
import elixter.blog.order.OrderService;
import elixter.blog.order.OrderServiceImpl;
import elixter.blog.post.MemoryPostRepository;
import elixter.blog.post.PostRepository;
import elixter.blog.post.PostService;
import elixter.blog.post.PostServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository()); // 생성자 주입
    }

    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

    @Bean
    public OrderService orderService() {
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    @Bean
    public DiscountPolicy discountPolicy() {
        return new RateDiscountPolicy();
    }

    @Bean
    public PostService postService() {
        return new PostServiceImpl(postRepository());
    }

    @Bean
    public PostRepository postRepository() {
        return new MemoryPostRepository();
    }
}
