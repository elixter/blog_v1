package elixter.blog.service.auth;

public interface CertificationService {

    boolean ValidateEmailByCode(String email, String code);
}
