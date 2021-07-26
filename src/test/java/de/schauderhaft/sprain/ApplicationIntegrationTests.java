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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class ApplicationIntegrationTests {

	@MockBean(name="subjectRepository")
	SubjectRepository subjects;

	@Autowired
	WebApplicationContext webApplicationContext;

	MockMvc mockMvc;

	@BeforeEach
	void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
	}

	@Test
	void mainPageContainsRefToItself() throws Exception {

		MvcResult mvcResult = this.mockMvc.perform(get("/"))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		assertThat(mvcResult.getResponse().getContentType()).isEqualTo("text/html;charset=UTF-8");

		final String content = mvcResult.getResponse().getContentAsString();

		final Document doc = Jsoup.parse(content);
		final Elements anchors = doc.select("h1 > a[href]");

		assertThat(anchors).hasSize(1);

		final Element anchor = anchors.get(0);

		assertThat(anchor.attr("href")).isEqualTo("/");
		assertThat(anchor.text()).isEqualTo("Sprain");

	}

	@Test
	void mainPageContainsSubjectElement() throws Exception {

		Mockito.when(subjects.findAll()).thenReturn(singletonList(new Subject("one")));

		MvcResult mvcResult = this.mockMvc.perform(get("/"))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		assertThat(mvcResult.getResponse().getContentType()).isEqualTo("text/html;charset=UTF-8");

		final String content = mvcResult.getResponse().getContentAsString();

		final Document doc = Jsoup.parse(content);
		final Elements anchors = doc.select("div > h4");

		assertThat(anchors).hasSize(1);

		final Element anchor = anchors.get(0);

		assertThat(anchor.text()).isEqualTo("one");

	}

}
