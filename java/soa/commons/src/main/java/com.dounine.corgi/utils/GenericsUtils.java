package com.dounine.corgi.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Hello world!
 *
 */
public final class GenericsUtils {
	/**
	 * 根据反射,获得定义Class时声明的父类的范型参数的类型
	 * @param clazz 本类class
	 * @return 超类类型
     */
	public static Class getSuperClassGenricType(Class<?> clazz) {
		return getSuperClassGenricType(clazz, 0);
	}

	/**
	 * 根据反射,获得定义Class时声明的父类的范型参数的类型
	 * @param clazz 本类class
	 * @param index 获取的参数索引
	 * @return 超类类型
	 * @throws IndexOutOfBoundsException 数组下标越界
     */
	public static Class<?> getSuperClassGenricType(Class<?> clazz, int index) throws IndexOutOfBoundsException {
		Type genType = clazz.getGenericSuperclass();
		if (!(genType instanceof ParameterizedType)) {
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		Class coz = null;
		if (index >= params.length || index < 0) {
			coz = Object.class;
		}else if (!(params[index] instanceof Class)) {
			coz = Object.class;
		}
		coz = (Class<?>) params[index];
		return coz;
	}
}
