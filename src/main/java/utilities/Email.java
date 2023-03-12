package utilities;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Email{



    public static String Reporturl;


    static void readTextfile() throws IOException {

        FileReader fr = new FileReader(System.getProperty("user.dir") + "/mavenlogs.txt");
        System.out.println(System.getProperty("user.dir"));
        BufferedReader br = new BufferedReader(fr);
        String str;
        while ((str = br.readLine()) != null) {

            if (str.contains("https://reports.cucumber.io/reports")) {
                str = str.trim();
                str = str.replaceAll("\\s", "");
                String result = str.split("h")[1];
                result = "h" + result;
                result = result.split("\\[")[0];
                Reporturl = result;
                System.out.println("Reporturl :" + Reporturl);
            }

        }

    }

    public static void emailTrigger(String recipient) throws IOException  {


        final String fromEmail = "simpuudemy@gmail.com"; //requires valid gmail id
        final String password = "akdyrvmfbzxcxncv"; // correct password for gmail id

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS



        //create Authenticator object to pass in Session.getInstance argument
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };
        Session session = Session.getInstance(props, auth);


        try
        {
            readTextfile();
            // Create object of MimeMessage class
            Message message = new MimeMessage(session);

            BodyPart messageBodyPart1 = new MimeBodyPart();

            String messagtosend = "Hi,"
                    + "<br><br><b>Please click on the below link to view the test results    </b>"
                    + "<br><br>";

            String regards = ""
                    + "<br><br>Regards,"
                    + "<br>Shibahar Nagarajan";

            messageBodyPart1.setContent(messagtosend, "text/html; charset=utf-8");


            MimeBodyPart messageBodyPart2 = new MimeBodyPart();
            MimeBodyPart messageBodyPart3 = new MimeBodyPart();

            String reportLink =Reporturl;
            messageBodyPart2.setText(reportLink);
            messageBodyPart3.setContent(regards,"text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();

            multipart.addBodyPart(messageBodyPart1);
            multipart.addBodyPart(messageBodyPart2);
            multipart.addBodyPart(messageBodyPart3);
            message.setSubject("Gitlab issues Api Automation Report");
            message.setContent(multipart);

            //****************

            message.setFrom(new InternetAddress("no_reply@automation.com", "Noreply-Automation"));

            message.setReplyTo(InternetAddress.parse("no_reply@automation.com", false));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient, false));
            System.out.println("Message is ready");
            Transport.send(message);

            System.out.println("EMail Sent Successfully!!");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


}






