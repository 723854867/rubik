package org.rubik.mybatis;

import org.rubik.mybatis.entity.DBEntityFactory;
import org.rubik.mybatis.extension.DBConfig;
import org.rubik.mybatis.extension.DaoAccessor;
import org.rubik.mybatis.extension.DaoScanner;
import org.rubik.util.common.CollectionUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ScannerConfiguration {

	/**
	 * 该 bean 不能和 @EnableTransactionManagement 一起使用，因为该 bean 是一个spring的生命周期bean
	 * 有特殊的初始化
	 * @return
	 */
	@Bean
	public static DaoScanner daoScanner(DBConfig dbConfig) {
		DaoScanner scanner = new DaoScanner();
		DBEntityFactory entityFactory = new DBEntityFactory(dbConfig.getDialect());
		DaoAccessor daoAccessor = new DaoAccessor();
		daoAccessor.setEntityFactory(entityFactory);
		scanner.setDaoAccessor(daoAccessor);
		scanner.setSqlSessionFactoryBeanName("sqlSessionFactory");
		if (!CollectionUtil.isEmpty(dbConfig.getBasePackage()))
			scanner.setBasePackage(CollectionUtil.toString(dbConfig.getBasePackage(), ";"));
		return scanner;
	}
}
