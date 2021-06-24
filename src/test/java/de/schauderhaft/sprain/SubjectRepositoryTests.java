
/*
 * Copyright 2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.schauderhaft.sprain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;

import java.util.List;

import static java.util.Arrays.*;
import static org.assertj.core.api.Assertions.*;

@DataNeo4jTest
class SubjectRepositoryTests {

	@Autowired
	SubjectRepository subjects;

	@Test
	void test() {
		subjects.deleteAll();

		final Subject jens = new Subject("jens");
		final Subject developer = new Subject("developer");
		final Subject character = new Subject("character");
		final Subject frodo = new Subject("frodo");

		subjects.saveAll(asList(jens, developer, character, frodo));

		assertThat(jens.id).isNotNull();
		assertThat(developer.id).isNotNull();
		assertThat(character.id).isNotNull();
		assertThat(frodo.id).isNotNull();

		jens.addRelation("is a", developer);
		frodo.addRelation("is a", character);
		jens.addRelation("likes", frodo);

		subjects.saveAll(asList(jens, developer, character, frodo));

		final List<Subject> all = subjects.findAll();

		assertThat(all).contains(jens);
		assertThat(jens.getRelation()).hasSize(2);
	}

}