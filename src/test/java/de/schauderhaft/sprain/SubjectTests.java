package de.schauderhaft.sprain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

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
class SubjectTests {

	@Test
	void returnsCorrectRelations() {

		final Subject subject = new Subject("Test Subject");
		final Subject one = new Subject("one");
		final Subject two = new Subject("two");
		final Subject three = new Subject("three");

		subject.addRelation("rel1", one);
		subject.addRelation("rel1", two);
		subject.addRelation("rel2", one);
		subject.addRelation("rel3", one);
		subject.addRelation("rel3", two);
		subject.addRelation("rel3", three);


		assertThat(subject.getRelation()).containsExactlyInAnyOrder(
				new Relation("rel1", one),
				new Relation("rel1", two),
				new Relation("rel2", one),
				new Relation("rel3", one),
				new Relation("rel3", two),
				new Relation("rel3", three)
		);
	}

}