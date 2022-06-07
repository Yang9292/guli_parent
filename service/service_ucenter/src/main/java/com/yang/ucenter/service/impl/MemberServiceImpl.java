package com.yang.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yang.commonutils.JwtUtils;
import com.yang.commonutils.MD5;
import com.yang.servicebase.exceptionhandler.GuliException;
import com.yang.ucenter.entity.Member;
import com.yang.ucenter.entity.vo.RegisterVo;
import com.yang.ucenter.mapper.MemberMapper;
import com.yang.ucenter.service.MemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.SocketUtils;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author YangYoung
 * @since 2022-03-28
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public String login(Member member) {
        //取出手机号
        String mobile = member.getMobile();
        //取出密码
        String password = member.getPassword();
        //手机号或密码为空，抛出异常
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)){
            throw new GuliException(20001,"账号或密码为空..");
        }

        QueryWrapper<Member> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        //根据手机号取出数据库中对应的值
        Member mobileMember = baseMapper.selectOne(wrapper);
        //判断账号是否存在
        if (mobileMember == null){
            throw new GuliException(20001,"账号不存在...");
        }
        //密码不相等，抛出异常
        password = MD5.encrypt(password);
        if (!password.equals(mobileMember.getPassword())){
            throw new GuliException(20001,"密码错误...");
        }
        //判断账号是否被禁用
        if (mobileMember.getIsDisabled()){
            throw new GuliException(20001,"该账号已被禁用..");
        }

        //如果上述都不符合则证明登陆成功，返回一个token值
        String jwtToken = JwtUtils.getJwtToken(mobileMember.getId(), mobileMember.getNickname());

        return jwtToken;
    }

    @Override
    public void register(RegisterVo registerVo) {
        String mobile = registerVo.getMobile();
        String nickname = registerVo.getNickname();
        String password = registerVo.getPassword();
        String code = registerVo.getCode();
        //判断参数传递的值是否为空
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(nickname) || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(code)){
            throw new GuliException(20001,"注册失败..");
        }
        //判断验证码是否正确以及过时
        //从redis中获取验证码
        String s = redisTemplate.opsForValue().get(mobile);
        //再与前端传回来的验证码比对，是否正确
        if (!s.equals(code)) {
            throw new GuliException(20001,"验证码不正确，注册失败！");
        }
        //判断手机号是否存在
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile",mobile);
        Integer count = baseMapper.selectCount(queryWrapper);
        //大于0则证明手机号存在，抛出异常
        if (count > 0){
            throw new GuliException(20001,"手机号已存在...");
        }
        //上述情况都没有，则证明可以注册
        Member member = new Member();
        member.setMobile(mobile);
        member.setPassword(MD5.encrypt(password));
        member.setIsDisabled(false);
        member.setNickname(nickname);
        member.setAvatar("https://edu-9292.oss-cn-beijing.aliyuncs.com/2022/03/21/docter.jpg");
        //将数据保存到数据库
        baseMapper.insert(member);
    }

    @Override
    public void miniRegister(String nickName, String mobile, String password) {
        //判断参数传递的值是否为空
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(nickName) || StringUtils.isEmpty(password)){
            throw new GuliException(20001,"注册失败..");
        }
        //判断手机号是否存在
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile",mobile);
        Integer count = baseMapper.selectCount(queryWrapper);
        //大于0则证明手机号存在，抛出异常
        if (count > 0){
            throw new GuliException(20001,"手机号已存在...");
        }

        //上述情况都没有，则证明可以注册
        Member member = new Member();
        member.setMobile(mobile);
        member.setPassword(MD5.encrypt(password));
        member.setIsDisabled(false);
        member.setNickname(nickName);
        member.setAvatar("https://edu-9292.oss-cn-beijing.aliyuncs.com/2022/03/21/docter.jpg");
        //将数据保存到数据库
        baseMapper.insert(member);

    }

    @Override
    public Integer countRegister(String day) {
        Integer count = baseMapper.countRegister(day);
        return count;
    }


}
