package com.blue.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.blue.config.property.FinanceiroApiProperty;

@Configuration
public class MailConfig {

	@Autowired
	private FinanceiroApiProperty financeiroApiProperty;
	
	@Bean
	public JavaMailSender javaMailSender() {
		
		Properties props = new Properties();
		
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.starttls.enable", true);
		props.put("mail.smtp.connectiontimeout", 10000);
		
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		
		mailSender.setHost(financeiroApiProperty.getMail().getHost());
		mailSender.setPort(financeiroApiProperty.getMail().getPort());
		mailSender.setUsername(financeiroApiProperty.getMail().getUsername());
		mailSender.setPassword(financeiroApiProperty.getMail().getPassword());
		mailSender.setJavaMailProperties(props);
		
		return mailSender;
	}
}
