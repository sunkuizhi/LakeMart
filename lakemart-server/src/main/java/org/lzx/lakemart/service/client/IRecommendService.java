package org.lzx.lakemart.service.client;

import org.lzx.lakemart.model.vo.RecommendProductVO;

import java.util.List;

public interface IRecommendService {
    // 返回推荐商品列表（含推荐理由）
    List<RecommendProductVO> recommendForUser(Long userId, int limit);
}