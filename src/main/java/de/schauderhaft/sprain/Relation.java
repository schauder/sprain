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

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import java.util.Objects;

@RelationshipProperties
public class Relation {

	@Id @GeneratedValue
	Long id;

	String name;

	@TargetNode
	Subject subject;

	public String getName() {
		return name;
	}

	public Subject getSubject() {
		return subject;
	}

	public Relation(String name, Subject subject) {

		assert name != null;
		assert subject != null;

		this.name = name;
		this.subject = subject;
	}

	public Long getId() {
		return id;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Relation relation = (Relation) o;

		return name.equals(relation.name) && subject.equals(relation.subject);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, subject);
	}
}
