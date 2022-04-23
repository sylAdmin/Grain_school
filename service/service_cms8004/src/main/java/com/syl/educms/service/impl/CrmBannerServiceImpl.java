package com.syl.educms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.syl.educms.entity.CrmBanner;
import com.syl.educms.mapper.CrmBannerMapper;
import com.syl.educms.service.CrmBannerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务实现类
 * </p>
 *
 * @author syl
 * @since 2022-04-08
 */
@Service
public class CrmBannerServiceImpl extends ServiceImpl<CrmBannerMapper, CrmBanner> implements CrmBannerService {

    @Override
    public List<CrmBanner> selectAllBanner() {
        QueryWrapper<CrmBanner> qw = new QueryWrapper<>();
        qw.orderByDesc("sort");
        qw.last("limit 2");
        List<CrmBanner> list = baseMapper.selectList(qw);
        return list;
    }
}
