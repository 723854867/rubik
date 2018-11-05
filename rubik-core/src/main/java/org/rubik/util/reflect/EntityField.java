package org.rubik.util.reflect;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import lombok.NonNull;

public class EntityField {

	// 属性名
	private String name;
	// 属性字段
	private Field field;
	// 属性setter方法
	private Method setter;
	// 属性getter方法
	private Method getter;
	// 属性类型
	private Class<?> javaType;
	
	public EntityField(@NonNull Field field) {
		this.field = field;
		this.name = field.getName();
		this.javaType = field.getType();
	}
	
	public EntityField(@NonNull PropertyDescriptor propertyDescriptor) {
		this.name = propertyDescriptor.getName();
		this.setter = propertyDescriptor.getWriteMethod();
		this.getter = propertyDescriptor.getReadMethod();
		this.javaType = propertyDescriptor.getPropertyType();
	}
	
	/**
	 * 先创建field，然后可以通过该方法获取property等属性
	 */
	public void copyFromPropertyDescriptor(EntityField other) {
		this.name = other.name;
		this.field = other.field;
		this.setter = other.setter;
		this.getter = other.getter;
		this.javaType = other.javaType;
	}

	/**
	 * 是否有该注解
	 *
	 * @param annotationClass
	 * @return
	 */
	public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
		boolean result = false;
		if (field != null)
			result = field.isAnnotationPresent(annotationClass);
		if (!result && setter != null)
			result = setter.isAnnotationPresent(annotationClass);
		if (!result && getter != null)
			result = getter.isAnnotationPresent(annotationClass);
		return result;
	}

	/**
	 * 获取指定的注解
	 *
	 * @param annotationClass
	 * @param <T>
	 * @return
	 */
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		T result = null;
		if (field != null) 
			result = field.getAnnotation(annotationClass);
		if (result == null && setter != null) 
			result = setter.getAnnotation(annotationClass);
		if (result == null && getter != null) 
			result = getter.getAnnotation(annotationClass);
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		EntityField that = (EntityField) o;
		return !(name != null ? !name.equals(that.name) : that.name != null);
	}

	@Override
	public int hashCode() {
		return name != null ? name.hashCode() : 0;
	}
	
	/**
	 * 字段属性名
	 */
	public String getName() {
		return name;
	}

	/**
	 * 获取javaType
	 */
	public Class<?> getJavaType() {
		return javaType;
	}

	/**
	 * 设置javaType
	 */
	public void setJavaType(Class<?> javaType) {
		this.javaType = javaType;
	}
}
