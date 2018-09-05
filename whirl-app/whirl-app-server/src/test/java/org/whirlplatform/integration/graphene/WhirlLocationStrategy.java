package org.whirlplatform.integration.graphene;

import org.jboss.arquillian.graphene.spi.findby.LocationStrategy;
import org.openqa.selenium.By;
import org.whirlplatform.selenium.ByWhirl;

import java.lang.annotation.Annotation;

public class WhirlLocationStrategy implements LocationStrategy {

    @Override
    public By fromAnnotation(Annotation annotation) {
        FindByWhirl a = (FindByWhirl) annotation;
        return new ByWhirl(a.value());
    }

}
