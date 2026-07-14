package org.lzx.lakemart.service.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * AB测试分流管理器
 * 用于控制实验组（ALS推荐）和对照组（默认推荐）的流量比例
 */
@Component
@Slf4j
public class ABTestManager {

    // 实验组流量比例，可通过 application.yml 配置，例如 abtest.experiment.ratio=0.2
    @Value("${abtest.experiment.ratio:0.1}")
    private double experimentTrafficRatio;

    /**
     * 判断用户是否属于实验组（ALS推荐）
     * 使用 userId 的 hash 值取模，保证同一用户每次分流结果一致
     */
    public boolean isInExperimentGroup(Long userId) {
        if (userId == null) return false;
        int bucket = Math.abs(userId.hashCode()) % 100;
        boolean inExperiment = bucket < (experimentTrafficRatio * 100);
        log.debug("用户 {} 分流: {}", userId, inExperiment ? "实验组(ALS)" : "对照组(降级)");
        return inExperiment;
    }

    /**
     * 获取用户所属实验ID，用于埋点上报
     */
    public String getExperimentId(Long userId) {
        return isInExperimentGroup(userId) ? "ALS_EXP" : "CONTROL";
    }
}