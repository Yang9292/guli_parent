package com.yang.cms.service.impl;

import com.yang.cms.entity.CrmBanner;
import com.yang.cms.mapper.CrmBannerMapper;
import com.yang.cms.service.CrmBannerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务实现类
 * </p>
 *
 * @author YangYoung
 * @since 2022-03-28
 */
@Service
public class CrmBannerServiceImpl extends ServiceImpl<CrmBannerMapper, CrmBanner> implements CrmBannerService {

    //获取所有轮播图片
    @Cacheable(value = "banner",key = "'selectAllBannxer'")
    @Override
    public List<CrmBanner> selectAllBannxer() {
        List<CrmBanner> crmBanners = baseMapper.selectList(null);
        return crmBanners;
    }
}
