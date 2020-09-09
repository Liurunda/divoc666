package com.java.liurunda.data;

public class EpidemicDataEntry {
    public int confirmed = 0, suspected = 0, cured = 0, dead = 0;

    public EpidemicDataEntry(final int confirmed, final int suspected, final int cured, final int dead) {
        this.confirmed = confirmed;
        this.suspected = suspected;
        this.cured = cured;
        this.dead = dead;
    }

    public EpidemicDataEntry add(final EpidemicDataEntry rhs) {
        return new EpidemicDataEntry(this.confirmed + rhs.confirmed, this.suspected + rhs.suspected, this.cured + rhs.cured, this.dead + rhs.dead);
    }
}
