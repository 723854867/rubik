package org.rubik.util.serializer;

import java.io.Serializable;

public interface Serializer {
	
	byte[] serial(Serializable entity);

	<ENTITY extends Serializable> ENTITY deserial(byte[] data, Class<ENTITY> entity);
}
