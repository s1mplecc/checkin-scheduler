package com.caacetc.scheduling.plan.controllers.request;

import lombok.Data;

@Data
public class RoleItem {
    private String code;
    private String currentValueMin;
    private String currentValueMax;
}
