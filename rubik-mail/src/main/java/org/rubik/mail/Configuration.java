package org.rubik.mail;

import java.util.List;
import java.util.Properties;

import org.rubik.bean.core.enums.ColStyle;
import org.rubik.util.ConfigLoader;
import org.rubik.util.ConfigMap;
import org.rubik.util.reflect.EntityField;
import org.rubik.util.reflect.FieldHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@org.springframework.context.annotation.Configuration("mailConfiguration")
class Configuration {
	
	@Bean
	public MailConfig mailConfig() {
		ConfigMap map = ConfigLoader.load("classpath:conf/mail.properties");
		MailConfig config = map.toBean(MailConfig.class, ColStyle.camel2dot);
		List<EntityField> fields = FieldHelper.getAll(MailConfig.class);
		fields.forEach(field -> map.remove(field.getName()));
		Properties properties = new Properties();
		map.entrySet().forEach(entry -> properties.setProperty(entry.getKey(), entry.getValue().toString().trim()));
		config.setProperties(properties);
		return config;
	}

	@Bean
	public JavaMailSender javaMailSender(MailConfig mailConfig) {
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		sender.setHost(mailConfig.getHost());
		sender.setPort(mailConfig.getPort());
		sender.setProtocol(mailConfig.getProtocol());
		sender.setUsername(mailConfig.getUsername());
		sender.setPassword(mailConfig.getPassword());
		sender.setDefaultEncoding(mailConfig.getEncoding());
		sender.setJavaMailProperties(mailConfig.getProperties());
		return sender;
	}
}
