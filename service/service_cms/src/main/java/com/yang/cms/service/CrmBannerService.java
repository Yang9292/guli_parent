package com.yang.cms.service;

import com.yang.cms.entity.CrmBanner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author YangYoung
 * @since 2022-03-28
 */
public interface CrmBannerService extends IService<CrmBanner> {

    //显示所有轮播图
    List<CrmBanner> selectAllBannxer();
}
