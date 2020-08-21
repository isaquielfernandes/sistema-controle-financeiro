package com.blue.storage;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.ObjectTagging;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.SetObjectTaggingRequest;
import com.amazonaws.services.s3.model.Tag;
import com.blue.config.property.FinanceiroApiProperty;

@Component
public class S3 {
	
	private static final Logger logger = LoggerFactory.getLogger(S3.class);
	
	@Autowired
	private FinanceiroApiProperty property;
	
	@Autowired
	private AmazonS3 amazonS3;

	public void salvar(String anexo) {
		SetObjectTaggingRequest objectTaggingRequest = new SetObjectTaggingRequest(
				property.getS3().getBucket(), anexo, new ObjectTagging(Collections.emptyList()));
		
		amazonS3.setObjectTagging(objectTaggingRequest);
	}

	public void remover(String anexo) {
		DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(
				property.getS3().getBucket(), anexo);
		
		amazonS3.deleteObject(deleteObjectRequest);
	}

	public void substituir(String anexoAntigo, String anexoNovo) {
		if(StringUtils.hasText(anexoAntigo))
			this.remover(anexoAntigo);
		
		salvar(anexoNovo);
	}
	
	public String configurarUrl(String obj) {
		return "\\"+ property.getS3().getBucket() + ".s3.amzonaws.com/" + obj;
	}
	
	public String salvarTemporariamente(MultipartFile arquivo) {
		AccessControlList accessControlList = new AccessControlList();
		accessControlList.grantPermission(GroupGrantee.AllUsers, Permission.Read);
		
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(arquivo.getContentType());
		metadata.setContentLength(arquivo.getSize());
		
		String nomeUnico = gerarNomeUnico(arquivo.getOriginalFilename());
		
		try {
			PutObjectRequest putObjectRequest = new PutObjectRequest(
					property.getS3().getBucket(), nomeUnico, arquivo.getInputStream(), metadata)
					.withAccessControlList(accessControlList);
			
			putObjectRequest.setTagging(new ObjectTagging(Arrays.asList(new Tag("expirar", "true"))));
			
			amazonS3.putObject(putObjectRequest);
			
			if(logger.isDebugEnabled())
				logger.debug("Arquivo {} enviado com sucesso para o S3", arquivo.getOriginalFilename());
			
		} catch (IOException e) {
			throw new RuntimeException("Problemas ao tentar enviar o arquivo para o s3.", e);
		}
		
		return nomeUnico;
	}
	
	private String gerarNomeUnico(String originalFileName) {
		return UUID.randomUUID().toString() + "_" + originalFileName;
	}

}
