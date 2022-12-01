package org.whirlplatform.server.config;

import java.util.Map;
import org.whirlplatform.server.utils.ContextUtil;

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
