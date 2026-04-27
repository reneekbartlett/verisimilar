package com.reneekbartlett.verisimilar.core.model;

// TODO: Implement
public enum Religion {

    PROTESTANT("Protestant"),
    CATHOLIC("Catholic"),
    MORMON("Church of Jesus Christ of Latter-day Saints"),
    EASTERN_ORTHODOX("Eastern Orthodox"),

    JEWISH("Jewish"),
    MUSLIM("Muslim"),
    BUDDHIST("Buddhist"),
    HINDU("Hindu"),
    SIKH("Sikh"),

    ATHEIST("Atheist"),
    AGNOSTIC("Agnostic"),
    NOTHING_IN_PARTICULAR("Nothing in particular"),

    OTHER_CHRISTIAN("Other Christian"),
    OTHER_RELIGION("Other Religion"),

    UNKNOWN("Unknown"),
    DECLINE_TO_ANSWER("Decline to Answer");

    private final String label;

    Religion(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
