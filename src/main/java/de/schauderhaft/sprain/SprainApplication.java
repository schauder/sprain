package de.schauderhaft.sprain;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Arrays.*;

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

		subjects.deleteAll();

	}
}
