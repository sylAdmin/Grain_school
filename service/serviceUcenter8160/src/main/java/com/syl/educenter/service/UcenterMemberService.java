package com.syl.educenter.service;

import com.syl.educenter.entity.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.syl.educenter.entity.vo.RegisterVo;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author syl
 * @since 2022-04-10
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    String login(UcenterMember ucenterMember);

    void register(RegisterVo registerVo);

    UcenterMember getOpenIdMember(String openid);

    Integer countRegisterDay(String day);
}
