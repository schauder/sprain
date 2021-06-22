package de.schauderhaft.sprain;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
public class SprainApplication implements InitializingBean {

	@Autowired
	private SubjectRepository subjects;

	public static void main(String[] args) {
		SpringApplication.run(SprainApplication.class, args);
	}

	@Override
	@Transactional
	public void afterPropertiesSet() {

		subjects.save(new Subject("jens"));
		subjects.save(new Subject("developer"));
		subjects.save(new Subject("frodo"));
		subjects.save(new Subject("character"));

		subjects.save(new Subject("bilbo"));
		subjects.save(new Subject("sam"));
		subjects.save(new Subject("underhill"));
		subjects.save(new Subject("baggins"));

	}
}
