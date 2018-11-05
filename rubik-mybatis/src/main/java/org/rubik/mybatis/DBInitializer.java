package org.rubik.mybatis;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.rubik.bean.core.enums.ColStyle;
import org.rubik.mybatis.extension.DBConfig;
import org.rubik.mybatis.extension.handler.AutoEnumTypeHandler;
import org.rubik.util.ConfigLoader;
import org.rubik.util.common.CollectionUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.github.pagehelper.PageInterceptor;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
public class DBInitializer {
	
	@Bean
	public static DBConfig dbConfig() {
		return ConfigLoader.load("classpath*:conf/db.properties").toBean(DBConfig.class, ColStyle.camel2dot);
	}
	
	/**
	 * 初始化数据源：默认使用HikariCP
	 * 
	 * @return
	 * @throws Exception
	 */
	@Bean(name = "dataSource", destroyMethod = "close")
	public DataSource datasource() throws Exception {
		DBConfig dbConfig = dbConfig();
		String dataSourceClass = dbConfig.getDatasourceClass();
		Class.forName(dataSourceClass);
		if (dataSourceClass.equals("com.zaxxer.hikari.HikariDataSource")) 
			return _hikariCP();
		else 
			throw new RuntimeException("Unsupport dataSource type: " + dataSourceClass);
	}
	
	/**
	 * HikariCP连接池初始化
	 * 
	 * @return
	 */
	private DataSource _hikariCP() {
		DBConfig dbConfig = dbConfig();
		HikariConfig config = new HikariConfig();
		config.setDriverClassName(dbConfig.getDriverClass());
		config.setJdbcUrl(dbConfig.getJdbc());
		config.setUsername(dbConfig.getUsername());
		config.setPassword(dbConfig.getPassword());
		// 连接池中允许的最大连接数。常见的错误是设置一个太大的值，连接数多反而性能下降。参考计算公式是：connections = ((cpu * 2) + 硬盘数)
		config.setMaximumPoolSize(dbConfig.getMaxPoolSize());
		// 连接池空闲连接的最小数量，当连接池空闲连接少于minimumIdle，而且总共连接数不大于maximumPoolSize时，HikariCP会尽力补充新的连接。为了性能考虑，不建议设置此值，而是让HikariCP把连接池当做固定大小的处理，
		// 默认minimumIdle与maximumPoolSize一样。当minIdle<0或者minIdle>maxPoolSize,则被重置为maxPoolSize，该值默认为10。
		config.setMinimumIdle(dbConfig.getMinIdle());
		// 一个连接的生命时长（毫秒），超时而且没被使用则被释放。强烈建议设置比数据库超时时长少30秒，（MySQL的wait_timeout参数，show variables like ‘%timeout%’，一般为8小时）
		// 和IdleTimeout的区别是不管连接池链接数量多少，只要一个连接超过该时间没被使用就会被回收
		config.setMaxLifetime(dbConfig.getMaxLifeTime());
		// 如果idleTimeout+1秒>maxLifetime 且 maxLifetime>0，则会被重置为0。如果idleTimeout=0则表示空闲的连接在连接池中永远不被移除。
		// 只有当minimumIdle小于maximumPoolSize时，这个参数才生效，当空闲连接数超过minimumIdle且空闲时间超过idleTimeout，才会被移除。
		config.setIdleTimeout(dbConfig.getIdleTimeout());
		// 等待连接池分配连接的最大时长（毫秒），超过这个时长还没可用的连接则发生SQLException。 缺省:30秒
		config.setConnectionTimeout(dbConfig.getConnTimeout());
		config.addDataSourceProperty("cachePrepStmts", dbConfig.isCachePrepStmts());
		config.addDataSourceProperty("prepStmtCacheSize", dbConfig.getPrepStmtCacheSize());
		config.addDataSourceProperty("prepStmtCacheSqlLimit", dbConfig.getPrepStmtCacheSqlLimit());
		return new HikariDataSource(config);
	}
	
	/**
	 * 事务管理器初始化
	 * 
	 * @return
	 * @throws Exception
	 */
	@Bean
	public PlatformTransactionManager txManager() throws Exception {
		return new DataSourceTransactionManager(datasource());
	}
	
	/**
	 * SqlSessionFactory 初始化
	 * 
	 * @return
	 * @throws Exception
	 */
	@Bean("sqlSessionFactory")
	public SqlSessionFactoryBean sessionFactory() throws Exception {
		DBConfig dbConfig = dbConfig();
		SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
		factory.setDataSource(datasource());
		factory.setMapperLocations(ConfigLoader.getResources(dbConfig.getMapperLocation()));
		if (!CollectionUtil.isEmpty(dbConfig.getTypeAliasesPackage()))
			factory.setTypeAliasesPackage(CollectionUtil.toString(dbConfig.getTypeAliasesPackage(), ";"));
		Set<Interceptor> interceptors = new HashSet<Interceptor>();
		if (dbConfig.isPagehelperEnable()) {
			PageInterceptor interceptor = new PageInterceptor();
			interceptor.setProperties(new Properties());
			interceptors.add(interceptor);
		}
		if (!CollectionUtil.isEmpty(interceptors))
			factory.setPlugins(interceptors.toArray(new Interceptor[] {}));
		org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
		configuration.setCacheEnabled(dbConfig.isCacheEnabled());
		configuration.setMapUnderscoreToCamelCase(dbConfig.isCamel2underline());
		configuration.getTypeHandlerRegistry().setDefaultEnumTypeHandler(AutoEnumTypeHandler.class);
		factory.setConfiguration(configuration);
		return factory;
	}
	
	/**
	 * SqlSession初始化
	 * 
	 * @param sqlSessionFactory
	 * @return
	 */
	@Bean("sqlSession")
	public SqlSession sqlSession(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}
