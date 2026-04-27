package com.reneekbartlett.verisimilar.core.model;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.JsonNodeFactory;

public record ValueCount(String value, int count){

    public JsonNode toJsonNode() {
        return JsonNodeFactory.instance.objectNode().put(value, count);
    }

    @Override
    public String toString() {
        return new StringBuilder().append(this.value).append(": ").append(this.count).toString();
    }
}
