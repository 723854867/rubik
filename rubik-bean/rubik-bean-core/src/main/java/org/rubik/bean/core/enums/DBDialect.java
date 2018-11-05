package org.rubik.bean.core.enums;

public enum DBDialect {

	mysql {
		@Override
		public String keyWordWrapper() {
			return "`{0}`";
		}
	},
	oracle {
		@Override
		public String keyWordWrapper() {
			return "\"{0}\"";
		}
	},
	sqlserver {
		@Override
		public String keyWordWrapper() {
			return "[{0}]";
		}
	};
	
	public abstract String keyWordWrapper();
}
