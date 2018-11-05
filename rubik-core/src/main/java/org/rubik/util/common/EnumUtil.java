package org.rubik.util.common;

import org.rubik.bean.core.enums.IEnum;

public class EnumUtil {
	
	public static <MARK, E extends Enum<?> & IEnum<MARK>> E valueOf(Class<E> enumClass, Object code) {
		E[] enumConstants = enumClass.getEnumConstants();
		for (E e : enumConstants) {
			String mark = e.mark().toString();
			if (code.toString().equals(mark))
				return e;
		}
		return null;
	}
}
