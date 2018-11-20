package org.rubik.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.rubik.bean.core.enums.ColStyle;
import org.rubik.bean.core.enums.Env;
import org.rubik.bean.core.enums.Locale;
import org.rubik.core.hook.DisposableHook;
import org.rubik.core.hook.Hook;
import org.rubik.core.hook.InitialHook;
import org.rubik.util.ConfigLoader;
import org.rubik.util.SpringContextUtil;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.stereotype.Component;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Rubik implements ApplicationListener<ApplicationContextEvent> {
	
	private boolean initial;
	private static Config config;
	
	static {
		config = ConfigLoader.load("classpath*:conf/rubik.properties").toBean(Config.class, ColStyle.camel2dot);
	}
	
	@Override
	public void onApplicationEvent(ApplicationContextEvent event) {
		if (event instanceof ContextClosedEvent) {
			if (initial)
				_dispose();
		} else if (event instanceof ContextRefreshedEvent) {
			log.info("spring container refreshed!");
			if (!initial)
				_initial();
		} else if (event instanceof ContextStartedEvent) {
			log.info("spring container started!");
			if (!initial)
				_initial();
		} else if (event instanceof ContextStoppedEvent) {
			if (initial)
				_dispose();
		} else 
			log.error("Receive unrecognizable spring event {}!", event);
	}
	
	private synchronized void _initial() {
		if (initial)
			return;
		this.initial = true;
		log.info("spring container initialize success, start initialize application...");
		long start = System.nanoTime();
		Map<String, InitialHook> hooks = SpringContextUtil.getApplicationContext().getBeansOfType(InitialHook.class, false, true);
		List<Hook> list = new ArrayList<Hook>(hooks.values());
		Collections.sort(list, (o1, o2) -> o1.priority() - o2.priority());
		for (Hook hook : list) {
			try {
				hook.invoke();
			} catch (Exception e) {
				log.info("application hook initialize failure, system will closed!", e);
				System.exit(1);
			}
		}
		long end = System.nanoTime();
		log.info("application initial success in {} seconds!", TimeUnit.NANOSECONDS.toSeconds(end - start));
	}
	
	private void _dispose() {
		if (!initial)
			return;
		this.initial = false;
		log.info("start stop application...");
		long start = System.nanoTime();
		Map<String, DisposableHook> hooks = SpringContextUtil.getApplicationContext().getBeansOfType(DisposableHook.class, false, true);
		List<Hook> list = new ArrayList<Hook>(hooks.values());
		Collections.sort(list, (o1, o2) -> o1.priority() - o2.priority());
		for (Hook hook : list) {
			try {
				hook.invoke();
			} catch (Exception e) {
				log.info("application hook dispose failure, system will closed!", e);
			}
		}
		long end = System.nanoTime();
		log.info("application stop success in {} seconds!", TimeUnit.NANOSECONDS.toSeconds(end - start));
	}
	
	public static final Env env() {
		return config.env;
	}
	
	public static final Locale locale() {
		return config.locale;
	}
	
	public static final Config config() {
		return config;
	}
	
	@Getter
	@Setter
	public static class Config implements Serializable {
		private static final long serialVersionUID = 2984065777984752966L;
		// 本地环境
		private Env env = Env.LOCAL;
		// 语言
		private Locale locale = Locale.ZH_CN;
		// 上传文件最大总字节数
		@SerializedName("upload.maximum")
		private long uploadMaximum = 5242880l;
		// 单个文件上传最大字节数
		@SerializedName("upload.maximum.per.file")
		private long uploadMaximumPerFile = 512000;
		// 内存中能存放的最字节数
		@SerializedName("upload.maximum.in.memory")
		private int uploadMaximumInMemory = 10485760;
	}
}
