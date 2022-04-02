package com.yang.serviceedu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yang.serviceedu.entity.EduTeacher;
import com.yang.serviceedu.mapper.EduTeacherMapper;
import com.yang.serviceedu.service.EduTeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author YangYoung
 * @since 2022-03-18
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {

    @Override
    public Map<String, Object> getTeacherFrontPageList(Page<EduTeacher> pageInfo) {
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        baseMapper.selectMapsPage(pageInfo,wrapper);

        HashMap<String,Object> map = new HashMap<>();
        //总记录数
        long total = pageInfo.getTotal();
        //当前页
        long current = pageInfo.getCurrent();
        //每页记录数
        long size = pageInfo.getSize();
        //查询到的对象
        List<EduTeacher> teacherList = pageInfo.getRecords();
        //总页数
        long pages = pageInfo.getPages();
        //是否有上一页
        boolean hasPrevious = pageInfo.hasPrevious();
        //是否有下一页
        boolean hasNext = pageInfo.hasNext();

        //将数据封装到map中返回
        map.put("total",total);
        map.put("current",current);
        map.put("size",size);
        map.put("list",teacherList);
        map.put("hasPrevious",hasPrevious);
        map.put("hasNext",hasNext);
        map.put("pages",pages);

        return map;
    }
}
