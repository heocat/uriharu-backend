package hh.com.uriharu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class UriharuApplication {

	public static void main(String[] args) {
		SpringApplication.run(UriharuApplication.class, args);
	}

}
