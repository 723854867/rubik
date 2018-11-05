package org.rubik.util.serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

@SuppressWarnings("unchecked")
public class ProtostuffSerializer implements Serializer {

	@Override
	public byte[] serial(Serializable model) {
		return serialToByteArray(model);
	}

	@Override
	public <ENTITY extends Serializable> ENTITY deserial(byte[] data, Class<ENTITY> clazz) {
		return deserialFromByteArray(data, clazz);
	}
	
	public static final <T> byte[] serialToByteArray(T object) {
		Class<T> clazz = (Class<T>) object.getClass();
		Schema<T> schema = RuntimeSchema.getSchema(clazz);
		LinkedBuffer buffer = LinkedBuffer.allocate();
		try {
			return ProtostuffIOUtil.toByteArray(object, schema, buffer);
		} finally {
			buffer.clear();
		}
	}

	public static final <T> int serialToOutputStream(T object, OutputStream out) {
		Class<T> clazz = (Class<T>) object.getClass();
		Schema<T> schema = RuntimeSchema.getSchema(clazz);
		LinkedBuffer buffer = LinkedBuffer.allocate();
		try {
			return ProtostuffIOUtil.writeTo(out, object, schema, buffer);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			buffer.clear();
		}
	}

	public static final <T> T deserialFromByteArray(byte[] data, Class<T> clazz) {
		Schema<T> schema = RuntimeSchema.getSchema(clazz);
		T t = schema.newMessage();
		ProtostuffIOUtil.mergeFrom(data, t, schema);
		return t;
	}

	public static final <T> T deserialFromInputStream(InputStream input, Class<T> clazz) {
		Schema<T> schema = RuntimeSchema.getSchema(clazz);
		T t = schema.newMessage();
		try {
			ProtostuffIOUtil.mergeFrom(input, t, schema);
			return t;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
