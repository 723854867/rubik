package org.rubik.util.callback;

public interface Callback<P, V> {

	V invoke(P param);
}

