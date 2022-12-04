package org.whirlplatform.server.compiler;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.jci.stores.ResourceStore;
import org.apache.commons.lang.StringUtils;
import org.xeustechnologies.jcl.JarClassLoader;

public class CompilationData {

    private static Map<String, String> sourceCache = new HashMap<>();
    private ResourceStore resourceStore;
    private ClassLoader mainClassLoader;
    private JarClassLoader applicationClassLoader;

    public CompilationData(ResourceStore resourceStore,
                           ClassLoader mainClassLoader, JarClassLoader applicationClassLoader) {
        this.resourceStore = resourceStore;
        this.mainClassLoader = mainClassLoader;
        this.applicationClassLoader = applicationClassLoader;
    }

    public ClassLoader getMainClassLoader() {
        return mainClassLoader;
    }

    public ResourceStore getResourceStore() {
        return resourceStore;
    }

    public JarClassLoader getApplicationClassLoader() {
        return applicationClassLoader;
    }

    public boolean sourceChanged(String className, String source) {
        if (StringUtils.isEmpty(className) || StringUtils.isEmpty(source)) {
            return true;
        }
        return !sourceCache.containsKey(className)
                || !StringUtils.equals(source, sourceCache.get(className));
    }

    public void cacheSource(String className, String source) {
        sourceCache.put(className, source);
    }

}
