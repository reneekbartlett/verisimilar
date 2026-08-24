package com.reneekbartlett.verisimilar.core.templates;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import com.reneekbartlett.verisimilar.core.model.AstrologySign;
import com.reneekbartlett.verisimilar.core.model.TemplateField;

public record TemplateParameters(Set<UsernameTemplateParam> usernameTemplateFields) {

    public EnumSet<TemplateField> populatedFields(){
        if(usernameTemplateFields == null && usernameTemplateFields.isEmpty()) {
            return EnumSet.noneOf(TemplateField.class);
        }
        return usernameTemplateFields.stream()
                .map(x -> x.templateField())
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(TemplateField.class)));
    }

    /***
     * Get fields for template.
     * @return
     */
    public Map<String, Object> resolved(){
        // TODO:  Use placeholder instead? TemplateField.BIRTHDAY.getPlaceholder()
        //usernameTemplateFields.stream().map(x -> x.fieldValue())
        Map<String, Object> resolvedValueParams = HashMap.newHashMap(usernameTemplateFields.size());
        for(UsernameTemplateParam templateParam : usernameTemplateFields) {
            resolvedValueParams.put(templateParam.templateField().getPlaceholder(), templateParam.fieldValue());
        }

        String birthdayPlaceholder = TemplateField.BIRTHDAY.getPlaceholder();
        if(resolvedValueParams.containsKey(birthdayPlaceholder)) {
            LocalDate birthday = LocalDate.parse((String)resolvedValueParams.get(birthdayPlaceholder));
            AstrologySign sign = AstrologySign.fromLocalDate(birthday);
            resolvedValueParams.put("BIRTHDAY_YEAR", String.valueOf(birthday.getYear()));
            resolvedValueParams.put("BIRTHDAY_YEAR_SHORT", String.valueOf(birthday.getYear()).substring(2));
            resolvedValueParams.put("BIRTHDAY_DAY", String.valueOf(birthday.getDayOfMonth()));
            resolvedValueParams.put("BIRTHDAY_SIGN", sign.name());
        }

        String firstNamePlaceholder = TemplateField.FIRST_NAME.getPlaceholder();
        if(resolvedValueParams.containsKey(firstNamePlaceholder)) {
            String firstName = (String) resolvedValueParams.get(firstNamePlaceholder);
            resolvedValueParams.put("FIRST_INITIAL", firstName.charAt(0));
        }

        if(resolvedValueParams.containsKey(TemplateField.LAST_NAME.getPlaceholder())) {
             String lastName = (String) resolvedValueParams.get(TemplateField.LAST_NAME.getPlaceholder());
             resolvedValueParams.put("LAST_INITIAL", lastName.charAt(0));
        }

        if(resolvedValueParams.containsKey(TemplateField.MIDDLE_NAME.getPlaceholder())) {
             String middleName = (String) resolvedValueParams.get(TemplateField.MIDDLE_NAME.getPlaceholder());
             resolvedValueParams.put("MIDDLE_INITIAL", middleName.charAt(0));
        }

        return resolvedValueParams;
    }

    @SuppressWarnings("unused")
    private Map<String, Object> general(){
        Map<String, Object> generalValueParams = new HashMap<>();
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        generalValueParams.put(TemplateField.fromValue("NUM10").getPlaceholder(), String.valueOf(rand.nextInt(100)));
        generalValueParams.put(TemplateField.fromValue("NUM100").getPlaceholder(), String.valueOf(rand.nextInt(1000)));
        generalValueParams.put(TemplateField.fromValue("NUM1000").getPlaceholder(), String.valueOf(rand.nextInt(1000,9999)));
        generalValueParams.put("SEPARATOR", ".");
        return generalValueParams;
    }

    public Map<String, Object> keyword(String... keywords){
        Map<String, Object> keywordValueParams = new HashMap<>();
        int i = 1;
        for(String keyword : keywords) {
            TemplateField keywordField = TemplateField.fromValue("KEYWORD" + String.valueOf(i));
            if(keywordField != null) {
                keywordValueParams.put(keywordField.getPlaceholder(), keyword);
                i++;
            } else {
                break;
            }
        }
        return keywordValueParams;
    }
}
