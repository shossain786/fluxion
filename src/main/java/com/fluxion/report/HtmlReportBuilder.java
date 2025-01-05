package com.fluxion.report;

import java.util.List;
import java.util.Map;

public class HtmlReportBuilder {
    private final String reportName;
    private final String suiteName;

    public HtmlReportBuilder(String reportName, String suiteName) {
        this.reportName = reportName;
        this.suiteName = suiteName;
    }

    public String buildHtml(List<Map<String, String>> scenarios, String chartData) {
        StringBuilder htmlBuilder = new StringBuilder();

        htmlBuilder.append("<!DOCTYPE html><html><head>")
                .append("<title>").append(reportName).append("</title>")
                .append("<script src='https://cdn.jsdelivr.net/npm/chart.js'></script>")
                .append("</head><body>")
                .append("<h1>").append(suiteName).append("</h1>")
                .append("<canvas id='statusChart' width='400' height='400'></canvas>")
                .append("<script>")
                .append("var ctx = document.getElementById('statusChart').getContext('2d');")
                .append("new Chart(ctx, ").append(chartData).append(");")
                .append("</script>")
                .append("</body></html>");

        return htmlBuilder.toString();
    }
}
