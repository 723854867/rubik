package org.rubik.bean.core;

import java.nio.charset.Charset;

import org.rubik.bean.core.model.ConfigKey;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

public interface Constants {

	final Charset UTF_8										= Charset.forName("UTF-8");
	final ConfigurableConversionService CONVERTER 			= new DefaultConversionService();
	// 资源访问路径
	final ConfigKey<String> RESOURCE_URL					= ConfigKey.<String>create("resource_url", String.class);
	// 资源文件路径(只对测试和线上环境有效，本地无效，如果没配置则默认存放本地)
	final ConfigKey<String> RESOURCE_DIRECTORY				= ConfigKey.<String>create("resource_directory", "", String.class);
	// 设备下次检测日期阈值(单位天)
	final ConfigKey<Integer> EXAMINE_THRESHOLD_DAY			= ConfigKey.<Integer>create("examine_threshold_day", 30, Integer.class);
	// 单个维保记录选择设备个数限制
	final ConfigKey<Integer> DEVICE_MAXIMUM_REPAIR			= ConfigKey.<Integer>create("device_maximum_repair", 20, Integer.class);
	// 维保记录上传资源个数限制
	final ConfigKey<Integer> RESOURCE_MAXIMUM_REPAIR		= ConfigKey.<Integer>create("resource_maximum_repair", 5, Integer.class);
	// 自查自纠上传资源个数限制
	final ConfigKey<Integer> RESOURCE_MAXIMUM_INTROSPECT	= ConfigKey.<Integer>create("resource_maximum_introspect", 5, Integer.class);
	// 作业人员证书有效期阈值(单位天)
	final ConfigKey<Integer> OPERATOR_CERT_THRESHOLD_DAY	= ConfigKey.<Integer>create("operator_cert_threshold_day", 30, Integer.class);
}
