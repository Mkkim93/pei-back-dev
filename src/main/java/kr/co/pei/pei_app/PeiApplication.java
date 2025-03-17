package kr.co.pei.pei_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

@SpringBootApplication(scanBasePackages = "kr.co.pei.pei_app")
public class PeiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PeiApplication.class, args);
	}
}
