// package com.nrifintech.medicalmanagementsystem.service;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;

// @Service
// public class EmailServiceImpl {

//     @Value("${spring.mail.username}")
//     private String sender;

//     @Autowired
//     private JavaMailSender javaMailSender;

//     @Override
//     public String sendMail(String recipient, String subject, String textBody)
//     {
//         //  MimeMessage message = mailSender.createMimeMessage();

//         // message.setFrom(sender);
//         // message.setRecipients(MimeMessage.RecipientType.TO, "recipient@example.com");
//         // message.setSubject("Test email from Spring");

//         // String htmlContent = "<h1>This is a test Spring Boot email</h1>" +
//         //                     "<p>It can contain <strong>HTML</strong> content.</p>";
//         // message.setContent(htmlContent, "text/html; charset=utf-8");

//         // mailSender.send(message);

//         MimeMessage message = emailSender.createMimeMessage();
//         MimeMessageHelper helper = new MimeMessageHelper(message, true); // true indicates multipart message

//         helper.setFrom(sender);
//         helper.setTo(recipient);
//         helper.setSubject(subject);
//         helper.setText(textBody, false); // true indicates HTML content

//         emailSender.send(message);
//         // MimeMessage mimeMessage = javaMailSender.createMimeMessage();


//         // MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);


//         // mimeMessageHelper.setFrom(sender);
//         // mimeMessageHelper.setTo(recipient);
//         // mimeMessageHelper.setSubject(subject);
//         // mimeMessageHelper.setText(fromEmail);
//         // mimeMessageHelper.setFrom(fromEmail);
//         // mimeMessageHelper.setFrom(fromEmail);
//     }

    
// }