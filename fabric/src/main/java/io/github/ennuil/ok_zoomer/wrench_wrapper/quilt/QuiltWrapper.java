package io.github.ennuil.ok_zoomer.wrench_wrapper.quilt;

import io.github.ennuil.ok_zoomer.wrench_wrapper.WrenchWrapper;
import org.quiltmc.config.api.ReflectiveConfig;

import java.lang.reflect.InvocationTargetException;

public class QuiltWrapper {
	public static <C extends ReflectiveConfig> C create(String family, String id, Class<C> configCreatorClass) {
		var clazz = WrenchWrapper.getClass("org.quiltmc.loader.api.config.v2.QuiltConfig");
		if (clazz == null) return null;

		try {
			return (C) clazz.getMethod("create", String.class, String.class, Class.class).invoke(null, family, id, configCreatorClass);
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			return null;
		}
	}
}
