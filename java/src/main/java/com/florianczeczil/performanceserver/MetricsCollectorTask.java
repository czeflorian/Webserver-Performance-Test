package com.florianczeczil.performanceserver;

import java.util.TimerTask;

public class MetricsCollectorTask extends TimerTask{

    @Override
    public void run() {
        MetricsCollector.queryCpuUsage();
        MetricsCollector.queryMemoryUsage();        
    }
    
}
