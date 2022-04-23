package com.syl.educms.controller;


import com.syl.commonutils.R;
import com.syl.educms.entity.CrmBanner;
import com.syl.educms.service.CrmBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author syl
 * @since 2022-04-08
 */
@RestController
@RequestMapping("/educms/bannerfront")
public class BannerFrontController {

    @Resource
    private CrmBannerService bannerService;

    //将返回的结果放在缓存中，当用户发起请求时，会先查缓存中有没有数据，没有在查数据库
    @Cacheable(value = "banner",key = "'selectIndexList'")
    @GetMapping("getAllBanner")
    public R getAllBanner(){
        List<CrmBanner> list = bannerService.selectAllBanner();
        return R.ok().data("list",list);
    }

}

