package org.rubik.redis;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RedisConfig implements Serializable {

	private static final long serialVersionUID = -4598344635882033998L;

	private String host;
	private String master;
	private String password;
	private int port = 6379;
	@SerializedName("max.idle")
	private int maxIdle = 8;
	@SerializedName("min.idle")
	private int minIdle = 0;
	@SerializedName("max.total")
	private int maxTotal = 8;
	private int database = 0;
	private boolean lifo = true;
	private boolean enable = false;
	@SerializedName("max.wait.millis")
	private int maxWaitMillis = -1;
	@SerializedName("conn.timeout")
	private int connTimeout = 3000;
	@SerializedName("jmx.enabled")
	private boolean jmxEnabled = true;
	@SerializedName("test.on.borrow")
	private boolean testOnBorrow = false;
	@SerializedName("test.on.create")
	private boolean testOnCreate = false;
	@SerializedName("test.on.return")
	private boolean testOnReturn = false;
	@SerializedName("test.while.idle")
	private boolean testWhileIdle = false;
	@SerializedName("num.tests.per.eviction.run")
	private int numTestsPerEvictionRun = 3;
	@SerializedName("block.when.exhausted")
	private boolean blockWhenExhausted = true;
	@SerializedName("time.between.eviction.runs.millis")
	private int timeBetweenEvictionRunsMillis = -1;
	@SerializedName("min.evictable.idle.time.millis")
	private int minEvictableIdleTimeMillis = 1800000;
	@SerializedName("soft.min.evictable.idle.time.millis")
	private int softMinEvictableIdleTimeMillis = 1800000;
	@SerializedName("pool.class")
	private String poolClass = "clients.jedis.JedisPool";
	@SerializedName("eviction.policy.class")
	private String evictionPolicyClass = "org.apache.commons.pool2.impl.DefaultEvictionPolicy";
}
