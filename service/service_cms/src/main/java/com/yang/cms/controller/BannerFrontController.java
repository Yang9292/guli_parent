package com.yang.cms.controller;

import com.yang.cms.entity.CrmBanner;
import com.yang.cms.service.CrmBannerService;
import com.yang.commonutils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 首页banner表 前端控制器
 *
 * 普通用户显示轮播图
 * </p>
 *
 * @author YangYoung
 * @since 2022-03-28
 */
@RestController
@RequestMapping("/educms/bannerfront")
@CrossOrigin
public class BannerFrontController {

    @Autowired
    private CrmBannerService crmBannerService;

    //查询所有幻灯片
    @GetMapping("getAllBanner")
    public R getAllBanner(){
        List<CrmBanner> list = crmBannerService.selectAllBannxer();
        return R.ok().data("list",list);
    }
}
