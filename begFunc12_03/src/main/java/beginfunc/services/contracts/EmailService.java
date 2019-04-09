package beginfunc.services.contracts;

public interface EmailService {
    boolean sendEmail(String toEmail, String subject, String body);
}
