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

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Set;

public class ReferenceMap<T> extends AbstractMap<String, List<T>> {

	private final Map<String, List<T>> map = new HashMap<>();
	@Override
	public Set<Entry<String, List<T>>> entrySet() {
		return map.entrySet();
	}

	public void addReference(String name, T value) {

		final List<T> List = computeIfAbsent(name, __ -> new ArrayList<>());
		List.add(value);
	}

	@Override
	public List<T> put(String key, List<T> value) {
		return map.put(key, value);
	}

	public boolean removeReference(String name, T value) {
		final List<T> List = map.get(name);
		if (List != null) {
			return List.remove(value);
		}
		return false;
	}
}
