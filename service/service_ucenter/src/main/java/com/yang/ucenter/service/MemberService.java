package com.yang.ucenter.service;

import com.yang.ucenter.entity.Member;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yang.ucenter.entity.vo.RegisterVo;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author YangYoung
 * @since 2022-03-28
 */
public interface MemberService extends IService<Member> {

    //登录
    String login(Member member);

    //注册
    void register(RegisterVo registerVo);
}
