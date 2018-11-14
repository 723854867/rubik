package org.rubik.soa.authority.bean;

import org.rubik.bean.core.model.Code;

public interface AuthCode {
	
	final Code AUTH_FAIL 					= Code.create("code.auth.fail");
	final Code API_EXIST 					= Code.create("code.api.exist");
	final Code API_NOT_EXIST 				= Code.create("code.api.not.exist");
	final Code ROLE_NOT_EXIST 				= Code.create("code.role.not.exist");
	final Code MODULAR_NOT_EXIST 			= Code.create("code.modular.not.exist");
	final Code UNLOGIN_API_NO_AUTH			= Code.create("code.api.unlogin.no.auth");
}
