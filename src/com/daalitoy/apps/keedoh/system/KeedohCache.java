package com.daalitoy.apps.keedoh.system;

import java.io.File;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class KeedohCache {

	private static Cache<String, Object> cache = null;
	// private static CacheLoader cacheLoader=CacheLoader.
	static {
		cache = CacheBuilder.newBuilder().maximumSize(100).initialCapacity(10)
				.build();
	}

	public static void cache(String objName, Object obj) {
		cache.put(objName, obj);

	}

	public static Object get(String objName) {
		return (cache.getIfPresent(objName));
	}

	public static File getRecentDirectory() {
		return (File) (cache.getIfPresent("keedoh_last_dir"));
	}

	public static void setRecentDirectory(File file) {
		cache("keedoh_last_dir", file);
	}

}
