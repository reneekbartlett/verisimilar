package com.reneekbartlett.verisimilar.core.model;

import java.util.Set;

/***
 * 
 */
public interface CombinationResultField extends ResultRecord {
    Set<TemplateField> getFields();
}
