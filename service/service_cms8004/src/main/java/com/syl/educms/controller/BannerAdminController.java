package com.syl.educms.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.syl.commonutils.R;
import com.syl.educms.entity.CrmBanner;
import com.syl.educms.service.CrmBannerService;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author syl
 * @since 2022-04-08
 */
@RestController
@RequestMapping("/educms/banneradmin")
public class BannerAdminController {

    @Resource
    private CrmBannerService bannerService;

    @ApiOperation(value = "分页查询分页")
    @GetMapping("pageBanner/{page}/{limit}")
    public R pageBanner(@PathVariable long page,@PathVariable long limit){
        Page<CrmBanner> crmBannerPage = new Page<>(page,limit);
        bannerService.page(crmBannerPage,null);
        return R.ok().data("items",crmBannerPage.getRecords()).data("total",crmBannerPage.getTotal());
    }

    @ApiOperation(value = "获取Banner")
    @GetMapping("get/{id}")
    public R get(@PathVariable String id) {
        CrmBanner banner = bannerService.getById(id);
        return R.ok().data("item", banner);
    }

    @ApiOperation(value = "新增Banner")
    @PostMapping("save")
    public R save(@RequestBody CrmBanner banner) {
        bannerService.save(banner);
        return R.ok();
    }

    @ApiOperation(value = "修改Banner")
    @PutMapping("update")
    public R updateById(@RequestBody CrmBanner banner) {
        bannerService.updateById(banner);
        return R.ok();
    }

    @ApiOperation(value = "删除Banner")
    @DeleteMapping("remove/{id}")
    public R remove(@PathVariable String id) {
        bannerService.removeById(id);
        return R.ok();
    }

}

