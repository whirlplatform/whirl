package org.whirlplatform.integration.event;

import java.io.Serializable;

public class TransmissionParameters implements Serializable {
    private static final long serialVersionUID = -6230798110918896798L;
    private String text;
    private String number;
    private String date;
    private Boolean bool;

    public TransmissionParameters() {
    }

    public TransmissionParameters(String text, String number, String date, Boolean bool) {
        this.text = text;
        this.number = number;
        this.date = date;
        this.bool = bool;
    }

    public String getText() {
        return text;
    }

    public TransmissionParameters setText(String text) {
        this.text = text;
        return this;
    }

    public String getNumber() {
        return number;
    }

    public TransmissionParameters setNumber(String number) {
        this.number = number;
        return this;
    }

    public String getDate() {
        return date;
    }

    public TransmissionParameters setDate(String date) {
        this.date = date;
        return this;
    }

    public Boolean getBool() {
        return bool;
    }

    public TransmissionParameters setBool(Boolean bool) {
        this.bool = bool;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bool == null) ? 0 : bool.hashCode());
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((number == null) ? 0 : number.hashCode());
        result = prime * result + ((text == null) ? 0 : text.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TransmissionParameters other = (TransmissionParameters) obj;
        if (bool == null) {
            if (other.bool != null)
                return false;
        } else if (!bool.equals(other.bool))
            return false;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (number == null) {
            if (other.number != null)
                return false;
        } else if (!number.equals(other.number))
            return false;
        if (text == null) {
            return other.text == null;
        } else return text.equals(other.text);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(this.getClass().getSimpleName());
        builder.append("[");
        builder.append("text:").append(text).append(",");
        builder.append("num:").append(number).append(",");
        builder.append("date:").append(date).append(",");
        builder.append("bool:").append(bool);
        builder.append("]");
        return builder.toString();
    }
}
