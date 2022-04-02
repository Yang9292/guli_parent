package com.yang.serviceedu.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yang.commonutils.JwtUtils;
import com.yang.commonutils.R;
import com.yang.servicebase.exceptionhandler.GuliException;
import com.yang.serviceedu.client.UcenterClient;
import com.yang.serviceedu.entity.EduComment;
import com.yang.serviceedu.entity.vo.MemberVo;
import com.yang.serviceedu.service.EduCommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author YangYoung
 * @since 2022-04-01
 */
@RestController
@RequestMapping("/serviceedu/comment")
@CrossOrigin
public class EduCommentController {
    @Autowired
    private EduCommentService eduCommentService;

    @Autowired
    private UcenterClient ucenterClient;

    //根据课程ID查询评论列表带分页
    @GetMapping("getComment/{nowPage}/{limit}")
    public R getComment(@PathVariable int nowPage,
                         @PathVariable int limit,
                         String courseid){

        Page<EduComment> page = new Page<>(nowPage,limit);
        QueryWrapper<EduComment> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseid);
        wrapper.orderByDesc("gmt_create");
        eduCommentService.pageMaps(page,wrapper);
        HashMap<String,Object> map = new HashMap<>();
        List<EduComment> commentList = page.getRecords();
        map.put("items",commentList);
        map.put("current", page.getCurrent());
        map.put("pages", page.getPages());
        map.put("size", page.getSize());
        map.put("total", page.getTotal());
        map.put("hasNext", page.hasNext());
        map.put("hasPrevious", page.hasPrevious());
        return R.ok().data(map);
    }

    //添加评论
    @PostMapping("saveComment")
    public R saveComment(@RequestBody EduComment eduComment,
                         HttpServletRequest request){
        //通过request取出token值中的用户ID
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        System.out.println("**************"+memberId);
        if (StringUtils.isEmpty(memberId)){
            throw new GuliException(20001,"请先登录..");
        }
        eduComment.setMemberId(memberId);
        //根据用户ID远程调用UCenter中的方法，获取用户信息
        MemberVo ucenterMemberById = ucenterClient.getUcenterMemberById(memberId);
//        BeanUtils.copyProperties(ucenterMemberById,eduComment);
        eduComment.setAvatar(ucenterMemberById.getAvatar());
        eduComment.setNickname(ucenterMemberById.getNickname());
        eduCommentService.save(eduComment);
        return R.ok();
    }

}

