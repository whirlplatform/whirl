package org.whirlplatform.server.config;

import java.util.Map;

public interface Configuration {

	<T> T lookup(String name);

	<T> Map<String, T> lookupAll(String path, Class<T> cls);
	
}
