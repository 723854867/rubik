package org.rubik.mybatis.extension;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.rubik.util.common.StringUtil;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;

public class DaoScanner extends MapperScannerConfigurer {

	private DaoAccessor daoAccessor;

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
		super.postProcessBeanDefinitionRegistry(registry);
		daoAccessor.registerDao(Dao.class);
		String[] names = registry.getBeanDefinitionNames();
		GenericBeanDefinition definition;
		for (String name : names) {
			BeanDefinition beanDefinition = registry.getBeanDefinition(name);
			if (beanDefinition instanceof GenericBeanDefinition) {
				definition = (GenericBeanDefinition) beanDefinition;
				String clazz = definition.getBeanClassName();
				if (StringUtil.hasText(clazz) && clazz.equals("org.mybatis.spring.mapper.MapperFactoryBean")) {
					definition.setBeanClass(DaoFactoryBean.class);
					definition.getPropertyValues().add("daoAccessor", this.daoAccessor);
				}
			}
		}
	}
	
	public void setDaoAccessor(DaoAccessor daoAccessor) {
		this.daoAccessor = daoAccessor;
	}
}
