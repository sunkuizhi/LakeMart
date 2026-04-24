package org.lzx.lakemart.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.lzx.lakemart.model.entity.PointsLog;
import org.lzx.lakemart.model.vo.PointsLogVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface PointsLogService extends IService<PointsLog> {
    /**
     * 记录积分变动
     * @param userId 用户ID
     * @param pointsChange 变动值（正加负减）
     * @param type 类型
     * @param relatedId 关联业务ID
     * @param remark 备注
     */
    void recordPoints(Long userId, Integer pointsChange, String type, Long relatedId, String remark);

    /**
     * 分页查询用户积分明细
     * @param userId 用户ID
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return 分页VO
     */
    Page<PointsLogVO> getUserPointsLogs(Long userId, Integer pageNum, Integer pageSize);
}