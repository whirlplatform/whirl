package org.whirlplatform.meta.shared.editor;

import java.io.Serializable;

/**
 * Enumerates all possible file element categories which the application can hold.
 *
 * @author bedritckiy_mr
 */
public enum FileElementCategory implements Serializable {
    JAVASCRIPT, CSS, JAVA, IMAGE, STATIC, DATA;

    /**
     * Gets the file element category object using the string name
     *
     * @param name
     * @return the file element category instance or null
     */
    public static FileElementCategory get(final String name) {
        for (FileElementCategory category : FileElementCategory.values()) {
            if (category.toString().equalsIgnoreCase(name)) {
                return category;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
