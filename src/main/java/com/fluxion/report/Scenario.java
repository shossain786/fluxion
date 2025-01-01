package com.fluxion.report;

import java.util.ArrayList;
import java.util.List;

public class Scenario {
    private String name;
    private String status;
    private List<Step> steps;

    public Scenario(String name) {
        this.name = name;
        this.steps = new ArrayList<>();
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public void addStep(Step step) {
        steps.add(step);
    }

    public List<Step> getSteps() {
        return steps;
    }
}
