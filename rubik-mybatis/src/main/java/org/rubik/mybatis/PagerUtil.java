package org.rubik.mybatis;

import java.util.ArrayList;
import java.util.List;

import org.rubik.bean.core.model.Pager;
import org.rubik.util.callback.Callback;
import org.rubik.util.common.CollectionUtil;

import com.github.pagehelper.Page;

public class PagerUtil {

	public static final <T> Pager<T> page(List<T> list) {
		Pager<T> pager = new Pager<T>();
		if (list instanceof Page) {
			List<T> l = new ArrayList<T>(list.size());
			list.forEach(item -> l.add(item));
			Page<T> page = (Page<T>) list;
			pager.setPages(page.getPages());
			pager.setTotal(page.getTotal());
			pager.setList(l);
			page.close();
		} else
			pager.setList(list);
		return pager;
	}
	
	public static final <T, K> Pager<T> page(List<K> list, Callback<K, T> callback) {
		Pager<T> pager = new Pager<T>();
		if (!CollectionUtil.isEmpty(list)) {
			List<T> l = new ArrayList<T>();
			list.forEach(k -> l.add(callback.invoke(k)));
			pager.setList(l);
		}
		if (list instanceof Page) {
			Page<K> page = (Page<K>) list;
			pager.setPages(page.getPages());
			pager.setTotal(page.getTotal());
			page.close();
		}
		return pager;
	}
}
