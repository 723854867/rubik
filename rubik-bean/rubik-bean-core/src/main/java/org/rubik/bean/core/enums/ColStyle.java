package org.rubik.bean.core.enums;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 列名转换规则
 * 
 * @author lynn
 */
public enum ColStyle {

	// 原值
	normal,
	// 驼峰转小点
	camel2dot {
		@Override
		public String convert(String value) {
			if (null == value || value.isEmpty())
				return value;
			value = String.valueOf(value.charAt(0)).toUpperCase().concat(value.substring(1));
			StringBuilder builder = new StringBuilder();
			Matcher matcher = CAMEL.matcher(value);
			while (matcher.find()) {
				String word = matcher.group();
				builder.append(word.toLowerCase());
				builder.append(matcher.end() == value.length() ? "" : ".");
			}
			return builder.toString();
		}
	},
	dot2Camel {
		@Override
		public String convert(String value) {
			if (null == value || value.isEmpty())
				return value;
			StringBuilder sb = new StringBuilder();
			Matcher matcher = DOT.matcher(value);
			while (matcher.find()) {
				String word = matcher.group();
				if (matcher.start() == 0)
					sb.append(Character.toLowerCase(word.charAt(0)));
				else 
					sb.append(Character.toUpperCase(word.charAt(0)));
				int index = word.lastIndexOf('.');
				sb.append(index > 0 ? word.substring(1, index).toLowerCase() : word.substring(1).toLowerCase());
			}
			return sb.toString();
		}
	},
	// 点转大驼峰
	dot2BigCamel {
		@Override
		public String convert(String value) {
			if (null == value || value.isEmpty())
				return value;
			StringBuilder sb = new StringBuilder();
			Matcher matcher = DOT.matcher(value);
			while (matcher.find()) {
				String word = matcher.group();
				sb.append(Character.toUpperCase(word.charAt(0)));
				int index = word.lastIndexOf('.');
				sb.append(index > 0 ? word.substring(1, index).toLowerCase() : word.substring(1).toLowerCase());
			}
			return sb.toString();
		}
	},
	// 驼峰转下划线
	camel2underline {
		@Override
		public String convert(String value) {
			if (null == value || value.isEmpty())
				return value;
			value = String.valueOf(value.charAt(0)).toUpperCase().concat(value.substring(1));
			StringBuilder builder = new StringBuilder();
			Matcher matcher = CAMEL.matcher(value);
			while (matcher.find()) {
				String word = matcher.group();
				builder.append(word.toLowerCase());
				builder.append(matcher.end() == value.length() ? "" : "_");
			}
			return builder.toString();
		}
	},
	// 下划线转驼峰(首字母小写)
	underline2Camel {
		@Override
		public String convert(String value) {
			if (null == value || value.isEmpty())
				return value;
			StringBuilder sb = new StringBuilder();
			Matcher matcher = UNDERLINE.matcher(value);
			while (matcher.find()) {
				String word = matcher.group();
				if (matcher.start() == 0)
					sb.append(Character.toLowerCase(word.charAt(0)));
				else 
					sb.append(Character.toUpperCase(word.charAt(0)));
				int index = word.lastIndexOf('_');
				sb.append(index > 0 ? word.substring(1, index).toLowerCase() : word.substring(1).toLowerCase());
			}
			return sb.toString();
		}
	},
	// 下划线转大驼峰(首字母大写)
	undlerline2BigCamel {
		@Override
		public String convert(String value) {
			if (null == value || value.isEmpty())
				return value;
			StringBuilder sb = new StringBuilder();
			Matcher matcher = UNDERLINE.matcher(value);
			while (matcher.find()) {
				String word = matcher.group();
				sb.append(Character.toUpperCase(word.charAt(0)));
				int index = word.lastIndexOf('_');
				sb.append(index > 0 ? word.substring(1, index).toLowerCase() : word.substring(1).toLowerCase());
			}
			return sb.toString();
		}
	},
	// 转换为大写
	uppercase {
		@Override
		public String convert(String value) {
			return value.toUpperCase();
		}
	},
	// 转换为小写
	lowercase {
		@Override
		public String convert(String value) {
			return value.toLowerCase();
		}
	};

	private static final Pattern CAMEL = Pattern.compile("[A-Z]([a-z\\d]+)?");
	private static final Pattern DOT = Pattern.compile("([A-Za-z\\d]+)(\\.)?");
	private static final Pattern UNDERLINE = Pattern.compile("([A-Za-z\\d]+)(_)?");

	public String convert(String value) {
		return value;
	}
}
