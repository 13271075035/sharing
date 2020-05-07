package com.sharing.filter;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * Created by ASUS on 2019/9/10.
 */
public class LogFilter extends Filter<ILoggingEvent> {

    @Override
    public FilterReply decide(ILoggingEvent event) {

        if (event.getLoggerName().equals("com.syxd.common.utils.HttpClientUtils")
                ||event.getLoggerName().equals("com.syxd.service.OrderMasterService")
                ||event.getLoggerName().equals("com.syxd.service.ActivityInfoService")
                ){
            return FilterReply.DENY;
        } else{
            return FilterReply.ACCEPT;
        }
    }



}
