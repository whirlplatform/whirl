package org.whirlplatform.meta.shared;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import org.whirlplatform.meta.shared.data.RowModelData;

import java.util.*;

@JsonTypeInfo(use = Id.MINIMAL_CLASS)
@JsonAutoDetect(fieldVisibility = Visibility.ANY, creatorVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class LoadData<T extends RowModelData> {

    private PageConfig page = new PageConfig();

    private List<T> data = new ArrayList<>();

    public LoadData() {
    }

    public LoadData(List<T> data) {
        this.data = data;
    }

    public List<T> getData() {
        return data;
    }

    public void setRows(int rows) {
        this.page.setRows(rows);
    }

    public int getRows() {
        return page.getRows();
    }

    public int size() {
        return data.size();
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public boolean contains(Object o) {
        return data.contains(o);
    }

    public Iterator<T> iterator() {
        return data.iterator();
    }

    public Object[] toArray() {
        return data.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return data.toArray(a);
    }

    public boolean add(T e) {
        return data.add(e);
    }

    public boolean remove(Object o) {
        return data.remove(o);
    }

    public boolean containsAll(Collection<?> c) {
        return data.contains(c);
    }

    public boolean addAll(Collection<? extends T> c) {
        return data.addAll(c);
    }

    public boolean addAll(int index, Collection<? extends T> c) {
        return data.addAll(index, c);
    }

    public boolean removeAll(Collection<?> c) {
        return data.removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        return data.retainAll(c);
    }

    public void clear() {
        data.clear();
    }

    public T get(int index) {
        return data.get(index);
    }

    public T set(int index, T element) {
        return data.set(index, element);
    }

    public void add(int index, T element) {
        data.add(index, element);
    }

    public T remove(int index) {
        return data.remove(index);
    }

    public int indexOf(Object o) {
        return data.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return data.lastIndexOf(o);
    }

    public ListIterator<T> listIterator() {
        return data.listIterator();
    }

    public ListIterator<T> listIterator(int index) {
        return data.listIterator(index);
    }

    public List<T> subList(int fromIndex, int toIndex) {
        return data.subList(fromIndex, toIndex);
    }
}
