package org.whirlplatform.component.client.utils;

public class Pair<A, B> {

    private final A first;
    private final B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public static <A, B> A[] asArrayOfFirst(Pair<A, B>[] pairs, A[] zeroArray) {
        @SuppressWarnings("unchecked")
        A[] result = (A[]) new Object[pairs.length];
        for (int i = 0; i < pairs.length; i++) {
            result[i] = pairs[i].getFirst();
        }
        return result;
    }

    public static <A, B> B[] asArrayOfSecond(Pair<A, B>[] pairs, B[] zeroArray) {
        @SuppressWarnings("unchecked")
        B[] result = (B[]) new Object[pairs.length];
        for (int i = 0; i < pairs.length; i++) {
            result[i] = pairs[i].getSecond();
        }
        return result;
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }

}
