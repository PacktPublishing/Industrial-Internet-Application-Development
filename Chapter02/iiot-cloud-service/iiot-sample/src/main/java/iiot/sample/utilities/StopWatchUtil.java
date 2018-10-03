package iiot.sample.utilities;

import org.springframework.util.StopWatch;

/**
 * Created by 212552609 on 5/2/17.
 */
public class StopWatchUtil {
    private final StopWatch stopWatch;

    public StopWatchUtil(StopWatch stopWatch){
        this.stopWatch = stopWatch;
    }

    @Override
    public String toString(){
        if (stopWatch == null){
            return "[]";
        }
        StopWatch.TaskInfo[] taskInfo = stopWatch.getTaskInfo();
        StringBuilder b = new StringBuilder();
        b.append(String.format("totalTime[%s]", stopWatch.getTotalTimeMillis()));
        for (StopWatch.TaskInfo info : taskInfo) {
            b.append(String.format("[%s][%s]", info.getTaskName(), info.getTimeMillis()));
        }
        return b.toString();
    }
}

