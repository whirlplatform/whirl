package org.whirlplatform.java;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Inject {

	String name() default "";

	boolean singleton() default false;

	Scope scope() default Scope.SESSION;

}
