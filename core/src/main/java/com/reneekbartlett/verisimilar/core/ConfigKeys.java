package com.reneekbartlett.verisimilar.core;

/***
 * 
 */
public final class ConfigKeys {

    private ConfigKeys() {
        throw new UnsupportedOperationException("Constant utility class");
    }

    // Feature Toggles Group
    public static final class Features {
        public static final String CLEANUP_ENABLED = "features.cleanup.enabled";
        public static final String MOCK_API = "features.mock.api";
    }

    public static final class Database {
        public static final class SQLite {
            public static final String DEFAULT_FILE = "datasets.firstname.defaultfile";
        }
    }

    public static final class Datasets {
        public static final String DATASETS_DIR = "datasets.dir";

        public static final class FirstName {
            public static final String DEFAULT_FILE = "datasets.firstname.defaultfile";
        }

        public static final class StreetName {
            // "datasets/cfg_postaladdress_address1_streetname_ALL.csv"
            public static final String DEFAULT_FILE = "datasets.streetname.defaultfile";
        }
    }

    public static final class Templates {
        public static final String TEMPLATES_DIR = "templates.dir";
    }

}
