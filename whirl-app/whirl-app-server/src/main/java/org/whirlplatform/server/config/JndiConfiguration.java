package org.whirlplatform.server.config;

import org.whirlplatform.server.utils.ContextUtil;

import java.util.Map;

public class JndiConfiguration implements Configuration {

	@Override
	public <T> T lookup(String name) {
		return ContextUtil.lookup(name);
	}

	@Override
	public <T> Map<String, T> lookupAll(String path, Class<T> cls) {
		return ContextUtil.lookupAll(path, cls);
	}

}
