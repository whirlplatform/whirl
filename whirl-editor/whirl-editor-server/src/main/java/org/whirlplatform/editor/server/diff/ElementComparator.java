package org.whirlplatform.editor.server.diff;

import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import com.google.gwt.thirdparty.guava.common.reflect.Reflection;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;
import org.javers.core.metamodel.clazz.EntityDefinition;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.PropertyValue;

public class ElementComparator {

    //    private Javers javers;
    private static final Set<Class<?>> ENTITIES = new HashSet<>();
    private static final Set<Class<?>> VALUES = new HashSet<>();
    private static final Set<Class<?>> VALUE_OBJECTS = new HashSet<>();

    static {
        collectAllSubclasses(ENTITIES, AbstractElement.class);
        collectAllSubclasses(VALUES, DataValue.class);
        collectAllSubclasses(VALUES, PropertyValue.class);
    }

    private JaversBuilder builder;

    public ElementComparator() {
        builder = JaversBuilder.javers();
        for (Class<?> c : ENTITIES) {
            builder.registerEntity(new EntityDefinition(c, "id"));
        }

        for (Class<?> c : VALUES) {
            builder.registerValue(c);
        }

        for (Class<?> c : VALUE_OBJECTS) {
            builder.registerValueObject(c);
        }

    }

    private static void collectAllSubclasses(Set<Class<?>> collection,
                                             Class<?> superclass) {
        try {
            Set<ClassInfo> classes = ClassPath.from(superclass.getClassLoader())
                    .getTopLevelClassesRecursive(
                            Reflection.getPackageName(superclass));
            for (ClassInfo info : classes) {
                Class<?> base = info.load();
                if (base.isInterface() || base.isEnum()) {
                    continue;
                }
                Class<?> sup = base;
                while (sup != null) {
                    if (sup == superclass) {
                        collection.add(base);
                        break;
                    }
                    sup = sup.getSuperclass();
                }
            }
        } catch (IOException e) {
        }
    }

    public Diff compare(AbstractElement left, AbstractElement right) {
        Javers javers = builder.build();
        return javers.compare(left, right);
    }

}
