package org.lzx.lakemart.service.impl.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AiAnalysisService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String apiKey;

    @Autowired
    public AiAnalysisService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.apiKey = System.getenv("ZHIPUAI_API_KEY");
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalStateException("未配置 ZHIPUAI_API_KEY 环境变量");
        }
    }

    public String generateAdvice(String productName, int historicalDays, String method,
                                 double firstPrediction, double lastPrediction,
                                 Double totalSales, Double avgSales, Double maxSales, String recentTrend) {
        String methodZh = switch (method) {
            case "simple" -> "简单移动平均";
            case "weighted" -> "加权移动平均";
            case "exponential" -> "指数平滑";
            default -> method;
        };

        StringBuilder prompt = new StringBuilder();
        prompt.append("你是一个资深的电商数据分析专家。现在分析商品「").append(productName).append("」。\n");
        prompt.append("历史数据：基于过去 ").append(historicalDays).append(" 天的销量，");
        prompt.append("使用 ").append(methodZh).append(" 算法预测未来7天销量，预测值从 ")
                .append(String.format("%.0f", firstPrediction)).append(" 到 ").append(String.format("%.0f", lastPrediction)).append("。\n");

        if (totalSales != null && avgSales != null && maxSales != null && recentTrend != null) {
            prompt.append("历史销量统计：总量 ").append(String.format("%.0f", totalSales))
                    .append(" 件，日均 ").append(String.format("%.1f", avgSales))
                    .append(" 件，最大单日 ").append(String.format("%.0f", maxSales))
                    .append(" 件，最近一周趋势 ").append(recentTrend).append("。\n");
        }

        prompt.append("请从以下几个方面给出专业建议（总字数150字左右）：\n");
        prompt.append("1. 销量趋势判断（上升/平稳/下降）\n");
        prompt.append("2. 库存风险提示（是否可能缺货或积压）\n");
        prompt.append("3. 具体补货建议（如：建议下周补货 XX 件）\n");
        prompt.append("4. 如有必要，可提促销或调整定价的策略。\n");
        prompt.append("请用流畅的中文输出，避免使用 Markdown。");

        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "glm-4-flash");
            requestBody.put("messages", List.of(Map.of("role", "user", "content", prompt.toString())));
            requestBody.put("temperature", 0.8);
            requestBody.put("max_tokens", 500);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                    "https://open.bigmodel.cn/api/paas/v4/chat/completions",
                    HttpMethod.POST, entity, Map.class);

            if (response.getBody() != null) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    Object content = message.get("content");
                    if (content != null && !content.toString().isEmpty()) {
                        return content.toString();
                    }
                }
            }
            return getFallbackAdvice(historicalDays, methodZh, firstPrediction, lastPrediction);
        } catch (Exception e) {
            e.printStackTrace();
            return getFallbackAdvice(historicalDays, methodZh, firstPrediction, lastPrediction);
        }
    }

    private String getFallbackAdvice(int historicalDays, String methodZh, double first, double last) {
        return String.format("⚠️ AI 服务暂时不可用，使用静态分析：基于过去%d天销量，使用%s算法预测，未来7天销量预计在%.0f~%.0f之间。建议保持关注，可根据实际销售调整库存。",
                historicalDays, methodZh, first, last);
    }
}