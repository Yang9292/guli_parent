package com.yang.serviceedu.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yang.commonutils.R;
import com.yang.serviceedu.entity.EduSubject;
import com.yang.serviceedu.entity.excel.SubjectData;
import com.yang.serviceedu.entity.subject.OneSubject;
import com.yang.serviceedu.entity.subject.TwoSubject;
import com.yang.serviceedu.listener.SubjectExcelListener;
import com.yang.serviceedu.mapper.EduSubjectMapper;
import com.yang.serviceedu.service.EduSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author YangYoung
 * @since 2022-03-22
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    //实现对上文文件的内容进行读取
    @Override
    public R saveSubject(EduSubjectService eduSubjectService, MultipartFile multipartFile) {

        try {
            InputStream inputStream = multipartFile.getInputStream();

            if (multipartFile.isEmpty()){
                return R.error().message("上传的文件为空");
            }
            //通过流的方式对文件内容进行读取
            EasyExcel.read(inputStream,SubjectData.class,new SubjectExcelListener(eduSubjectService)).sheet().doRead();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.ok();
    }

    //获取所有的一级分类课程，每个一级分类课程里面包含二级分类课程
    @Override
    public List<OneSubject> getAllSubject() {
        //查询出所有一级分类科目
        QueryWrapper<EduSubject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",0);
        List<EduSubject> oneEduSubjectList = baseMapper.selectList(queryWrapper);

        //查询出二级分类科目
        QueryWrapper<EduSubject> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.ne("parent_id",0);
        List<EduSubject> twoEduSubjectList = baseMapper.selectList(queryWrapper1);

        //创建一个集合 作为最终集合作为返回
        List<OneSubject> oneSubjectsFinalList = new ArrayList<>();

        //封装一级分类科目
        for (EduSubject oneEduSubject : oneEduSubjectList){
            //利用spring将查询到的对象属性值赋给一级属性
            OneSubject oneSubject = new OneSubject();
            //第一个参数是get值的对象，第二个参数是set值的对象
            BeanUtils.copyProperties(oneEduSubject,oneSubject);

            //封装二级分类科目
            for (EduSubject twoEduSubject : twoEduSubjectList){
                if (oneEduSubject.getId().equals(twoEduSubject.getParentId())){
                    TwoSubject twoSubject = new TwoSubject();
                    BeanUtils.copyProperties(twoEduSubject,twoSubject);
                    //将二级目录添加到一级目录中
                    oneSubject.getChildren().add(twoSubject);
                }
            }

            //将封装好的一级分类科目添加到最后的集合中
            oneSubjectsFinalList.add(oneSubject);
        }
        return oneSubjectsFinalList;
    }
}
