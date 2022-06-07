package com.yang.ucenter.mapper;

import com.yang.ucenter.entity.Member;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author YangYoung
 * @since 2022-03-28
 */
public interface MemberMapper extends BaseMapper<Member> {

    //后台 查询一天注册的人数
    Integer countRegister(@Param("day") String day);
}
