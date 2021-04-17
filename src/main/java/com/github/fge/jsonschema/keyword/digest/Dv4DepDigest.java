package com.github.fge.jsonschema.keyword.digest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.fge.jackson.JacksonUtils;
import com.github.fge.jackson.NodeType;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Dv4DepDigest extends AbstractDigester {

    private static final Digester INSTANCE = new Dv4DepDigest();

    private Dv4DepDigest() {
        super("dependencies", NodeType.OBJECT);
    }

    public static Digester getInstance() {
        return INSTANCE;
    }

    private static JsonNode v4sortedSet(final JsonNode node) {
        final List<JsonNode> list = Lists.newArrayList(node);

        Collections.sort(list, new Comparator<JsonNode>() {
            @Override
            public int compare(final JsonNode o1, final JsonNode o2) {
                return o1.textValue().compareTo(o2.textValue());
            }
        });

        final ArrayNode ret = FACTORY.arrayNode();
        ret.addAll(list);
        return ret;
    }

    @Override
    public JsonNode digest(JsonNode schema) {
        super.digestFactory();

        final Map<String, JsonNode> map
                = JacksonUtils.asMap(schema.get(keyword));


        String key;
        JsonNode value;

        for (final Map.Entry<String, JsonNode> entry : map.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
            if (value.isObject()) // schema dep
                list.add(key);
            else // property dep
                propertyDeps.set(key, v4sortedSet(value));
        }

        for (final String s : Ordering.natural().sortedCopy(list))
            schemaDeps.add(s);

        return ret;
    }
}
