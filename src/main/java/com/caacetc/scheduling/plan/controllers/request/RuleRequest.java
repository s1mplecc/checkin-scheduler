package com.caacetc.scheduling.plan.controllers.request;

import lombok.Data;

import java.util.List;

@Data
public class RuleRequest {
    private String job;
    private String workType;
    private List<RoleItem> roles;
}
