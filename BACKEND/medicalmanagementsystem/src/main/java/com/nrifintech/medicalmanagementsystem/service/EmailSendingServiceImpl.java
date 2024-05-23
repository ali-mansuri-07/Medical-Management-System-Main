package com.nrifintech.medicalmanagementsystem.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;


@Service
public class EmailSendingServiceImpl implements EmailSendingService{

    
    @Value("${spring.mail.username}")
    private String sender;

    @Autowired
    private JavaMailSender emailSender;

    public void sendBookingCancelledMail(String recipient, String date, String time, String name) throws MessagingException
    {
       
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        String textBody = "<html>\n" +
        "<head>\n" +
        "    <style>\n" +
        "        body {\n" +
        "            font-family: Arial, sans-serif;\n" +
        "            background-color: #f4f4f4;\n" +
        "            padding: 20px;\n" +
        "        }\n" +
        "        .button{ \n" +
        "            align-self: center; \n" +
        "            padding: 20px 100px 18px 100px; \n" +
        "            color: white; \n" +
        "            font-family: arial; \n" +
        "            font-size: 18px; \n" +
        "            letter-spacing: 1px; \n" +
        "            text-transform: uppercase; \n" +
        "            text-align: center; \n" +
        "            background: rgb(255, 85, 85); \n" +
        "            border-radius: 10px; \n" +
        "            transition: all 0.1s ease-in-out; \n" +
        "            cursor: pointer; \n" +
        "        }\n" +
        "        " +
        "        .button:hover{ \n" +
        "        background: #E03A3A;\n" +
        "        }\n" +
        "        " +
        "        .button:active{\n" +
        "            transform: scale(1.025);\n" +
        "        }\n" +
        "        .container {\n" +
        "            max-width: 600px;\n" +
        "            margin: 0 auto;\n" +
        "            background-color: #fff;\n" +
        "            border-radius: 10px;\n" +
        "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
        "            border: 10px solid black;\n" +
        "            padding: 20px;\n" +
        "        }\n" +
        "        h1, p {\n" +
        "            color: #333;\n" +
        "        }\n" +
        "    </style>\n" +
        "</head>\n" +
        "<body>\n" +
        "    <div class=\"container\">\n" +
        "        <h1>Dear " + name + ",</h1>\n" +
        "        <p>We hope this message finds you well.</p>\n" +
        "        <p>We want to inform you that your appointment scheduled for " + date +
        " at " + time +
        " has been successfully cancelled in our medical management system.</p>\n" +
        "        <p>If you have any further questions or need assistance, please feel free to contact our office.</p>\n" +
        "        <p>Thank you for your patience.</p>\n" +
        "        <p>Best regards,<br/>Medicare</p>\n" +
        "        <button class=\"button\" type=\"submit\" ><a href=\"http://localhost:4200\" style=\"text-decoration: none; color: white; font-weight: bold;\">Visit Website</a></button>\n" +

        "    </div>\n" +
        "</body>\n" +
        "</html>";
        
        helper.setFrom(sender);
        helper.setTo(recipient);
        helper.setSubject("APPOINTMENT CANCELLED CONFIRMATION");
        helper.setText(textBody, true); 

        emailSender.send(message);
      
    }

