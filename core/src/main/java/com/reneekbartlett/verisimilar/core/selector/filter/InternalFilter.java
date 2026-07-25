package com.reneekbartlett.verisimilar.core.selector.filter;

import com.reneekbartlett.verisimilar.core.model.CityStateZip;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
//import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter.Builder;

public record InternalFilter(SelectionFilter filter) {
    
    public InternalFilter() {
        this(null);
    }

    public static Builder getBuilder() {
        return new InternalFilter().builder();
    }

    public Builder builder() {
        return new InternalFilter.Builder();
    }

    public class Builder extends SelectionFilter.Builder {
        public Builder() {
            super();
        }

        @Override
        public Builder equalTo(String value, TemplateField field) {
            // TODO:  Check
            //super.equalToMap.put(field, value);
            //return this;
            return (Builder) super.equalTo(value, field);
        }

        @Override
        public Builder startsWith(String value, TemplateField field) {
            return (Builder) super.startsWith(value, field);
        }

        @Override
        public Builder endsWith(String value, TemplateField field) {
            return (Builder) super.endsWith(value, field);
        }

        public Builder cityStateZip(CityStateZip value) {
            return (Builder) super.cityStateZip(value);
        }

    }

    //public static Builder builder() {
        //new Builder();
    //    return null;
        //return new Builder();
    //}

}
