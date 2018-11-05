package org.rubik.mongo;

import org.rubik.bean.core.enums.ColStyle;
import org.rubik.util.ConfigLoader;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration("mongoConfiguration")
class Configuration {

	@Bean(initMethod = "init", destroyMethod = "dispose")
	public Mongo mongo() {
		MongoConfig config = ConfigLoader.load("classpath:conf/mongo.properties").toBean(MongoConfig.class, ColStyle.camel2dot);
		Mongo mongo = new Mongo();
		mongo.setConfig(config);
		return mongo;
	}
}
