package com.ky.notificationservice.exception;

import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailSendingException extends RuntimeException{
    public EmailSendingException(MessagingException message){
        super(message);
    }
}