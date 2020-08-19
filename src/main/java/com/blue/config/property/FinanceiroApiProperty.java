package com.blue.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@ConfigurationProperties("blue")
public class FinanceiroApiProperty {

	private final String ORIGINPERMITIDA = "http://localhost:8000";
	private final Seguranca seguranca = new Seguranca();
	private final Mail mail = new Mail();
	private final S3 s3 = new S3(); 
	
	@Data
	public static class S3{
		
		private String acessKeyId;
		private String secretAcessKey;
		private String bucket = "blue-financeiro-arquivos";
	}
	
	@Getter
	@Setter
	public static class Seguranca{
		
		private boolean enableHttps;
		
	}
	
	@Getter
	@Setter
	public static class Mail{
		
		private String host;
		private Integer port;
		private String username;
		private String password;
	}
}
