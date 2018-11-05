package org.rubik.util.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	public static final String EMPTY = "";

	public static final boolean hasText(CharSequence str) {
		if (!hasLength(str))
			return false;
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i)))
				return true;
		}
		return false;
	}

	public static final boolean hasLength(CharSequence str) {
		return (str != null && str.length() > 0);
	}

	public static final String trimIncludeNull(String content) {
		return !hasText(content) ? EMPTY : content.trim();
	}

	/**
	 * 替换字符串
	 */
	public static String replace(String string, String oldPattern, String newPattern) {
		if (!hasLength(string) || !hasLength(oldPattern) || newPattern == null)
			return string;
		StringBuilder sb = new StringBuilder();
		int pos = 0;
		int index = string.indexOf(oldPattern);
		int patLen = oldPattern.length();
		while (index >= 0) {
			sb.append(string.substring(pos, index));
			sb.append(newPattern);
			pos = index + patLen;
			index = string.indexOf(oldPattern, pos);
		}
		sb.append(string.substring(pos));
		return sb.toString();
	}
	
	/**
	 * 脱敏
	 */
	public static final String mask(String content, int top, int tail) {
		int len = content.length() - top - tail;
		if (len <= 0)
			return content;
		StringBuilder builder = new StringBuilder();
		int index = 0;
		while (top-- > 0)
			builder.append(content.charAt(index++));
		while (len-- > 0) {
			builder.append("*");
			index++;
		}
		while (tail-- > 0)
			builder.append(content.charAt(index++));
		return builder.toString();
	}

	/**
	 * 匹配数字或带小数的数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumberOrPoint(String str) {
		Pattern pattern = Pattern.compile("([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])");
		Matcher isNumP = pattern.matcher(str);
		if (!isNumP.matches())
			return false;
		return true;
	}
}
