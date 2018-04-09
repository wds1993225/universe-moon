package com.meetcity.moon.web.monitor;

import com.meetcity.moon.constant.Constants;
import com.meetcity.moon.spider.monitor.MoonMonitor;
import com.meetcity.moon.spider.schedule.MoonTask;

/**
 * Created by moon on 2018/2/15 .
 */

public class WebMonitor implements MoonMonitor {

    public void onStart(MoonTask moonTask) {

    }

    public void onSuccess(MoonTask moonTask) {

    }

    public void onFailed(MoonTask moonTask, String errorMessage, Object processData, Constants.MoonPeriod period) {

    }

    public void end(MoonTask moonTask) {

    }

    public void getTotalCount() {

    }

    public void getTotalTime() {

    }

    public void haveBeanDownloaded(MoonTask moonTask, String RDKey) {

    }
}
