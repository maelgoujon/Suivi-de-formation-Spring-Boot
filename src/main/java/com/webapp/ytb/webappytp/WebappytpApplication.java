package com.webapp.ytb.webappytp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebappytpApplication {

	public static void main(String[] args) {
		Logger log = LoggerFactory.getLogger(WebappytpApplication.class);
		log.info("Demarrage de l'application...");
		SpringApplication.run(WebappytpApplication.class, args);
	}

}
