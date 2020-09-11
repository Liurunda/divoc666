package com.java.liurunda.data;

public class EpidemicDataEntry {
    public int confirmed = 0, cured = 0, dead = 0;

    public EpidemicDataEntry(final int confirmed, final int suspected, final int cured, final int dead) {
        this.confirmed = confirmed;
        this.cured = cured;
        this.dead = dead;
    }
}
