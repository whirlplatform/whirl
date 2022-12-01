package org.whirlplatform.editor.server.merge;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.whirlplatform.editor.shared.merge.ChangeUnit;
import org.whirlplatform.editor.shared.merge.MergeException;
import org.whirlplatform.editor.shared.merge.Merger;
import org.whirlplatform.editor.shared.visitor.SearchGraphVisitor;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.ApplicationElement;

public class ReflectionMerger implements Merger {

    private SearchGraphVisitor search = new SearchGraphVisitor();

    @Override
    public void merge(ApplicationElement left, List<ChangeUnit> changes) {
        for (ChangeUnit c : changes) {
            switch (c.getType()) {
                case Add:
                    mergeAdd(left, c);
                    break;
                case Remove:
                    mergeRemove(left, c);
                    break;
                case Change:
                    mergeChange(left, c);
                    break;
            }
            merge(left, c.getNestedChanges());
        }
    }

    private <T> T search(ApplicationElement application, Object value) {
        Object result = null;
        if (value instanceof AbstractElement) {
            result = search.search(application, ((AbstractElement) value).getId());
        }
        if (result == null) {
            result = value;
        }
        return (T) result;
    }

    private Field getDeclaredClassOrSuperclassField(Class<?> clazz, String property)
            throws NoSuchFieldException {
        Class<?> current = clazz;
        while (current != null) {
            try {
                return current.getDeclaredField(property);
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            }
        }
        throw new NoSuchFieldException();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void mergeAdd(ApplicationElement application, ChangeUnit change) {
        try {
            AbstractElement target = search(application, change.getTarget());
            Class<?> clazz = target.getClass();
            Field field = getDeclaredClassOrSuperclassField(clazz, change.getField());
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            Class<?> fieldType = field.getType();
            if (Map.class.isAssignableFrom(fieldType)) {
                Map map = (Map) field.get(target);
                map.put(search(application, change.getKey()), change.getRightValue());
                return;
            } else if (List.class.isAssignableFrom(fieldType)) {
                List list = (List) field.get(target);
                if (change.getKey() != null && change.getKey() instanceof Integer) {
                    list.add((Integer) change.getKey(), change.getRightValue());
                } else {
                    list.add(change.getRightValue());
                }
                return;
            } else if (Collection.class.isAssignableFrom(fieldType)) {
                Collection collection = (Collection) field.get(target);
                collection.add(change.getRightValue());
                return;
            }
            throw new MergeException("Unsupported add type");
        } catch (ReflectiveOperationException e) {
            throw new MergeException(e);
        }
    }

    @SuppressWarnings("rawtypes")
    private void mergeRemove(ApplicationElement application, ChangeUnit change) {
        try {
            AbstractElement target = search(application, change.getTarget());
            Class<?> clazz = target.getClass();
            Field field = getDeclaredClassOrSuperclassField(clazz, change.getField());
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            Class<?> fieldType = field.getType();
            if (Map.class.isAssignableFrom(fieldType)) {
                ((Map) field.get(target)).remove(search(application, change.getKey()));
                return;
            } else if (Collection.class.isAssignableFrom(fieldType)) {
                ((Collection) field.get(target)).remove(search(application, change.getLeftValue()));
                return;
            }
            throw new MergeException("Unsupportet remove type");
        } catch (ReflectiveOperationException e) {
            throw new MergeException(e);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void mergeChange(ApplicationElement application, ChangeUnit change) {
        try {
            if (change.getField() == null) {
                return;
            }
            AbstractElement target = search(application, change.getTarget());
            Class<?> clazz = target.getClass();
            Field field = getDeclaredClassOrSuperclassField(clazz, change.getField());
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            Class<?> fieldType = field.getType();
            if (Map.class.isAssignableFrom(fieldType)) {
                Map map = (Map) field.get(target);
                map.remove(search(application, change.getKey()));
                map.put(search(application, change.getKey()), change.getRightValue());
                return;
            } else if (List.class.isAssignableFrom(fieldType)) {
                List list = (List) field.get(target);
                list.remove(search(application, change.getLeftValue()));
                if (change.getKey() != null && change.getKey() instanceof Integer) {
                    list.add((Integer) change.getKey(), change.getRightValue());
                } else {
                    list.add(change.getRightValue());
                }
                return;
            } else if (Collection.class.isAssignableFrom(fieldType)) {
                Collection collection = (Collection) field.get(target);
                collection.remove(search(application, change.getLeftValue()));
                collection.add(change.getRightValue());
                return;
            } else {
                field.set(target, change.getRightValue());
                return;
            }
        } catch (ReflectiveOperationException e) {
            throw new MergeException(e);
        }
    }

}
