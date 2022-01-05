package elixter.blog.Order;

import elixter.blog.AppConfig;
import elixter.blog.member.Grade;
import elixter.blog.member.Member;
import elixter.blog.member.MemberService;
import elixter.blog.member.MemberServiceImpl;
import elixter.blog.order.Order;
import elixter.blog.order.OrderService;
import elixter.blog.order.OrderServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OrderServiceTest {

    MemberService memberService;
    OrderService orderService;

    @BeforeEach
    public void beforeEach() {
        AppConfig appConfig = new AppConfig();
        memberService = appConfig.memberService();
        orderService = appConfig.orderService();
    }

    @Test
    void createOrder() {
        Long memberId = 1L;
        Member member = new Member(memberId, "memberA", Grade.VIP);
        memberService.join(member);

        Order order = orderService.createOrder(memberId, "itemA", 10000);
        Assertions.assertThat(order.getDiscountPrice()).isEqualTo(1000);
    }
}
