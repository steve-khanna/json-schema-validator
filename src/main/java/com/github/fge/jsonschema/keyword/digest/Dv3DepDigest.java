package com.github.fge.jsonschema.keyword.digest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.fge.jackson.JacksonUtils;
import com.github.fge.jackson.NodeType;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedSet;

public class Dv3DepDigest extends AbstractDigester {

    private static final Digester INSTANCE = new Dv3DepDigest();

    private Dv3DepDigest() {
        super("dependencies", NodeType.OBJECT);
    }

    public static Digester getInstance() {
        return INSTANCE;
    }

    private static JsonNode v3sortedSet(final JsonNode node) {
        final SortedSet<JsonNode> set
                = Sets.newTreeSet(new Comparator<JsonNode>() {
            @Override
            public int compare(final JsonNode o1, final JsonNode o2) {
                return o1.textValue().compareTo(o2.textValue());
            }
        });

        set.addAll(Sets.newHashSet(node));
        final ArrayNode ret = FACTORY.arrayNode();
        ret.addAll(set);
        return ret;
    }

    @Override
    public JsonNode digest(JsonNode schema) {
        super.digestFactory();

        final Map<String, JsonNode> map
                = JacksonUtils.asMap(schema.get(keyword));

        String key;
        JsonNode value;
        NodeType type;

        for (final Map.Entry<String, JsonNode> entry : map.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
            type = NodeType.getNodeType(value);
            switch (type) {
                case OBJECT:
                    list.add(key);
                    break;
                case ARRAY:
                    final JsonNode node = v3sortedSet(value);
                    if (node.size() != 0)
                        propertyDeps.set(key, node);
                    break;
                case STRING:
                    propertyDeps.set(key, FACTORY.arrayNode()
                            .add(value.textValue()));
                    break;
                default:
                    break;
            }
        }

        for (final String s : Ordering.natural().sortedCopy(list))
            schemaDeps.add(s);

        return ret;
    }

}
