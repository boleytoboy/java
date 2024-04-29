package opendota;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoConfiguration
@SpringBootApplication
public class OpenDotaApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenDotaApiApplication.class, args);
	}
}
