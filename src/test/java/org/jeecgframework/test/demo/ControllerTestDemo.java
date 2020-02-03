package org.jeecgframework.test.demo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.zzjee.flow.util.TaskStatus;
import com.zzjee.md.entity.MdMovePalletEntity;
import com.zzjee.wm.entity.WmImNoticeHEntity;
import com.zzjee.wm.entity.WmOmNoticeHEntity;
import com.zzjee.wm.entity.WmOmNoticeIEntity;
import com.zzjee.wm.page.WmImNoticeHPage;
import com.zzjee.wm.page.WmOmNoticeHPage;
import com.zzjee.wm.service.WmImNoticeHServiceI;
import com.zzjee.wm.service.WmOmNoticeHServiceI;
import com.zzjee.wmutil.uasUtil;
import com.zzjee.wmutil.yyUtil;
import org.apache.commons.fileupload.util.LimitedInputStream;
import org.jeecgframework.AbstractUnitTest;
import org.jeecgframework.core.util.DateUtils;
import org.jeecgframework.web.system.service.SystemService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller 单元测试Demo
 * @author  许国杰
 */
public class ControllerTestDemo  extends AbstractUnitTest{

	private MockMvc mockMvc;
	private MockHttpSession session; //为模拟登录时，所有请求使用同一个session
	@Before
	public void setup() throws Exception {
		MockHttpServletRequestBuilder requestBuilder = post("/");
		MockHttpServletRequest request = requestBuilder.buildRequest(this.wac.getServletContext());
		session = (MockHttpSession) request.getSession();
		this.mockMvc = webAppContextSetup(this.wac).alwaysExpect(status().isOk()).build();
		this.testLogin(); //先调用登录
	}
	//测试登录
	@Test
	public void testLogin() throws Exception {
//        yyUtil.getProduct();
//		Map<String, Object> paramMap = new HashMap<String, Object>();
//		paramMap.put("formDate","2017-01-01");
//		paramMap.put("lastUpdateTime","2017-01-01");
//		paramMap.put("pi_class","采购验收单");
//		uasUtil.getProduct(paramMap);
//
//		uasUtil.getCustomer(paramMap);
//
//		uasUtil.getVendor(paramMap);
//
//		uasUtil.getWarehouse(paramMap);
//
//		uasUtil.getBil(paramMap);
 //		session.setAttribute("randCode", "1234"); //设置登录验证码
//		this.mockMvc.perform(post("/loginController.do?checkuser=")
//				.param("userName","admin")
//				.param("password", "c44b01947c9e6e3f")
//				.param("randCode", "1234")
//				.header("USER-AGENT", "")  // 设置USER-AGENT： 浏览器
//				.session(session))  //关键 每个测试都要设置session 。以保证是使用的同一个session
//				.andDo(print())
//				.andExpect(jsonPath("$.success").value(Matchers.equalTo(true)));
	}
	//验证返回view 是否正确
	@Test
	public void testAorudemo() throws Exception {
//		MockHttpServletRequestBuilder requestBuilder = post("/demoController.do?aorudemo=")
//			.param("type","table")
//			.header("USER-AGENT", "")  // 设置USER-AGENT： 浏览器
//			.session(session);
//
//		this.mockMvc.perform(requestBuilder)
//				.andDo(print()) //打印报文
//				.andExpect(view().name(containsString("jeecg/demo/base/tabledemo"))); //验证返回view 是否不正确
	}

	//使用jsonPath 验证返回json 的属性
	@Test
	public void testPDemoList() throws Exception{

//		MockHttpServletRequestBuilder requestBuilder = post("/userController.do?datagrid=")
//	    .param("field", "id")
//		.header("USER-AGENT", "")  // 设置USER-AGENT： 浏览器
//		.session(session);
//
//		this.mockMvc.perform(requestBuilder)
//		.andDo(print()) //打印报文
//		.andExpect(jsonPath("$.rows[0].id").exists()); // 验证id 属性是否存在


	}

	@Test
	public void testService(){
        WmOmNoticeHServiceI service = (WmOmNoticeHServiceI) wac.getBean("wmOmNoticeHService");
		List<WmOmNoticeIEntity> list = service.findUnloadTask("21");
		for (WmOmNoticeIEntity page : list){
			System.out.println(page.toString());
		}
	}

	@Test
	public void testHql(){
		SystemService service = (SystemService) wac.getBean("systemService");

		String hql = "from MdMovePalletEntity md where md.status=?";
		List<MdMovePalletEntity> moves = service.findHql(hql, TaskStatus.FINISHED);

		hql = "from MdMovePalletEntity md where md.status=? and type in ('上架','下架','码垛','拆垛') limit?";
		moves = service.findHql(hql, TaskStatus.FINISHED, 10);

		hql = "from MdMovePalletEntity md where md.status=? and type in ('上架','下架','码垛','拆垛') order by md.updateDate desc limit?";
		moves = service.findHql(hql, TaskStatus.FINISHED, new Date(), 10);

		/*hql = "from MdMovePalletEntity md where md.status=? and type in ('上架','下架','码垛','拆垛') order by md.updateDate desc limit ?";
		moves = service.findHql(hql, TaskStatus.FINISHED, new Date(), 10);

		hql = "from MdMovePalletEntity md where md.status=? and type in ('上架','下架','码垛','拆垛') order by md.updateDate desc limit ?";
		moves = service.findHql(hql, TaskStatus.FINISHED, new Date(), 10);*/
	}
}
