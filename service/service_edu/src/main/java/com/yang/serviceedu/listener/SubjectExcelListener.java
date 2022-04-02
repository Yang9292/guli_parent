package com.yang.serviceedu.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yang.servicebase.exceptionhandler.GuliException;
import com.yang.serviceedu.entity.EduSubject;
import com.yang.serviceedu.entity.excel.SubjectData;
import com.yang.serviceedu.service.EduSubjectService;

public class SubjectExcelListener extends AnalysisEventListener<SubjectData> {

    public EduSubjectService eduSubjectService;

    public SubjectExcelListener() {
    }

    public SubjectExcelListener(EduSubjectService eduSubjectService) {
        this.eduSubjectService = eduSubjectService;
    }

    //一行一行的读取数据
    @Override
    public void invoke(SubjectData subjectData, AnalysisContext analysisContext) {

        if (subjectData == null){
            throw new GuliException(20001,"文件数据为空..");
        }

        //一行一行读取,第一个值为一级分类,第二个值为二级分类
        //首先判断一级标题是否重复
        EduSubject eduSubject = this.existOneSubject(eduSubjectService, subjectData.getOneSubjectName());
        if (eduSubject == null){//说明数据库中一级分类没有重复
            eduSubject = new EduSubject();
            eduSubject.setParentId("0");
            eduSubject.setTitle(subjectData.getOneSubjectName());
            eduSubjectService.save(eduSubject);
        }

        //判断二级标题是否重复
        String pid = eduSubject.getId();
        EduSubject eduTwoSubject = this.existTwoSubject(eduSubjectService, subjectData.getTwoSubjectName(), pid);
        if (eduTwoSubject == null){//说明数据库中二级分类没有重复
            eduTwoSubject = new EduSubject();
            eduTwoSubject.setTitle(subjectData.getTwoSubjectName());
            eduTwoSubject.setParentId(pid);
            eduSubjectService.save(eduTwoSubject);
        }

    }


    //判断一级标题不能重复
    private EduSubject existOneSubject(EduSubjectService eduSubjectService,String name){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        //一级标题parent_id都为0
        wrapper.eq("parent_id","0");
        wrapper.eq("title",name);
        EduSubject one = eduSubjectService.getOne(wrapper);
        return one;
    }


    //判断二级标题不能重复
    private EduSubject existTwoSubject(EduSubjectService eduSubjectService,String name,String pid){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        //一级标题parent_id都为0
        wrapper.eq("parent_id",pid);
        wrapper.eq("title",name);
        EduSubject two = eduSubjectService.getOne(wrapper);
        return two;
    }


    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