    public void sendBookingConfirmationMail(String recipient,String date, String time, String name) throws MessagingException
    {
      
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        String textBody = "<html>\n" +
        "<head>\n" +
        "    <style>\n" +
        "        body {\n" +
        "            font-family: Arial, sans-serif;\n" +
        "            background-color: #f4f4f4;\n" +
        "            padding: 20px;\n" +
        "        }\n" +
        "        .button{ \n" +
        "            align-self: center; \n" +
        "            padding: 20px 100px 18px 100px; \n" +
        "            color: white; \n" +
        "            font-family: arial; \n" +
        "            font-size: 18px; \n" +
        "            letter-spacing: 1px; \n" +
        "            text-transform: uppercase; \n" +
        "            text-align: center; \n" +
        "            background: rgb(255, 85, 85); \n" +
        "            border-radius: 10px; \n" +
        "            transition: all 0.1s ease-in-out; \n" +
        "            cursor: pointer; \n" +
        "        }\n" +
        "        " +
        "        .button:hover{ \n" +
        "        background: #E03A3A;\n" +
        "        }\n" +
        "        " +
        "        .button:active{\n" +
        "            transform: scale(1.025);\n" +
        "        }\n" +
        "        .container {\n" +
        "            max-width: 600px;\n" +
        "            margin: 0 auto;\n" +
        "            background-color: #fff;\n" +
        "            border-radius: 10px;\n" +
        "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
        "            border: 10px solid black;\n" +
        "            padding: 20px;\n" +
        "        }\n" +
        "        h1, p {\n" +
        "            color: #333;\n" +
        "        }\n" +
        "    </style>\n" +
        "</head>\n" +
        "<body>\n" +
        "    <div class=\"container\">\n" +
        "        <h1>Dear " + name + ",</h1>\n" +
        "        <p>We hope this message finds you well.</p>\n" +
        "        <p>We want to inform you that your appointment scheduled for " + date +
        " at " + time +
        " has been successfully booked in our medical management system.</p>\n" +
        "        <p>If you have any further questions or need assistance, please feel free to contact our office.</p>\n" +
        "        <p>Thank you for your patience.</p>\n" +
        "        <p>Best regards,<br/>Medicare</p>\n" +
        "        <button class=\"button\" type=\"submit\" ><a href=\"http://localhost:4200\" style=\"text-decoration: none; color: white; font-weight: bold;\">Visit Website</a></button>\n" +

        "    </div>\n" +
        "</body>\n" +
        "</html>";
        
        helper.setFrom(sender);
        helper.setTo(recipient);
        helper.setSubject("APPOINTMENT BOOKING CONFIRMATION");
        helper.setText(textBody, true); 

        emailSender.send(message);
       
    }
    public void sendDoctorCredentials(String recipient, String username, String password) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        
        String textBody = "<html>\n" +
                "<head>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            background-color: #f4f4f4;\n" +
                "            padding: 20px;\n" +
                "        }\n" +
                "        .button { \n" +
                "            align-self: center; \n" +
                "            padding: 20px 100px 18px 100px; \n" +
                "            color: white; \n" +
                "            font-family: arial; \n" +
                "            font-size: 18px; \n" +
                "            letter-spacing: 1px; \n" +
                "            text-transform: uppercase; \n" +
                "            text-align: center; \n" +
                "            background: rgb(255, 85, 85); \n" +
                "            border-radius: 10px; \n" +
                "            transition: all 0.1s ease-in-out; \n" +
                "            cursor: pointer; \n" +
                "        }\n" +
                "        .button:hover { \n" +
                "            background: #E03A3A;\n" +
                "        }\n" +
                "        .button:active {\n" +
                "            transform: scale(1.025);\n" +
                "        }\n" +
                "        .container {\n" +
                "            max-width: 600px;\n" +
                "            margin: 0 auto;\n" +
                "            background-color: #fff;\n" +
                "            border-radius: 10px;\n" +
                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                "            border: 10px solid black;\n" +
                "            padding: 20px;\n" +
                "        }\n" +
                "        h1, p {\n" +
                "            color: #333;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <h1>Dear Doctor,</h1>\n" +
                "        <p>We hope this message finds you well.</p>\n" +
                "        <p>Your account has been successfully created in our medical management system.</p>\n" +
                "        <p>Below are your login credentials:</p>\n" +
                "        <p><strong>Username:</strong> " + username + "</p>\n" +
                "        <p><strong>Password:</strong> " + password + "</p>\n" +
                "        <p>If you have any further questions or need assistance, please feel free to contact our support team.</p>\n" +
                "        <p>Thank you for joining us.</p>\n" +
                "        <p>Best regards,<br/>Medicare</p>\n" +
                "        <button class=\"button\" type=\"submit\"><a href=\"http://localhost:4200\" style=\"text-decoration: none; color: white; font-weight: bold;\">Visit Website</a></button>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    
        helper.setFrom(sender);
        helper.setTo(recipient);
        helper.setSubject("Your Account Credentials for Medicare");
        helper.setText(textBody, true); 
    
        emailSender.send(message);
    }
    

}
