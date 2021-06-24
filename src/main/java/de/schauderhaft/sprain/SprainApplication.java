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

		final Subject jens = new Subject("jens");
		final Subject developer = new Subject("developer");
		final Subject character = new Subject("character");
		final Subject frodo = new Subject("frodo");
		final Subject bilbo = new Subject("bilbo");
		final Subject sam = new Subject("sam");
		final Subject underhill = new Subject("underhill");
		final Subject baggins = new Subject("baggins");

		subjects.saveAll(asList(jens,developer,frodo,character,bilbo,sam,underhill,baggins));

		jens.addRelation("is a", developer);
		frodo.addRelation("is a", character);
		frodo.addRelation("is named", baggins);
		bilbo.addRelation("is a", character);
		sam.addRelation("is a", character);
		frodo.addRelation("alias", underhill);
		jens.addRelation("likes", frodo);
		jens.addRelation("likes", bilbo);
		jens.addRelation("likes", sam);

		subjects.saveAll(asList(jens,developer,frodo,character,bilbo,sam,underhill,baggins));

	}
}
