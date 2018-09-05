package org.whirlplatform.rpc.shared;

import java.util.List;

public class ListHolder<T> {
    List<T> list;

    public ListHolder() {
    }

    public ListHolder(List<T> list) {
        this.list = list;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
