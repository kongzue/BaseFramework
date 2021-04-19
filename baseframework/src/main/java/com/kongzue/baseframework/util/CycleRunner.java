package com.kongzue.baseframework.util;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/3/15 14:27
 */
public class CycleRunner extends Timer {
    
    private TimerTask timerTask;
    private boolean isCanceled;
    
    public boolean isCanceled() {
        return isCanceled;
    }
    
    @Override
    public void cancel() {
        isCanceled = true;
        super.cancel();
    }
    
    @Override
    public void schedule(TimerTask task, long delay) {
        isCanceled = false;
        super.schedule(task, delay);
    }
    
    @Override
    public void schedule(TimerTask task, Date time) {
        isCanceled = false;
        super.schedule(task, time);
    }
    
    @Override
    public void schedule(TimerTask task, long delay, long period) {
        isCanceled = false;
        super.schedule(task, delay, period);
    }
    
    @Override
    public void schedule(TimerTask task, Date firstTime, long period) {
        isCanceled = false;
        super.schedule(task, firstTime, period);
    }
    
    @Override
    public void scheduleAtFixedRate(TimerTask task, long delay, long period) {
        isCanceled = false;
        super.scheduleAtFixedRate(task, delay, period);
    }
    
    @Override
    public void scheduleAtFixedRate(TimerTask task, Date firstTime, long period) {
        isCanceled = false;
        super.scheduleAtFixedRate(task, firstTime, period);
    }
}