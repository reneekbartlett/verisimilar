package com.reneekbartlett.verisimilar.core.generator;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.text.StringSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.datasets.key.KeywordDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.key.UsernameDatasetKey;
import com.reneekbartlett.verisimilar.core.model.AstrologySign;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.UsernameTemplateSelector;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.core.selector.engine.KeywordSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.UsernameSelectionEngine;

/***
 * USERNAME_TEMPLATES_NAME
 * USERNAME_TEMPLATES_POPULAR
 * List<String> USERNAME_KEYWORDS    ResourceValueLoader.loadStringList("/username_keywords.txt")
 * Map<String, String[]> NICKNAME_MAP
 */

public class UsernameGenerator extends AbstractStringGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(UsernameGenerator.class);
    //private static final String DEFAULT_USERNAME = "USER";
    //private static final String DEFAULT_DOMAIN = "example.com";
    //private static final String[] SEPARATORS = { "." };

    private final UsernameSelectionEngine usernameSelector;
    private final KeywordSelectionEngine keywordSelector;

    public UsernameGenerator(UsernameSelectionEngine usernameSelector, KeywordSelectionEngine keywordSelector) {
        this.usernameSelector = usernameSelector;
        this.keywordSelector = keywordSelector;
    }

    @Override
    protected String generateString(DatasetResolutionContext ctx, SelectionFilter filter) {
        // 1. Build the dataset key from the context
        UsernameDatasetKey key = UsernameDatasetKey.defaults();

        // 2. Ask the selector for the selector (selector filters templates)
        UsernameTemplateSelector templateSelector = usernameSelector.getTemplateSelector(key, filter);

        Set<String> templates = templateSelector.templates();

        if(templates.isEmpty()) {
            LOGGER.debug("No templates loaded.  Adding backup/default");
            templates.add("${KEYWORD}${SEPARATOR}${NUM1000}");
            templates.add("${KEYWORD}${NUM10}");
            templates.add("${KEYWORD}${NUM100}");
            templates.add("${KEYWORD}${NUM1000}");
        }

        return generateUsername(ctx, filter, templates);
    }

    private String generateUsername(DatasetResolutionContext ctx, SelectionFilter filter, Set<String> templates) {
        // 1. Build the dataset key from the context
        UsernameDatasetKey usernameDatasetKey = UsernameDatasetKey.defaults();
        KeywordDatasetKey keywordDatasetKey = KeywordDatasetKey.defaults();

        // Generate Random Keyword First
        String randomKeyword = keywordSelector.select(keywordDatasetKey, filter);

        // Then Add Username Templates and Apply Values
        Map<String, Object> params = new HashMap<>();
        params.putAll(filter.getResolvedValues());

        params.put("SEPARATOR", ".");
        params.put("KEYWORD", randomKeyword);

        ThreadLocalRandom rand = ThreadLocalRandom.current();
        params.put("NUM10", String.valueOf(rand.nextInt(100)));
        params.put("NUM100", String.valueOf(rand.nextInt(1000)));
        params.put("NUM1000", String.valueOf(rand.nextInt(1000,9999)));

        if(params.containsKey("BIRTHDAY")) {
            LocalDate birthday = (LocalDate) params.get("BIRTHDAY");
            params.put("BIRTHDAY_YEAR", String.valueOf(birthday.getYear()));
            params.put("BIRTHDAY_YEAR_SHORT", String.valueOf(birthday.getYear()).substring(2));
            params.put("BIRTHDAY_DAY", String.valueOf(birthday.getDayOfMonth()));

            AstrologySign sign = AstrologySign.fromLocalDate(birthday);
            params.put("BIRTHDAY_SIGN", sign.name());
        } 

        if(params.containsKey("FIRST")) {
            String firstName = (String) params.get("FIRST");
            params.put("FIRST_INITIAL", firstName.charAt(0));
        }

        if(params.containsKey("LAST")) {
            String lastName = (String) params.get("LAST");
            params.put("LAST_INITIAL", lastName.charAt(0));
        }

        if(params.containsKey("MIDDLE")) {
            String middleName = (String) params.get("MIDDLE");
            params.put("MIDDLE_INITIAL", middleName.charAt(0));
        }

        // Pick a template
        String template = pickTemplate(templates);

        return StringSubstitutor.replace(template, params);
    }

    private String pickTemplate(Set<String> templates) {
        //LOGGER.debug("picking random from " + templates.size() + " templates");
        return templates.stream()
            .skip(ThreadLocalRandom.current().nextInt(templates.size()))
            .findFirst()
            .orElse("${KEYWORD}${NUM1000}");
    }

    public static final String RFC_HANDLE_REGEX = "^[a-zA-Z0-9]([a-zA-Z0-9._-]{1,18}[a-zA-Z0-9])$";

    // GMAIL.COM
    // Length:
    // Allowed Chars:
    // Disallowed:
    // Structure:
    public static final String GMAIL_HANDLE_REGEX = "^(?=[a-z0-9.]{6,30}$)(?!.*?\\.\\.)[a-z0-9][a-z0-9.]*[a-z0-9]$";

    // OUTLOOK.COM
    // Length: 3 to 20 characters.
    // Characters Allowed: Letters (A-Z, a-z), numbers (0-9), periods (.), underscores (_), and hyphens (-).
    // Period Restriction: At most one period is allowed. It cannot be at the start or end of the username.
    // Structure: Must start with a letter or number.
    public static final String OUTLOOK_HANDLE_REGEX = "^[a-zA-Z0-9]([a-zA-Z0-9._-]{1,18}[a-zA-Z0-9])$";

    // YAHOO.COM
    // Length: 4 to 32 characters long
    // Allowed Chars:  Periods (.), and underscores (_)
    // Disallowed:  spaces, and special symbols (!, @, #, $, &, etc.)
    // Structure: Must start with a letter. Must end with a letter or number. Cannot have consecutive special characters. 
    // Only one period or underscore is allowed in the username
    public static final String YAHOO_HANDLE_REGEX = "^[a-zA-Z][a-zA-Z0-9._]{2,30}[a-z0-9]$";

    // ICLOUD.COM
    // Length:
    // Allowed Chars:
    // Disallowed:
    // Structure:
    public static final String ICLOUD_REGEX = "";

    // TODO: DISPOSABLE:  x8k2j1@10minutemail.com
}
