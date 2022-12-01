package org.whirlplatform.java;

public interface FunctionContext {

    void putObject(Scope scope, String key, Object object);

    Object getObject(Scope scope, String key);

    Object removeObject(Scope scope, String key);

    boolean hasObject(Scope scope, String key);

}
