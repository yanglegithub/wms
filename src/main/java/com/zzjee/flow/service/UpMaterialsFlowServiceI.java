package com.zzjee.flow.service;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface UpMaterialsFlowServiceI {
    /**
     * 流程启动
     * @param businessKey
     * @param vars
     */
    public void start(String businessKey, Map<String,Object> vars);

    /**
     * 流程完成
     * @param taskId
     * @param vars
     * @param comment
     */
    public void complete(String taskId, Map<String,Object> vars, String comment);

    /**
     * 我的待办
     * @param userId
     * @return
     */
    public List<Task> taskList(String userId, int pageNo, int pageSize);

    /**
     * 我的已办
     * @param userId
     * @return
     */
    public List<HistoricTaskInstance> taskedList(String userId, int pageNo, int pageSize);

    /**
     * 我的办结
     * @param userId
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<HistoricProcessInstance> taskThrough(String userId, int pageNo, int pageSize);
    /**
     * 流程跟踪
     * @param pid
     * @param response
     */
    public void traceProcess(String pid, String resourceName, HttpServletResponse response);

    /**
     * 流程终止
     * @param pid
     */
    public void endProcess(String pid,String reason);

    /**
     * 获取流程审批记录
     * @param pid
     * @return
     */
    public Object getComment(String pid);

    /**
     * 统计今日办理
     * @param userId
     * @return
     */
    public Object getCount(String userId);

    public String getProcessDefKey();
}
