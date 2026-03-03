package me.ilucah.audiovisualiser_service;

import me.ilucah.audiovisualiser_service.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
public class AudiovisualiserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AudiovisualiserServiceApplication.class, args);
	}

}
