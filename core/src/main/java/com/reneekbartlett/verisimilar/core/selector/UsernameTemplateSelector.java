//package com.reneekbartlett.verisimilar.core.selector;
//
//import java.util.Set;
//
//import com.reneekbartlett.verisimilar.core.model.TemplateField;
//
//public record UsernameTemplateSelector(
//        Set<String> templates,
//        Set<TemplateField> fields
//) {
//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder(0).append("UsernameTemplateSelector");
//        if(templates != null) sb.append("$templates=" + templates.size());
//        if(fields != null) sb.append("$fields=" + fields.size());
//        return sb.toString();
//    }
//}
