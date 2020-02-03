package com.zzjee.flow.web;

import com.zzjee.flow.service.UpMaterialsFlowServiceI;
import com.zzjee.md.entity.MdPalletEntity;
import com.zzjee.md.service.MdPalletServiceI;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 托盘上架流程
 */
@Controller
@RequestMapping("/flow/upMaterials")
public class UpMaterialsFlowController {
    @Resource
    private RepositoryService repositoryService;
    @Resource
    private MdPalletServiceI mdPalletService;

    @Resource
    private UpMaterialsFlowServiceI upMaterialsFlowService;

    /*@PostConstruct
    public void deployment(){
        long count = repositoryService.createDeploymentQuery().deploymentId(upMaterialsFlowService.getProcessDefKey()).count();
        if(count <= 0){
            DeploymentBuilder builder = repositoryService.createDeployment();
            builder.addClasspathResource("diagram/up_materials.bpmn")
                    .addClasspathResource("diagram/up_materials.jpg")
                    .deploy();
            builder.enableDuplicateFiltering();
        }
    }*/

    @RequestMapping("/getMdPalletInfo")
    @ResponseBody
    public Object getMdPalletInfo(String palletCode){
        MdPalletEntity pallet = mdPalletService.findUniqueByProperty(MdPalletEntity.class,"tuoPanBianMa",palletCode);
        return pallet;
    }

}
