package org.rubik.web;

import javax.validation.Validator;

import org.hibernate.validator.HibernateValidator;
import org.rubik.core.Rubik;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
@EnableAspectJAutoProxy
public class WebConfig {
	
	@Bean("validator")
	public Validator validator() {
		LocalValidatorFactoryBean factory = new LocalValidatorFactoryBean();
		factory.setProviderClass(HibernateValidator.class);
		factory.setValidationMessageSource(messageSource());
		return factory;
	}
	
	@Bean("messageSource")
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		String langFile = "classpath:conf/lang/lang_" + Rubik.locale().mark();
		messageSource.setBasenames(new String[] {langFile, "classpath:org/hibernate/validator/ValidationMessages"});
		messageSource.setUseCodeAsDefaultMessage(false);
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setCacheSeconds(60);
		return messageSource;
	}
	
	@Bean
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		resolver.setDefaultEncoding("UTF-8");
		resolver.setMaxUploadSize(Rubik.config().getUploadMaximum());
		resolver.setMaxInMemorySize(Rubik.config().getUploadMaximumInMemory());
		resolver.setMaxUploadSizePerFile(Rubik.config().getUploadMaximumPerFile());
		return resolver;
	}
}
