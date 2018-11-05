package org.rubik.bean.core.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pair<KEY, VALUE> implements Serializable {

	private static final long serialVersionUID = 1734806554323028349L;

	private KEY key;
	private VALUE value;
	
	@Override
	public String toString() {
		return "Pair [key=" + key + ", value=" + value + "]";
	}
}
