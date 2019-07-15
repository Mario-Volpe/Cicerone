package com.example.cicerone.data.model;

import android.content.Context;
import android.os.AsyncTask;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendIt extends AsyncTask<String,String,Void> {

    private String mailhost = "smtp.gmail.com";
    private String user = "ciceronestep@gmail.com";
    private String password = "itpscorsob";
    private Session session;
    private String email;
    private String subject;
    private String corpo;
    private Context context;

    public SendIt(String email,String subject,String corpo,Context context){
        setCorpo(corpo);
        setEmail(email);
        setSubject(subject);
        setContext(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected Void doInBackground(String... strings) {
        Properties props = new Properties();

        props.put("mail.smtp.host", mailhost);
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        //Creating a new session
        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user,password);
                    }
                });

        try {
            //Creating MimeMessage object
            MimeMessage mm = new MimeMessage(session);

            //Setting sender address
            mm.setFrom(new InternetAddress(user));
            //Adding receiver
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            //Adding subject
            mm.setSubject(subject);
            //Adding message
            mm.setText(corpo);

            //Sending email
            Transport.send(mm);


        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCorpo(String corpo) {
        this.corpo = corpo;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
