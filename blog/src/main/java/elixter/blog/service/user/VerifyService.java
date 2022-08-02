package elixter.blog.service.user;

public interface VerifyService {
    boolean validateEmailByCode(String email, String code);
    String generateEmailVerificationCode(String email);
    void sendVerifyEmail(String email, String code);
}
