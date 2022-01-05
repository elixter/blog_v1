package elixter.blog.order;

import elixter.blog.discount.DiscountPolicy;
import elixter.blog.discount.FixDiscountPolicy;
import elixter.blog.discount.RateDiscountPolicy;
import elixter.blog.member.Member;
import elixter.blog.member.MemberRepository;
import elixter.blog.member.MemoryMemberRepository;

public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository ;
    private final DiscountPolicy discountPolicy;

    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }
}
