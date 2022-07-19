package elixter.blog.service.auth;

public interface CertificationService {
    boolean validateEmailByCode(String email, String code);
    String generateEmailCertificationCode(String email);
}
