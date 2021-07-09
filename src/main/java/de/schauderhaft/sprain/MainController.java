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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
public class MainController {

	@Autowired
	SubjectRepository subjects;

	@Autowired
	RelationService relations;

	@RequestMapping
	ModelAndView main() {

		KnowledgeGraph graph = new KnowledgeGraph();
		final List<Subject> all = subjects.findAll();
		graph.getNodes().addAll(all);

		return new ModelAndView("main", Collections.singletonMap("graph", graph));
	}

	@RequestMapping(method = POST, path = "/relations")
	ModelAndView addRelation(@ModelAttribute("subject") String subjectName,
							 @ModelAttribute("verb") String verbString,
							 @ModelAttribute("object") String objectName) {

		final Subject subject = relations.getOrCreateSubject(subjectName);
		final Subject object = relations.getOrCreateSubject(objectName);
		relations.createRelation(subject, verbString, object);

		return main();
	}

	@RequestMapping(method = POST, path = "/subject/{id}")
	ModelAndView removeSubject(@PathVariable Long id) {

		subjects.deleteById(id);

		return main();
	}
}
