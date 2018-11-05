package org.rubik.mybatis.extension;

import java.io.Serializable;
import java.util.Set;

import org.rubik.bean.core.enums.DBDialect;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DBConfig implements Serializable {

	private static final long serialVersionUID = 3752818278796516201L;

	private String jdbc;
	private int minIdle = 10;
	private String username;
	private String password;
	@SerializedName("idle.timeout")
	private int idleTimeout = 600000;
	@SerializedName("max.life.time")
	private int maxLifeTime = 1800000;
	@SerializedName("max.pool.size")
	private int maxPoolSize = 10;
	@SerializedName("conn.timeout")
	private int connTimeout = 30000;
	private DBDialect dialect = DBDialect.mysql;
	@SerializedName("driver.class")
	private String driverClass = "com.mysql.cj.jdbc.Driver";
	@SerializedName("cache.enabled")
	private boolean cacheEnabled = false;
	@SerializedName("prep.stmt.cache.size")
	private int prepStmtCacheSize = 250;
	@SerializedName("mapper.location")
	private String mapperLocation = "classpath*:conf/mybatis/*.xml";
	@SerializedName("datasource.class")
	private String datasourceClass = "com.zaxxer.hikari.HikariDataSource";
	@SerializedName("cache.prep.stmts")
	private boolean cachePrepStmts = true;
	@SerializedName("camel2underline")
	private boolean camel2underline = true;
	@SerializedName("base.package")
	private Set<String> basePackage;
	@SerializedName("pagehelper.enable")
	private boolean pagehelperEnable = false;
	@SerializedName("prep.stmt.cache.sql.limit")
	private int prepStmtCacheSqlLimit = 2048;
	@SerializedName("type.aliases.package")
	private Set<String> typeAliasesPackage;
}
