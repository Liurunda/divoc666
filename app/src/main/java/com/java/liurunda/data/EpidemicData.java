package com.java.liurunda.data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

public class EpidemicData implements Serializable {
    public LocalDate startDate = LocalDate.now(ZoneId.systemDefault());
    public ArrayList<EpidemicDataEntry> entries = null;

    public EpidemicData add(final EpidemicData rhs) {
        final long dateDiff = this.startDate.toEpochDay() - rhs.startDate.toEpochDay();
        long start1 = 0, start2 = dateDiff;
        if (dateDiff < 0) {
            start1 -= dateDiff;
            start2 = 0;
        }
        EpidemicData ans = new EpidemicData();
        ans.startDate = LocalDate.ofEpochDay(this.startDate.toEpochDay() + start1);
        ans.entries = new ArrayList<>();
        while (start1 < this.entries.size() && start2 < rhs.entries.size()) {
            ans.entries.add(this.entries.get((int) start1).add(rhs.entries.get((int) start2)));
        }
        return ans;
    }
}
