package com.github.zymen.springsessionrest.persistent;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import static org.apache.commons.lang3.StringUtils.removeStart;
import static org.apache.commons.lang3.StringUtils.startsWith;
import static org.springframework.util.Base64Utils.decodeFromString;
import static org.springframework.util.Base64Utils.encodeToString;
import static org.springframework.util.ClassUtils.isPrimitiveOrWrapper;
import static org.springframework.util.SerializationUtils.deserialize;
import static org.springframework.util.SerializationUtils.serialize;

public class Serializer {

    protected static final String SERIALIZED_OBJECT_PREFIX = "_$object=";

    public Map<String, Object> serializeSessionAttributes(Map<String, Object> attributes) {
        if (attributes == null) {
            return null;
        }
        Map<String, Object> serialized = new HashMap<>(attributes.size());
        for (Entry<String, Object> attribute : attributes.entrySet()) {
            if (isDeserializedObject(attribute.getValue())) {
                Object attributeValue = encodeToString(serialize(attribute.getValue()));
                serialized.put(attribute.getKey(), SERIALIZED_OBJECT_PREFIX + attributeValue);
            } else {
                serialized.put(attribute.getKey(), attribute.getValue());
            }
        }
        return serialized;
    }

    public Map<String, Object> deserializeSessionAttributes(Map<String, Object> attributes) {
        if (attributes == null) {
            return null;
        }
        Map<String, Object> deserialized = new HashMap<>(attributes.size());
        for (Entry<String, Object> attribute : attributes.entrySet()) {
            Object attributeValue = attribute.getValue();
            if (isSerializedObject(attribute.getValue())) {
                String content = removeStart(attribute.getValue().toString(), SERIALIZED_OBJECT_PREFIX);
                attributeValue = deserialize(decodeFromString(content));
            }
            deserialized.put(attribute.getKey(), attributeValue);
        }
        return deserialized;
    }

    protected boolean isDeserializedObject(Object attributeValue) {
        return attributeValue != null && !isPrimitiveOrWrapper(attributeValue.getClass()) && !(attributeValue instanceof String);
    }

    protected boolean isSerializedObject(Object attributeValue) {
        return attributeValue != null && attributeValue instanceof String && startsWith(attributeValue.toString(), SERIALIZED_OBJECT_PREFIX);
    }
}
