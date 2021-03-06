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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RelationService {

	@Autowired
	SubjectRepository subjects;

	Subject getOrCreateSubject(String name) {

		Subject subject = subjects.findByName(name);

		return subject == null
				? subjects.save(new Subject(name))
				: subject;
	}

	public void createRelation(Subject subject, String verbString, Subject object) {

		subject.addRelation(verbString, object);
		subjects.save(subject);
	}

	public void removeRelation(Long subjectId, Long relationId) {

		subjects.findById(subjectId).ifPresent(
				s -> {
					s.removeRelation(relationId);
					subjects.save(s);
				}
		);
	}
}
