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

import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Node
class Subject {

	@Id
	@GeneratedValue
	public Long id;

	public String name;

	public Map<String, List<Subject>> references = new HashMap<>();

	public Subject(String name) {
		this.name = name;
	}

	@PersistenceConstructor
	private Subject(long id, String name) {

		this.id = id;
		this.name = name;
	}

	public void addRelation(String name, Subject value) {

		final List<Subject> subjects = references.computeIfAbsent(name, __ -> new ArrayList<>());
		subjects.add(value);
	}

	public Collection<Relation> getRelation() {

		return references.entrySet().stream()
				.flatMap(e -> e.getValue().stream()
						.map(v -> new Relation(e.getKey(), v))
				).toList();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Subject subject = (Subject) o;
		return Objects.equals(id, subject.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "Subject{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}
}

