package org.whirlplatform.meta.shared;

import java.util.Date;

public interface DateValueProvider {

    String toString(Date value);

    Date fromString(String value);

}
