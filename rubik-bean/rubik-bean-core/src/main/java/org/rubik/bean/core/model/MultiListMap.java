package org.rubik.bean.core.model;

import java.util.ArrayList;
import java.util.HashMap;

public class MultiListMap<K, E> extends HashMap<K, ArrayList<E>> {

	private static final long serialVersionUID = -1032741580169471827L;

	public void add(K key, E ele) {
		ArrayList<E> list = get(key);
		if (null == list) {
			list = new ArrayList<E>();
			put(key, list);
		}
		list.add(ele);
	}
}
