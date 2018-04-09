package com.meetcity.moon.spider.template;

import com.meetcity.moon.spider.schedule.MoonProducer;
import com.meetcity.moon.spider.schedule.MoonQueue;
import com.meetcity.moon.spider.processor.MoonProcessor;
import com.meetcity.moon.spider.schedule.MoonTask;
import com.meetcity.moon.spider.schedule.MoonTaskExecutor;
import com.meetcity.moon.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Slf4j
public abstract class TemplateListProcessor implements MoonProcessor {


    public abstract Integer processPagination(Object o, MoonTask moonTask);        //解析分页

    public abstract List<MoonTask> processorList(Object downloadResult, MoonTask moonParam);


    @Override
    public Object processor(Object downloadResult, MoonTask moonTask) {

        Integer totalPage = processPagination(downloadResult, moonTask);  //获取总页数
        if (totalPage == null) {
            log.error("Total page is null , task is : {}", moonTask.toString());
            return null;
        }

        String paginationName = moonTask.getPaginationName();      //分页名
        Integer paginationIndex = moonTask.getPaginationIndex();   //分页数

        List<MoonTask> paramList = processorList(downloadResult, moonTask);      //将详情任务加入队列，比分页任务先放入队列中，会被先执行到
        for (MoonTask param : paramList) {
            MoonProducer.getProducer().produce(param);
            log.debug("Detail tasks add to queue , task param is : {}", param.toString());
        }

        if (!StringUtil.isEmpty(paginationName) && paginationIndex != null && paginationIndex < totalPage) {        //分页名称 分页页数 不为空，分页页数小于总页数
            moonTask.setPaginationIndex(paginationIndex + 1);
            MoonProducer.getProducer().produce(moonTask);
            log.debug("PageTask add to queue,task is :{}", moonTask.toString());
            return "new page add to queue";
        } else if (paginationIndex == totalPage) {
            log.debug("PageTask finish , index is : {} , totolPage is : {} ", paginationIndex, totalPage);
            return "all page task execute finish";
        }

        return null;
    }
}