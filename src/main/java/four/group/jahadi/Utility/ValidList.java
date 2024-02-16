package four.group.jahadi.Utility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javax.validation.Valid;

public class ValidList<E> implements List<E> {
    private @Valid List<E> list;

    public ValidList() {
        this.list = new ArrayList();
    }

    public ValidList(List<E> list) {
        this.list = list;
    }

    public List<E> getList() {
        return this.list;
    }

    public void setList(List<E> list) {
        this.list = list;
    }

    public int size() {
        return this.list.size();
    }

    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    public boolean contains(Object o) {
        return this.list.contains(o);
    }

    public Iterator<E> iterator() {
        return this.list.iterator();
    }

    public Object[] toArray() {
        return this.list.toArray();
    }

    public <T> T[] toArray(T[] ts) {
        return this.list.toArray(ts);
    }

    public boolean add(E e) {
        return this.list.add(e);
    }

    public boolean remove(Object o) {
        return this.list.remove(o);
    }

    public boolean containsAll(Collection<?> collection) {
        return this.list.containsAll(collection);
    }

    public boolean addAll(Collection<? extends E> collection) {
        return this.list.addAll(collection);
    }

    public boolean addAll(int i, Collection<? extends E> collection) {
        return this.list.addAll(i, collection);
    }

    public boolean removeAll(Collection<?> collection) {
        return this.list.removeAll(collection);
    }

    public boolean retainAll(Collection<?> collection) {
        return this.list.retainAll(collection);
    }

    public void clear() {
        this.list.clear();
    }

    public E get(int i) {
        return this.list.get(i);
    }

    public E set(int i, E e) {
        return this.list.set(i, e);
    }

    public void add(int i, E e) {
        this.list.add(i, e);
    }

    public E remove(int i) {
        return this.list.remove(i);
    }

    public int indexOf(Object o) {
        return this.list.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return this.list.lastIndexOf(o);
    }

    public ListIterator<E> listIterator() {
        return this.list.listIterator();
    }

    public ListIterator<E> listIterator(int i) {
        return this.list.listIterator(i);
    }

    public List<E> subList(int i, int i1) {
        return this.list.subList(i, i1);
    }
}

