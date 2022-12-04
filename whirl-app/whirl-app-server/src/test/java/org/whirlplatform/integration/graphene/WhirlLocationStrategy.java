package org.whirlplatform.integration.graphene;

import java.lang.annotation.Annotation;
import org.jboss.arquillian.graphene.spi.findby.LocationStrategy;
import org.openqa.selenium.By;
import org.whirlplatform.selenium.ByWhirl;

public class WhirlLocationStrategy implements LocationStrategy {

    @Override
    public By fromAnnotation(Annotation annotation) {
        FindByWhirl a = (FindByWhirl) annotation;
        return new ByWhirl(a.value());
    }

}
