package org.lzx.lakemart.service.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ABTestManager {

    @Value("${abtest.experiment.ratio:0.1}")
    private double experimentTrafficRatio;

    public boolean isInExperimentGroup(Long userId) {
        if (userId == null) return false;
        int bucket = Math.abs(userId.hashCode()) % 100;
        boolean inExperiment = bucket < (experimentTrafficRatio * 100);
        log.debug("用户 {} 分流: {}", userId, inExperiment ? "实验组(ALS)" : "对照组(降级)");
        return inExperiment;
    }

    public String getExperimentId(Long userId) {
        return isInExperimentGroup(userId) ? "ALS_EXP" : "CONTROL";
    }
}