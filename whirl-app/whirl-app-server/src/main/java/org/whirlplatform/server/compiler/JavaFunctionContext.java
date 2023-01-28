package org.whirlplatform.server.compiler;

import org.whirlplatform.java.FunctionContext;
import org.whirlplatform.java.Scope;
import org.whirlplatform.server.login.ApplicationUser;

public class JavaFunctionContext implements FunctionContext {

    private ApplicationUser user;

    public JavaFunctionContext(ApplicationUser user) {
        this.user = user;
    }

    @Override
    public void putObject(Scope scope, String key, Object object) {
        if (Scope.SESSION == scope) {
            user.saveJavaObject(key, object);
        } else if (Scope.SESSION == scope) {
            // skipped
        }
    }

    @Override
    public Object getObject(Scope scope, String key) {
        if (Scope.SESSION == scope) {
            return user.loadJavaObject(key);
        } else if (Scope.APPLICATION == scope) {
            return null;
        }
        return null;
    }

    @Override
    public Object removeObject(Scope scope, String key) {
        if (Scope.SESSION == scope) {
            return user.removeJavaObject(key);
        } else if (Scope.APPLICATION == scope) {
            // skipped
        }
        return null;
    }

    @Override
    public boolean hasObject(Scope scope, String key) {
        if (Scope.SESSION == scope) {
            return user.hasJavaObject(key);
        } else if (Scope.APPLICATION == scope) {
            // skipped
        }
        return false;
    }

}
