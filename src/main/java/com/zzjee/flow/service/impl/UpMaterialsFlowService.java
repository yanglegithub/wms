package com.zzjee.flow.service.impl;

import com.zzjee.flow.service.UpMaterialsFlowServiceI;
import com.zzjee.md.entity.MdPalletEntity;
import com.zzjee.md.service.MdPalletServiceI;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Service
public class UpMaterialsFlowService implements UpMaterialsFlowServiceI {
    /**
     * 流程定义Key
     */
    private final String processDefKey = "up_materials";

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private HistoryService historyService;

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private TaskService taskService;

    @Resource
    private ProcessEngine processEngine;

    @Resource
    private MdPalletServiceI mdPalletService;


    @Override
    public void start(String businessKey, Map<String, Object> vars){
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefKey,businessKey,vars);
    }

    @Override
    public void complete(String taskId, Map<String, Object> vars, String comment) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if(task == null){
            return ;
        }

    }

    @Override
    public List<Task> taskList(String userId, int pageNo, int pageSize) {
        return null;
    }

    @Override
    public List<HistoricTaskInstance> taskedList(String userId, int pageNo, int pageSize) {
        return null;
    }

    @Override
    public List<HistoricProcessInstance> taskThrough(String userId, int pageNo, int pageSize) {
        return null;
    }

    @Override
    public void traceProcess(String pid, String resourceName, HttpServletResponse response) {

    }

    @Override
    public void endProcess(String pid, String reason) {

    }

    @Override
    public Object getComment(String pid) {
        return null;
    }

    @Override
    public Object getCount(String userId) {
        return null;
    }

    @Override
    public String getProcessDefKey() {
        return this.processDefKey;
    }
}
