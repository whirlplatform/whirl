package org.whirlplatform.integration.graphene;

import org.jboss.arquillian.graphene.spi.findby.ImplementsLocationStrategy;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Анотация
 *
 * @author semenov_pa
 */
@Retention(RUNTIME)
@Target(FIELD)
@ImplementsLocationStrategy(WhirlLocationStrategy.class)
public @interface FindByWhirl {

    String value();

}
