package com.caacetc.scheduling.plan;

public class Staff {
    private final int id;

    public Staff(int id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this.id == ((Staff) obj).id;
    }
}
