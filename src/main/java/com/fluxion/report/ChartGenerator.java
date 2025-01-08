package com.fluxion.report;

import java.util.List;
import java.util.Map;

public class ChartGenerator {
    public String generateChartData(List<Map<String, String>> scenarios) {
        long passed = scenarios.stream().filter(s -> s.get("status").equalsIgnoreCase("passed")).count();
        long failed = scenarios.stream().filter(s -> s.get("status").equalsIgnoreCase("failed")).count();

        return "{type: 'pie', data: { labels: ['Passed', 'Failed'], datasets: [{ data: ["
                + passed + "," + failed + "], backgroundColor: ['#28a745', '#dc3545'] }]}}";
    }
}
