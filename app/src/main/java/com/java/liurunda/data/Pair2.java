package com.java.liurunda.data;

import java.io.Serializable;

public class Pair2<T extends Serializable, U extends Serializable> implements Serializable {
    public T first;
    public U second;

    public static<T extends Serializable, U extends Serializable> Pair2<T, U> create(T first, U second) {
        Pair2<T, U> ans = new Pair2<>();
        ans.first = first;
        ans.second = second;
        return ans;
    }
}
