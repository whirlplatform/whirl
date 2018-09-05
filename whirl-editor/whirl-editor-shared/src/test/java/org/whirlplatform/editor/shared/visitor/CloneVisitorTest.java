package org.whirlplatform.editor.shared.visitor;

import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import com.google.common.reflect.Reflection;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;
import org.javers.core.metamodel.clazz.EntityDefinition;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.LocaleElement;
import org.whirlplatform.meta.shared.editor.PropertyValue;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.meta.shared.editor.db.SchemaElement;
import org.whirlplatform.meta.shared.editor.db.TableColumnElement;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class CloneVisitorTest {

    private JaversBuilder builder;
    private static final Set<Class<?>> ENTITIES = new HashSet<>();
    private static final Set<Class<?>> VALUES = new HashSet<>();
    private static final Set<Class<?>> VALUE_OBJECTS = new HashSet<>();

    static {
        collectAllSubclasses(ENTITIES, AbstractElement.class);
        collectAllSubclasses(VALUES, DataValue.class);
        collectAllSubclasses(VALUES, PropertyValue.class);
    }

    private static void collectAllSubclasses(Set<Class<?>> collection, Class<?> superclass) {
        try {
            Set<ClassInfo> classes = ClassPath.from(superclass.getClassLoader())
                    .getTopLevelClassesRecursive(Reflection.getPackageName(superclass));
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

    private Javers javers() {
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
        return builder.build();
    }

    public Diff compare(AbstractElement left, AbstractElement right) {
        Javers javers = builder.build();
        return javers.compare(left, right);
    }

    LocaleElement defaultLocale;
    SchemaElement schema;
    PlainTableElement table;

    private PlainTableElement createTable(String id, String code, String name, String tableName) {
        PlainTableElement table = new PlainTableElement();
        table.setId(id);
        table.setCode(code);
        table.setName(name);
        table.setTableName(tableName);
        table.setTitle(new PropertyValue(DataType.STRING, defaultLocale, name));
        return table;
    }

    private TableColumnElement createColumn(String id, DataType type, String columnName, int width, boolean hidden) {
        TableColumnElement column = new TableColumnElement();
        column.setId(id);
        column.setType(type);
        column.setColumnName(columnName);
        column.setWidth(width);
        column.setHidden(hidden);
        return column;
    }

    @Before
    public void before() {
        defaultLocale = new LocaleElement("ru", "RU");

        schema = new SchemaElement();
        table = createTable("0", "test_code", "Table name", "TABLE");

        TableColumnElement idColumn = createColumn("10", DataType.STRING, "ID_COLUMN", 200, false);
        table.addColumn(idColumn);
        table.setIdColumn(idColumn);

        TableColumnElement deleteColumn = createColumn("11", DataType.BOOLEAN, "DELETE_COLUMN", 200, false);
        table.addColumn(deleteColumn);
        table.setDeleteColumn(deleteColumn);

    }

    @Ignore
    @Test
    public void testPlainTableCopy() {
        // Copy table
        CloneVisitor<PlainTableElement> copier = new CloneVisitor<>(table, true, false);
        PlainTableElement copy = copier.copy();

        Diff diff = javers().compare(table, copy);
        assertFalse("Copied object not equals: " + diff.prettyPrint(), diff.hasChanges());

        copy.setName("Other name");
        copy.setTitle(new PropertyValue(DataType.STRING, defaultLocale, "Name has changed"));

        diff = javers().compare(table, copy);
        assertEquals("Copied object should not be equals: " + diff.prettyPrint(), diff.getChanges().size(), 2);

        // Copy table with clone
        PlainTableElement clone = createTable("1", "table_clone", "Clone", "CLONE");
        TableColumnElement cloneId = createColumn("20", DataType.STRING, "ID_COLUMN", 200, false);
        clone.addColumn(cloneId);
        clone.setIdColumn(cloneId);

        TableColumnElement deleteId = createColumn("11", DataType.BOOLEAN, "DELETE_COLUMN", 200, false);
        clone.addColumn(deleteId);
        clone.setDeleteColumn(deleteId);

        copy.addClone(clone);

        copier = new CloneVisitor<PlainTableElement>(copy, true, false);
        PlainTableElement copyClones = copier.copy();

        diff = javers().compare(copy, copyClones);
        assertTrue("Clone tables should not be copied: " + diff.prettyPrint(), diff.hasChanges());

    }

}
