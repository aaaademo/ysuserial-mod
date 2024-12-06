package org.su18.ysuserial.payloads.util;

import com.bes.ejb.spark.tcp.marshal.SparkJavaMarshaller;



public class BESSerialize {
    public static final byte[] PROTOCOL = new byte[]{83, 112, 97, 114, 107};
    private static byte major = 0;
    private static byte minor = 0;


    public static byte[] serialize(Object o) throws Exception {
        SparkJavaMarshaller sparkJavaMarshaller = new SparkJavaMarshaller();
        byte[] responseBody = sparkJavaMarshaller.marshal(o);
        byte[] responsebytes = new byte[18 + responseBody.length];
        byte[] header = getHeader(100, responseBody.length);
        System.arraycopy(header, 0, responsebytes, 0, 18);
        System.arraycopy(responseBody, 0, responsebytes, 18, responseBody.length);

        return responsebytes;
    }

    public static byte[] getHeader(int requestId, int length) {
        byte[] header = new byte[18];
        fillHeader(requestId, length, header);
        return header;
    }

    public static void fillHeader(int requestId, int length, byte[] message) {
        System.arraycopy(PROTOCOL, 0, message, 0, PROTOCOL.length);
        message[5] = toByteValue();
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
    public static byte toByteValue() {
        return (byte)(major << 4 | minor);
    }
}
