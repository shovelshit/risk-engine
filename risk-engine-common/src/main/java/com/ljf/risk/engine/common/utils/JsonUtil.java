package com.ljf.risk.engine.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TimeZone;

/**
 * @author lijinfeng
 */
public class JsonUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        CustomDateFormat customDateFormat = new CustomDateFormat(MAPPER.getDateFormat());
        customDateFormat.setCalendar(Calendar.getInstance());
        customDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        MAPPER.setDateFormat(customDateFormat);
        MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static String objectToJson(Object data) {
        if (data == null) {
            return null;
        }

        try {
            return MAPPER.writeValueAsString(data);
        } catch (Exception e) {
            LOGGER.error("json convert exception", e);
        }
        return null;
    }

    public static <T> T jsonToBean(String jsonData, Class<T> beanType) {
        if (StringUtils.isEmpty(jsonData)) {
            return null;
        }

        try {
            return MAPPER.readValue(jsonData, beanType);
        } catch (Exception e) {
            LOGGER.error("json convert exception", e);
        }

        return null;
    }

    public static <T> T jsonToBeanByField(String jsonData, String field, Class<T> beanType) {
        if (StringUtils.isAllBlank(jsonData, field)) {
            return null;
        }

        try {
            Optional<JsonNode> jsonNode = Optional.ofNullable(stringToJsonNode(jsonData))
                    .flatMap(json -> Optional.ofNullable(getJsonNode(json, field)));
            return objectNodeToBean(jsonNode.orElse(null), beanType);
        } catch (Exception e) {
            LOGGER.error("json convert exception", e);
        }

        return null;
    }


    public static <T> Optional<T> jsonToOptionalBean(String jsonData, Class<T> beanType) {
        if (StringUtils.isEmpty(jsonData)) {
            return Optional.empty();
        }

        try {
            return Optional.ofNullable(MAPPER.readValue(jsonData, beanType));
        } catch (Exception e) {
            LOGGER.error("json convert exception", e);
        }
        return Optional.empty();
    }

    public static <T> List<T> jsonToList(String jsonData, Class<T> beanType) {
        if (StringUtils.isEmpty(jsonData)) {
            return null;
        }

        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);

        try {
            List<T> resultList = MAPPER.readValue(jsonData, javaType);
            return resultList;
        } catch (Exception e) {
            LOGGER.error("json convert exception", e);
        }

        return null;
    }

    public static <T> Set<T> jsonToSet(String jsonData, Class<T> beanType) {
        if (StringUtils.isEmpty(jsonData)) {
            return null;
        }

        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(HashSet.class, beanType);

        try {
            return MAPPER.readValue(jsonData, javaType);
        } catch (Exception e) {
            LOGGER.error("json convert exception", e);
        }

        return null;
    }

    public static <K, V> Map<K, V> jsonToMap(String jsonData, Class<K> keyType, Class<V> valueType) {
        if (StringUtils.isEmpty(jsonData)) {
            return null;
        }

        JavaType javaType = MAPPER.getTypeFactory().constructMapType(Map.class, keyType, valueType);

        try {
            Map<K, V> resultMap = MAPPER.readValue(jsonData, javaType);
            return resultMap;
        } catch (Exception e) {
            LOGGER.error("json convert exception", e);
        }

        return null;
    }

    public static ObjectNode stringToObjectNode(String jsonString) {
        if (StringUtils.isBlank(jsonString)) {
            return null;
        }
        try {
            return (ObjectNode) MAPPER.readTree(jsonString);
        } catch (Exception e) {
            LOGGER.error("json convert exception", e);
        }

        return null;
    }

    public static ArrayNode stringToArrayNode(String jsonString) {
        if (StringUtils.isBlank(jsonString)) {
            return null;
        }
        try {
            return (ArrayNode) MAPPER.readTree(jsonString);
        } catch (Exception e) {
            LOGGER.error("json convert exception", e);
        }

        return null;
    }

    public static JsonNode stringToJsonNode(String jsonString) {
        if (StringUtils.isBlank(jsonString)) {
            return null;
        }
        try {
            return MAPPER.readTree(jsonString);
        } catch (Exception e) {
            LOGGER.error("json convert exception", e);
        }

        return null;
    }

    public static ObjectMapper getMapper() {
        return MAPPER;
    }

    public static String getText(JsonNode jsonNode, String key) {
        if (jsonNode == null) {
            return null;
        }
        if (jsonNode.hasNonNull(key)) {
            JsonNode value = jsonNode.get(key);
            if (value instanceof ObjectNode) {
                return value.toString();
            } else if (value instanceof ArrayNode) {
                return value.toString();
            } else {
                return value.asText();
            }
        }
        return null;
    }

    public static String getText(JsonNode jsonNode, int index) {
        if (jsonNode == null) {
            return null;
        }
        if (jsonNode.hasNonNull(index)) {
            JsonNode value = jsonNode.get(index);
            if (value instanceof ObjectNode) {
                return value.toString();
            } else if (value instanceof ArrayNode) {
                return value.toString();
            } else {
                return value.asText();
            }
        }
        return null;
    }

    public static Long getLong(JsonNode jsonNode, String key) {
        if (jsonNode == null) {
            return null;
        }
        if (jsonNode.hasNonNull(key)) {
            return jsonNode.get(key).asLong();
        }
        return null;
    }

    public static Integer getInteger(JsonNode jsonNode, String key) {
        if (jsonNode == null) {
            return null;
        }
        if (jsonNode.hasNonNull(key)) {
            return jsonNode.get(key).asInt();
        }
        return null;
    }

    public static Boolean getBoolean(JsonNode jsonNode, String key) {
        if (jsonNode == null) {
            return null;
        }
        if (jsonNode.hasNonNull(key)) {
            return jsonNode.get(key).asBoolean();
        }
        return null;
    }

    public static JsonNode getJsonNode(JsonNode jsonNode, String key) {
        if (jsonNode == null) {
            return null;
        }
        if (jsonNode.hasNonNull(key)) {
            return jsonNode.get(key);
        }
        return null;
    }

    public static ArrayNode getArrayNode(JsonNode jsonNode, String key) {
        if (jsonNode == null) {
            return null;
        }
        if (jsonNode.hasNonNull(key)) {
            return (ArrayNode) jsonNode.get(key);
        }
        return null;
    }

    public static ObjectNode getObjectNode(JsonNode jsonNode, String key) {
        if (jsonNode == null) {
            return null;
        }
        if (jsonNode.hasNonNull(key)) {
            return (ObjectNode) jsonNode.get(key);
        }
        return null;
    }

    public static <T> T objectNodeToBean(JsonNode jsonNode, Class<T> beanType) {
        if (jsonNode == null) {
            return null;
        }
        if (jsonNode instanceof ObjectNode) {
            try {
                return MAPPER.treeToValue(jsonNode, beanType);
            } catch (Exception e) {
                LOGGER.error("json convert exception", e);
            }
        }
        return null;
    }

    public static String getJsonNodeValue(JsonNode node, String attrs) {
        int index = attrs.indexOf('.');
        if (index == -1) {
            return getText(node, attrs);
        } else {
            String s1 = attrs.substring(0, index);
            String s2 = attrs.substring(index + 1);
            return getJsonNodeValue(getJsonNode(node, s1), s2);
        }
    }

    public static void setJsonNodeValue(JsonNode node, String attrs, JsonNode data) {
        int index = attrs.indexOf('.');
        if (index == -1) {
            ObjectNode node1 = (ObjectNode) node;
            node1.set(attrs, data);
        } else {
            String s1 = attrs.substring(0, index);
            String s2 = attrs.substring(index + 1);
            setJsonNodeValue(getJsonNode(node, s1), s2, data);
        }
    }

}
