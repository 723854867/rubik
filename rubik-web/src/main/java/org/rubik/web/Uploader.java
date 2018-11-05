package org.rubik.web;

import java.io.File;
import java.io.IOException;

import org.rubik.bean.core.Constants;
import org.rubik.bean.core.enums.Env;
import org.rubik.bean.core.exception.AssertException;
import org.rubik.bean.core.model.Code;
import org.rubik.core.Rubik;
import org.rubik.soa.config.api.RubikConfigService;
import org.rubik.util.common.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class Uploader {
	
	private static final Logger logger = LoggerFactory.getLogger(Uploader.class);
	
	@javax.annotation.Resource
	private RubikConfigService rubikConfigService;
	
	public String resourceDirectory() {
		Env env = Rubik.env();
		switch (env) {
		case LOCAL:
			return WebUtil.request().getServletContext().getRealPath("/");
		default:
			String path = rubikConfigService.config(Constants.RESOURCE_DIRECTORY);
			return StringUtil.hasText(path) ? path : WebUtil.request().getServletContext().getRealPath("/");
		}
	}
	
	public String save(MultipartFile file, String directory, String category, String newFileName) {
		return save(file, directory, category, newFileName, null);
	}
	
	private String save(MultipartFile file, String directory, String category, String newFileName, String oldFileName) {
		File dir = new File(directory + category);
		if (!dir.exists())
			dir.mkdirs();
		String fileName = file.getOriginalFilename();
		String ext = fileName.substring(fileName.lastIndexOf("."));
		File dest = new File(dir.getPath() + File.separator + newFileName + ext);
		try {
			file.transferTo(dest);
		} catch (IllegalStateException | IOException e) {
			logger.error("资源 - {} 上传失败！", dest.getPath(), e);
			throw AssertException.error(Code.UPLOAD_FAILURE, e);
		}
		if (StringUtil.hasText(oldFileName)) {			// 删除老文件
			File old = new File(directory + File.separator + oldFileName);
			if (old.exists())
				old.delete();
		}
		return category + "/" + newFileName + ext;
	}
}
