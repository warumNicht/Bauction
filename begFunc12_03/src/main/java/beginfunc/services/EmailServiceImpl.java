package beginfunc.services;

import beginfunc.constants.EmailCredentialsConstants;
import beginfunc.services.contracts.EmailService;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class EmailServiceImpl implements EmailService {

    @Override
    public boolean sendEmail(String destinationEmail, String subject, String body) {
        try {
            Session session = getSession();

            MimeMessage msg = new MimeMessage(session);
            //set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress(EmailCredentialsConstants.DEFAULT_APP_EMAIL_ADDRESS, "NoReply-JD"));

            msg.setReplyTo(InternetAddress.parse(destinationEmail, false));

            msg.setSubject(subject, "UTF-8");
            msg.setText(body, "UTF-8");
            msg.setSentDate(new Date());

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinationEmail, false));
            System.out.println("Message is ready");
            Transport.send(msg);

            System.out.println("EMail Sent Successfully!!");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static Session getSession() {
        String fromEmail = EmailCredentialsConstants.DEFAULT_APP_EMAIL_ADDRESS; //requires valid gmail id
        String password = EmailCredentialsConstants.DEFAULT_APP_EMAIL_PASSWORD; // correct password for gmail id

        System.out.println("SSLEmail Start");
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.socketFactory.port", "465"); //SSL Port
        props.put("mail.smtp.socketFactory.fallBack", "false");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory"); //SSL Factory Class
        props.put("mail.smtp.auth", "true"); //Enabling SMTP Authentication
        props.put("mail.smtp.port", "465"); //SMTP Port

        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };

        return Session.getDefaultInstance(props, auth);
    }


}
