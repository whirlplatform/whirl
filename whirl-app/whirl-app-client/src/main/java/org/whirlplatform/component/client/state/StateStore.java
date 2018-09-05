package org.whirlplatform.component.client.state;

public interface StateStore<T> {

    StateScope getScope();

    boolean save(String code, T value);

    T restore(String code);

    T remove(String code);
}
