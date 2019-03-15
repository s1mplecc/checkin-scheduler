package com.caacetc.scheduling.plan;

public class Staff {
    private int id;

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this.id == ((Staff) obj).id;
    }
}
