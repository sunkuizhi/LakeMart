package org.lzx.lakemart.service.client;

import org.lzx.lakemart.model.dto.BehaviorMessage;
import org.lzx.lakemart.model.dto.BehaviorMonitorDto;

import java.util.List;

public interface BehaviorLogService {
    void saveBehaviorLog(BehaviorMessage message);

    // 新增：获取最近的行为记录（用于管理端轮询）
    List<BehaviorMonitorDto> getRecentBehaviors(int limit);
}