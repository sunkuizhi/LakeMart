package org.lzx.lakemart.service.admin;

public interface IAiAnalysisService {
    String generateAdvice(String productName, int historicalDays, String method,
                          double firstPrediction, double lastPrediction,
                          Double totalSales, Double avgSales, Double maxSales, String recentTrend);
}