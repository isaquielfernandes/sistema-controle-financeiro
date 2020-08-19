package com.blue.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.BucketLifecycleConfiguration;
import com.amazonaws.services.s3.model.BucketLifecycleConfiguration.Rule;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.Tag;
import com.amazonaws.services.s3.model.lifecycle.LifecycleFilter;
import com.amazonaws.services.s3.model.lifecycle.LifecycleTagPredicate;
import com.blue.config.property.FinanceiroApiProperty;

@Configuration
public class S3Config {

	@Autowired
	private FinanceiroApiProperty property;
	
	@Bean
	public AmazonS3 amazonS3() {
		
		AWSCredentials credenciais = new BasicAWSCredentials(property.getS3().getAcessKeyId(), property.getS3().getSecretAcessKey());
		
		AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credenciais))
				.withRegion(Regions.EU_WEST_1)
				.build();
		
		if(!amazonS3.doesBucketExistV2(property.getS3().getBucket())) {
			amazonS3.createBucket(new CreateBucketRequest(property.getS3().getBucket()));
			
			Rule regraDeExpiracao = new BucketLifecycleConfiguration.Rule()
					.withId("Regra de expiração de arquivos temporários")
					.withFilter(new LifecycleFilter(new LifecycleTagPredicate(new Tag("expirar", "true"))))
					.withExpirationInDays(1)
					.withStatus(BucketLifecycleConfiguration.ENABLED);
			
			BucketLifecycleConfiguration config = new BucketLifecycleConfiguration().withRules(regraDeExpiracao);
			
			amazonS3.setBucketLifecycleConfiguration(property.getS3().getBucket(), config);
			
		}
		
		return amazonS3;
	}
}
