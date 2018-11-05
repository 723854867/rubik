package org.rubik.bean.core.model;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Code implements Serializable {

	private static final long serialVersionUID = 565566251340718556L;
	
	private static final Map<String, Code> codes = new ConcurrentHashMap<String, Code>();
	
	// 成功
	public static final Code OK							= Code.create("code.ok");
	// 非法访问
	public static final Code FORBID						= Code.create("code.forbid");
	// 未登录
	public static final Code UNLOGIN					= Code.create("code.unlogin");
	// 系统错误
	public static final Code SYS_ERR					= Code.create("code.sys.err");
	// 参数错误
	public static final Code PARAM_ERR					= Code.create("code.param.err");
	// 用户名已存在
	public static final Code UNAME_EXIST				= Code.create("code.uname.exist");
	// 操作太频繁
	public static final Code USER_LOCKED				= Code.create("code.user.locked");
	// 无效的token
	public static final Code TOKEN_INVALID				= Code.create("code.token.invalid");
	// 上传失败
	public static final Code UPLOAD_FAILURE				= Code.create("code.upload.failure");
	// 用户不存在
	public static final Code USER_NOT_EIXST				= Code.create("code.user.not.exist");
	// 语言配置不存在
	public static final Code WORD_NOT_EXIST				= Code.create("code.word.not.exist");
	// 系统配置不存在
	public static final Code CONFIG_NOT_EXIST			= Code.create("code.config.not.exist");
	// sdk调用错误
	public static final Code SDK_INVOKE_FAILURE			= Code.create("code.sdk.invoke.failure");
	// 上传文件太大
	public static final Code UPLOAD_SIZE_EXCEEDED		= Code.create("code.upload.size.exceeded");
	// 不支持的 HTTP 请求方法
	public static final Code UNSUPPORT_HTTP_METHOD		= Code.create("code.unsupport.http.method");
	// 不支持的ContentType
	public static final Code UNSUPPORT_CONTENT_TYPE		= Code.create("code.unsupport.content.type");

	private String key;
	
	private Code(String key) {
		this.key = key;
	}
	
	public String key() {
		return key;
	}
	
	public static final Code create(String key) {  
		Code code = new Code(key);
		if (null != codes.putIfAbsent(key, code))
			throw new RuntimeException("code [" + key + "] has duplicated!");
		return code;
	}
}
