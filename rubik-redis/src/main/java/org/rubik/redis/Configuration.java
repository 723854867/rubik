package org.rubik.redis;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Set;

import org.rubik.bean.core.enums.ColStyle;
import org.rubik.redis.caller.HashCaller;
import org.rubik.redis.caller.LuaCaller;
import org.rubik.redis.caller.SortSetCaller;
import org.rubik.redis.caller.StringCaller;
import org.rubik.util.ConfigLoader;
import org.rubik.util.common.CollectionUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.util.Pool;

@Slf4j
@org.springframework.context.annotation.Configuration("redisConfiguration")
class Configuration {

	@Bean(name = "redisConfig")
	public RedisConfig config() {
		return ConfigLoader.load("classpath:conf/redis.properties").toBean(RedisConfig.class, ColStyle.camel2dot);
	}

	@Bean(name = "jedisPool")
	public Pool<Jedis> pool() {
		RedisConfig redisConfig = config();
		JedisPoolConfig config = new JedisPoolConfig();
		config.setBlockWhenExhausted(redisConfig.isBlockWhenExhausted());
		config.setEvictionPolicyClassName(redisConfig.getEvictionPolicyClass());
		// config.setFairness(fairness);
		config.setJmxEnabled(redisConfig.isJmxEnabled());
		// config.setJmxNameBase(jmxNameBase);
		// config.setJmxNamePrefix("");
		config.setLifo(redisConfig.isLifo());
		config.setMaxIdle(redisConfig.getMaxIdle());
		config.setMinIdle(redisConfig.getMinIdle());
		config.setMaxTotal(redisConfig.getMaxTotal());
		config.setMaxWaitMillis(redisConfig.getMaxWaitMillis());
		config.setMinEvictableIdleTimeMillis(redisConfig.getMinEvictableIdleTimeMillis());
		config.setNumTestsPerEvictionRun(redisConfig.getNumTestsPerEvictionRun());
		config.setSoftMinEvictableIdleTimeMillis(redisConfig.getSoftMinEvictableIdleTimeMillis());
		config.setTestOnBorrow(redisConfig.isTestOnBorrow());
		config.setTestOnCreate(redisConfig.isTestOnCreate());
		config.setTestOnReturn(redisConfig.isTestOnReturn());
		config.setTestWhileIdle(redisConfig.isTestWhileIdle());
		config.setTimeBetweenEvictionRunsMillis(redisConfig.getTimeBetweenEvictionRunsMillis());

		String poolName = redisConfig.getPoolClass();
		if (poolName.equals(ShardedJedisPool.class.getName()))
			throw new UnsupportedOperationException("Unsupported ShardedJedisPool");
		else if (poolName.equals(JedisSentinelPool.class.getName())) {
			String sentinels = redisConfig.getHost();
			Set<String> set = CollectionUtil.toSet(sentinels, ";");
			return new JedisSentinelPool(redisConfig.getMaster(), set, config, redisConfig.getConnTimeout(), redisConfig.getPassword());
		} else
			return new JedisPool(config, redisConfig.getHost(), redisConfig.getPort(), redisConfig.getConnTimeout(), redisConfig.getPassword(), redisConfig.getDatabase());
	}

	@Bean
	public Locker locker() {
		Locker locker = new Locker();
		locker.setLuaCaller(luaCaller());
		locker.setStringCaller(stringCaller());
		return locker;
	}
	
	@Bean
	public LuaCaller luaCaller() {
		LuaCaller caller = new LuaCaller(pool());
		log.info("Lua script initial start...");
		try {
			Resource[] resources = ConfigLoader.getResources("classpath*:conf/lua/*.lua");
			for (Resource resource : resources) {
				String url = resource.getURL().toString();
				log.debug("Loading lua script from {}...", url);
				int seperator = url.lastIndexOf("/");
				String key = url.substring(seperator + 1).replaceAll(".lua", "");
				InputStream in = resource.getInputStream();
				BufferedInputStream bin = new BufferedInputStream(in);
				if (in.available() <= 0) {
					log.warn("Lua script {} is empty, value will be ignored!", resource.getURL());
					continue;
				}
				byte[] buffer = new byte[in.available()];
				bin.read(buffer);
				caller.addScript(key.toLowerCase(), buffer);
			}
			log.info("Lua script initial finish, total {} records were loaded!", resources.length);
		} catch (Exception e) {
			log.error("Lua script load failure, system will closed!", e);
			System.exit(1);
		}
		return caller;
	}
	
	@Bean
	public HashCaller hashCaller() {
		return new HashCaller(pool());
	}
	
	@Bean
	public StringCaller stringCaller() {
		return new StringCaller(pool());
	}
	
	@Bean
	public SortSetCaller sortSetCaller() {
		return new SortSetCaller(pool());
	}
}
