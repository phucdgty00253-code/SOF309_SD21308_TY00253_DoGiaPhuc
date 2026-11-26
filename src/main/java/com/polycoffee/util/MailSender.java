package com.polycoffee.util;

/** Tách giao diện gửi mail để unit test thay được bằng đối tượng giả. */
public interface MailSender {
    /** Ném BusinessRuleException khi không gửi được. */
    void send(String to, String subject, String body);
}
