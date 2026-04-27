package com.reneekbartlett.verisimilar.core.io;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ResourceValueLoader {

	private static Logger logger = LoggerFactory.getLogger(ResourceValueLoader.class);

	private ResourceValueLoader() {}

	public static String[] loadStringArray(String resourcePath) {
		List<String> vals;
		try {
			vals = readLines(resourcePath);
		} catch(Exception e) {
			logger.error("Error loading " + resourcePath +  " file", e);
			return new String[0];
		}
		return vals.toArray(new String[0]);
	}

	public static List<String> loadStringList(String resourcePath) {
		List<String> vals;
		try {
			vals = readLines(resourcePath);
		} catch(Exception e) {
			logger.error("Error loading " + resourcePath +  " file", e);
			return Collections.emptyList();
		}
		return Collections.unmodifiableList(vals);
	}

	private static List<String> readLines(String resourcePath) {
		List<String> rows = new ArrayList<>();
		try (
			InputStream is = ResourceMapLoader.class.getResourceAsStream(resourcePath);
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
		) {
			String line;
			//int i = 0;
			while ((line = reader.readLine()) != null) {
				if (line.isBlank() || line.startsWith("#")) {
					continue;
				}
				rows.add(line);
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to load resource: " + resourcePath, e);
		}
		return rows;
	}
}
