package org.whirlplatform.integration.graphene;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.jboss.arquillian.graphene.spi.findby.ImplementsLocationStrategy;

/**
 * Анотация
 */
@Retention(RUNTIME)
@Target(FIELD)
@ImplementsLocationStrategy(WhirlLocationStrategy.class)
public @interface FindByWhirl {

    String value();

}
