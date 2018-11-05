package org.rubik.redis;

public interface RedisConsts {

	final String OK							= "OK";

	enum NXXX {
		NX,
		XX;
	}
	
	enum EXPX {
		EX,
		PX;
	}
}
