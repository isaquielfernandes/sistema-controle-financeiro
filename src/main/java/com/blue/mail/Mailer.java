package com.blue.mail;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.blue.model.Lancamento;
import com.blue.model.Usuario;

@Component
public class Mailer {

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private TemplateEngine thymeleaf;
	
	public void enviarEmail(String remetente, List<String> destinatarios, String assunto, String template, Map<String,Object> variaveis) {
		Context context = new Context(new Locale("pt", "BR"));
		
		variaveis.entrySet().forEach(e -> context.setVariable(e.getKey(), e.getValue()));
		
		String mensagem = thymeleaf.process(template,  context);
		
		this.enviarEmail(remetente, destinatarios, assunto, mensagem);
	}
	
	public void enviarEmail(String remetente, List<String> destinatarios, String assunto, String mensagem) {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
			messageHelper.setFrom(remetente);
			messageHelper.setTo(destinatarios.toArray(new String[destinatarios.size()]));
			messageHelper.setSubject(assunto);
			messageHelper.setText(mensagem, true);
			
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			throw new RuntimeException("Problema com o envio de email ", e.getCause());
		}
	}
	
	public void avisarSobreLancamentosVencidos(List<Lancamento> vencidos, List<Usuario> destinatarios) {
		Map<String, Object> variaveis = new HashMap<>();
		variaveis.put("lancamentos", vencidos);
		
		List<String> emails = destinatarios.stream().map(u -> u.getEmail()).collect(Collectors.toList());
		
		this.enviarEmail("iisaquielfernandes@outlook.com", emails, 
						"Lançamentos vencidos", 
						"mail/aviso-lancamentos-vencidos", variaveis);
	}
}
