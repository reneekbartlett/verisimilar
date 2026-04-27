// package com.reneekbartlett.verisimilar.core.model;

// import java.util.Arrays;

// import tools.jackson.databind.node.ArrayNode;
// import tools.jackson.databind.node.JsonNodeFactory;
// import tools.jackson.databind.node.ObjectNode;

// public record ValueData<T1, T2>(String columnName, ValueCount[] valueCounts){

//     public ArrayNode toJsonNode() {
//         ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode(valueCounts.length);
//         Arrays.asList(valueCounts).forEach(vc -> {
//             ObjectNode node = JsonNodeFactory.instance.objectNode();
//             node.put(columnName, vc.value());
//             node.put("count", vc.count());
//             arrayNode.add(node);
//         });
//         return arrayNode;
//     }

//     @Override
//     public String toString() {
//         //[{"category": "A", "value": 10}, {"category": "B", "value": 20}]
//         StringBuilder sb = new StringBuilder();
//         sb.append("[");
//         int i = 1;
//         for(ValueCount columnCount : valueCounts) {
//             sb.append(columnCount.toString());
//             if(i < valueCounts.length) sb.append(",");
//             sb.append("\r\n");
//             i++;
//         }
//         sb.append("]");
//         return sb.toString();
//     }
// }
