package openjchart.io;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

public abstract class AbstractWriterFactory<T> implements WriterFactory<T> {
	protected final Map<String, Class<? extends T>> writers = new TreeMap<String, Class<? extends T>>();

	protected AbstractWriterFactory(String propFileName) {
		// Retrieve property-files
		Enumeration<URL> propFiles = null;
		try {
			propFiles = getClass().getClassLoader().getResources(propFileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (propFiles != null) {
			Properties props = new Properties();
			while (propFiles.hasMoreElements()) {
				URL propURL = propFiles.nextElement();
				InputStream stream = null;
				try {
					stream = propURL.openStream();
					props.load(stream);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				} finally {
					if (stream != null) {
						try {
							stream.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				// Parse property files and register entries as writers
				for (Map.Entry<Object, Object> prop : props.entrySet()) {
					String mimeType = (String) prop.getKey();
					String className = (String) prop.getValue();
					Class<?> clazz = null;
					try {
						clazz = Class.forName(className);
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						continue;
					}
					// FIXME: Missing type safety check
					writers.put(mimeType, (Class<? extends T>) clazz);
				}
			}
		}
	}

	@Override
	public WriterCapabilities getCapabilities(String mimeType) {
		Class<? extends T> clazz = writers.get(mimeType);
		try {
			Method capabilitiesGetter = clazz.getMethod("getCapabilities");
			Set<WriterCapabilities> capabilities = (Set<WriterCapabilities>) capabilitiesGetter.invoke(clazz);
			for (WriterCapabilities c : capabilities) {
				if (c.getMimeType().equals(mimeType)) {
					return c;
				}
			}
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public WriterCapabilities[] getCapabilities() {
		WriterCapabilities[] caps = new WriterCapabilities[writers.size()];
		int i=0;
		for (String mimeType : writers.keySet()) {
			caps[i++] = getCapabilities(mimeType);
		}
		return caps;
	}

	@Override
	public String[] getSupportedFormats() {
		String[] formats = new String[writers.size()];
		writers.keySet().toArray(formats);
		return formats;
	}

	@Override
	public boolean isFormatSupported(String mimeType) {
		return writers.containsKey(mimeType);
	}
}