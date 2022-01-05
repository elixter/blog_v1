package elixter.blog.order;

public interface OrderService {
    Order createOrder(Long memberId, String itemName, int itemPrice);
}
