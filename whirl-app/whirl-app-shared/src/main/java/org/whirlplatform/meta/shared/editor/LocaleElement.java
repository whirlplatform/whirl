package org.whirlplatform.meta.shared.editor;

import java.io.Serializable;
import lombok.Data;

@SuppressWarnings("serial")
@Data
public class LocaleElement implements Serializable, Cloneable {

    private String language;
    private String country;

    public LocaleElement() {
    }

    public LocaleElement(String language, String country) {
        this.language = language;
        this.country = country;
    }

    public static LocaleElement valueOf(String string) {

        if (string == null || string.isEmpty()) {
            throw new IllegalArgumentException();
        }
        String language = null;
        String country = null;
        if ((string.length() == 2 || string.indexOf("_") == 2)
            && Character.isLetter(string.charAt(0))
            && Character.isLetter(string.charAt(1))) {
            //language = string.toLowerCase();
            language = string.toLowerCase().substring(0, 2);
        } else {
            throw new IllegalArgumentException();
        }
        if (string.length() == 5) {
            country = string.substring(3, 5);
        }
        return new LocaleElement(language, country);
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
            + ((language == null) ? 0 : language.hashCode());
        result = prime * result + ((country == null) ? 0 : country.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof LocaleElement)) {
            return false;
        }
        LocaleElement other = (LocaleElement) obj;
        if (language == null) {
            if (other.language != null) {
                return false;
            }
        } else if (!language.equals(other.language)) {
            return false;
        }
        if (country == null) {
            return other.country == null;
        } else {
            return country.equals(other.country);
        }
    }

    public String asString() {
        return language
            + ((country == null || country.isEmpty()) ? "" : "_" + country);
    }

    @Override
    public String toString() {
        return asString();
    }

    public LocaleElement clone() {
        LocaleElement clone = new LocaleElement(language, country);
        return clone;
    }

}
