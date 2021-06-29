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

import static org.assertj.core.api.Assertions.*;

class ReferenceMapUnitTests {

	@Test
	void mustContainAddedReference(){

		final ReferenceMap<Integer> map = new ReferenceMap<>();

		map.addReference("name", 23);

		assertThat(map.get("name")).containsExactly(23);
	}

	@Test
	void mustContainAddedReferencesWithSameName(){

		final ReferenceMap<Integer> map = new ReferenceMap<>();

		map.addReference("name", 23);
		map.addReference("name", 42);

		assertThat(map.get("name")).containsExactlyInAnyOrder(23, 42);
	}

	@Test
	void mustContainAddedReferencesWithDifferentNames(){

		final ReferenceMap<Integer> map = new ReferenceMap<>();

		map.addReference("one", 23);
		map.addReference("two", 42);
		map.addReference("two", 422);
		map.addReference("three", 23);
		map.addReference("three", 42);
		map.addReference("three", 4711);

		assertThat(map.get("one")).containsExactlyInAnyOrder(23);
		assertThat(map.get("two")).containsExactlyInAnyOrder(42, 422);
		assertThat(map.get("three")).containsExactlyInAnyOrder(23, 42, 4711);
	}

	@Test
	void canRemoveOneOfMultipleEntries(){

		final ReferenceMap<Integer> map = new ReferenceMap<>();

		map.addReference("name", 23);
		map.addReference("name", 42);
		assertThat(map.removeReference("name", 42)).isTrue();

		assertThat(map.get("name")).containsExactly(23);
	}

	@Test
	void cantRemoveNonExistantEntries(){

		final ReferenceMap<Integer> map = new ReferenceMap<>();

		map.addReference("name", 23);
		map.addReference("name", 42);
		assertThat(map.removeReference("name", 43)).isFalse();
		assertThat(map.removeReference("key", 42)).isFalse();

		assertThat(map.get("name")).containsExactlyInAnyOrder(23, 42);
	}
}