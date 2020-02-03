package org.jeecgframework.web.system.sms.util.task;

import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.web.system.sms.service.TSSmsServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * @ClassName:NoticeImTask 定时从NC获取原料入库数据
 * @Description: TODO
 * @date 2020-01-17 下午5:06:34
 * 
 */
@Service("noticeImTask")
public class NoticeImTask {

	@Autowired
	private TSSmsServiceI tSSmsService;
	@Autowired
	private SystemService systemService;
	
	/* @Scheduled(cron="0 0 01 * * ?") */
	public void run() {
		
	}
}
