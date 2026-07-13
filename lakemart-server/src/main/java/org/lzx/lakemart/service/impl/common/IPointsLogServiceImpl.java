package org.lzx.lakemart.service.impl.common;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.lzx.lakemart.mapper.PointsLogMapper;
import org.lzx.lakemart.mapper.UserMapper;
import org.lzx.lakemart.model.entity.PointsLog;
import org.lzx.lakemart.model.vo.PointsLogVO;
import org.lzx.lakemart.service.common.IPointsLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class IPointsLogServiceImpl extends ServiceImpl<PointsLogMapper, PointsLog> implements IPointsLogService {

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public void recordPoints(Long userId, Integer pointsChange, String type, Long relatedId, String remark) {
        // 查询用户当前积分余额
        Integer currentPoints = userMapper.selectPointsByUserId(userId);
        if (currentPoints == null) currentPoints = 0;
        Integer newBalance = currentPoints + pointsChange;
        if (newBalance < 0) {
            throw new RuntimeException("积分余额不足");
        }
        // 更新用户积分
        userMapper.updatePoints(userId, newBalance);
        // 记录日志
        PointsLog log = new PointsLog();
        log.setUserId(userId);
        log.setPointsChange(pointsChange);
        log.setBalance(newBalance);
        log.setType(type);
        log.setRelatedId(relatedId);
        log.setRemark(remark);
        log.setCreateTime(LocalDateTime.now());
        this.save(log);
    }

    @Override
    public Page<PointsLogVO> getUserPointsLogs(Long userId, Integer pageNum, Integer pageSize) {
        Page<PointsLog> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<PointsLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PointsLog::getUserId, userId).orderByDesc(PointsLog::getCreateTime);
        Page<PointsLog> logPage = this.page(page, wrapper);
        // 转换 VO
        Page<PointsLogVO> voPage = new Page<>(logPage.getCurrent(), logPage.getSize(), logPage.getTotal());
        voPage.setRecords(logPage.getRecords().stream().map(this::toVO).collect(Collectors.toList()));
        return voPage;
    }

    private PointsLogVO toVO(PointsLog log) {
        return PointsLogVO.builder()
                .id(log.getId())
                .pointsChange(log.getPointsChange())
                .balance(log.getBalance())
                .type(log.getType())
                .relatedId(log.getRelatedId())
                .remark(log.getRemark())
                .createTime(log.getCreateTime())
                .build();
    }
}