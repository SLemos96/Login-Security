package com.BuracosDCApi;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.BuracosDCApi.core.configs.StorageProperties;

@SpringBootApplication(scanBasePackages = "com.BuracosDCApi")
@ComponentScan(basePackageClasses = BuracosDcApiApplication.class)
@EntityScan("com.BuracosDCApi")
@EnableJpaRepositories(basePackages = { "com.BuracosDCApi.core.repository", "com.BuracosDCApi.repository",
		"com.BuracosDCApi.exemplo" })
@EnableConfigurationProperties({ StorageProperties.class })
@EnableScheduling
public class BuracosDcApiApplication {

	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(BuracosDcApiApplication.class);
	}

	public static void main(String[] args) {
//		System.err.println("senha");
//		System.err.println(BCrypt.hashpw("teste", BCrypt.gensalt(12)));
		// descomente para criar senhas manuais
		SpringApplication.run(BuracosDcApiApplication.class, args);
	}

	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("America/Recife"));
	}
}
