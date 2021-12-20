package com.jianzhi;

import java.io.FileInputStream;
import java.io.InputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import com.jianzhi.model.Pxbm;

import com.jianzhi.service.impl.PxbmServiceImpl;
import com.sun.prism.shader.Solid_TextureYV12_AlphaTest_Loader;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.log4j.Log4jImpl;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.jianzhi.mapper.QualificationsMapper;
import com.jianzhi.model.Qualifications;
import com.jianzhi.model.QualificationsDetail;
import com.jianzhi.service.SpotcheckmemberService;
import com.jianzhi.util.RandomNums;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


//@SpringBootTest
class SpotcheckApplicationTests {
	
	@Autowired
	QualificationsMapper qualificationsMapper;
	@Autowired
	SpotcheckmemberService spotcheckmemberService;
	

	@Test
	void contextLoads() {

		/*JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
		String url="http://localhost:8088/SSMCXF_exploded/webservice/pxbm?wsdl";
		Client client = dcf.createClient(url);
		String method = "updatePxbm";
		try {

			Object[] invoke = client.invoke(method, "0","0","0","0","D-1234567890","D1234567890");

			System.out.println(invoke[0]);



		} catch (Exception e) {
			e.printStackTrace();
		}

		 */

		//创建 JaxWsDynamicClientFactory 工厂实例
		JaxWsDynamicClientFactory factory=JaxWsDynamicClientFactory.newInstance();
		//根据服务地址创建客户端
		Client client=factory.createClient("http://localhost:8088/SSMCXF_exploded/webservice/pxbm?wsdl");
		Object [] result;
		try {

			result=client.invoke("updatePxbm", "0","0","0","0","D-1234567890","D1234567890");
			int user=(int) result[0];
			System.out.println(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void sx(){
		System.out.println(LocalDate.now());
		LocalDate date=LocalDate.now();
		System.out.println(date.getMonth());
		System.out.println(date.getMonthValue());
		System.out.println(date.getDayOfMonth());
		LocalDate aths=LocalDate.of(1999,12,13);

		System.out.println(aths.plusDays(10000));
		//在当前的日期减去参数中的天数得到一个月的第一天，minusDays:生成当前日期之后或之前的n的日期，按照先算括号里面的值再算括号外面的值
		//date.minusDays用来获取月份的第一天，这个时间函数原本已经获得当前天数，想要得到第一天，需要相加减，所以就在括号里面进行，因此配合里面的参数来进行获取，参数：当前天数-1
		System.out.println(date.minusDays(date.getDayOfMonth()-1));

		//获取星期几
		DayOfWeek dayOfWeek=date.getDayOfWeek();
		System.out.println(dayOfWeek.getValue());
		System.out.println();
	}

	@Test
	public void  ld(){
		System.out.println("漏洞排查");
	}
}

