package com.reneekbartlett.verisimilar.core.generator;

import com.reneekbartlett.verisimilar.core.datasets.key.UsernameDatasetKey;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;

import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.core.selector.engine.UsernameSelectionEngine;

/***
 * USERNAME_TEMPLATES_NAME
 * USERNAME_TEMPLATES_POPULAR
 */

public class UsernameGenerator extends AbstractStringGenerator {

    //private static final String DEFAULT_USERNAME = "USER";

    private final UsernameSelectionEngine usernameSelector;

    public UsernameGenerator(UsernameSelectionEngine usernameSelector) {
        this.usernameSelector = usernameSelector;
    }

    @Override
    protected String generateString(DatasetResolutionContext ctx, SelectionFilter filter) {
        UsernameDatasetKey key = UsernameDatasetKey.fromContext(ctx);
        String username = filter.username().orElseGet(() -> generateUsername(key, filter));
        return username.toUpperCase();
    }

    private String generateUsername(UsernameDatasetKey usernameDatasetKey, SelectionFilter filter) {
        return usernameSelector.select(usernameDatasetKey, filter);
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
