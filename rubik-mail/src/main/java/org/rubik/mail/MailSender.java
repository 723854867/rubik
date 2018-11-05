package org.rubik.mail;

import java.io.File;
import java.io.InputStream;
import java.util.Set;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

import org.springframework.core.io.InputStreamResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("mailSender")
public class MailSender {
	
	@Resource
	private MailConfig mailConfig;
	@Resource
	private JavaMailSender javaMailSender;
	
	public void sendMail(String acceptor, String subject, String text) { 
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(mailConfig.getFrom());
		message.setTo(acceptor);
		message.setSubject(subject);
		message.setText(text);
		javaMailSender.send(message);
	}
	
	public void sendHtmlMail(Set<String> acceptors, String subject, String text) { 
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
			helper.setSubject(subject);
			helper.setFrom(mailConfig.getFrom());
			String[] tos = acceptors.toArray(new String[] {});
			helper.setTo(tos);
			helper.setText(text, true);
			javaMailSender.send(message);
		} catch (Exception e) {
			log.error("邮件 - {}:{} 发送失败，接受者 - {}！", subject, text, acceptors, e);
			throw new RuntimeException(e);
		} 
	}
	
	public void sendMail(String acceptor, String subject, String text, File file) { 
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(mailConfig.getFrom());
			helper.setTo(acceptor);
			helper.setSubject(subject);
			helper.setText(text);
			helper.addAttachment(file.getName(), file);
			javaMailSender.send(message);
		} catch (Exception e) {
			log.error("邮件 - {}:{} 发送失败，接受者 - {}！", subject, text, acceptor, e);
			throw new RuntimeException(e);
		} 
	}
	
	public void sendMail(String acceptor, String subject, String text, String fileName, InputStream input) { 
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(mailConfig.getFrom());
			helper.setTo(acceptor);
			helper.setSubject(subject);
			helper.setText(text);
			helper.addAttachment(fileName, new InputStreamResource(input));
			javaMailSender.send(message);
		} catch (Exception e) {
			log.error("邮件 - {}:{} 发送失败，接受者 - {}！", subject, text, acceptor, e);
			throw new RuntimeException(e);
		} 
	}
}
