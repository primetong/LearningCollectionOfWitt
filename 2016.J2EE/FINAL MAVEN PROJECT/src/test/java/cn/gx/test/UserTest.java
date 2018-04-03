package cn.gx.test;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.gx.model.User;
import cn.gx.service.UserService;

public class UserTest {

	private UserService userService;

	@Before
	public void before() {
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "classpath:spring.xml",
						"classpath:spring-mybatis.xml" });

		userService = (UserService) context.getBean("userService");
	}

	@Test
	public void addUser() {

		String alphabeta = "abcdefghijklmnopqrstuvwxyz";
		String username = new String();
		for (int i = 0; i < 9; i++)
			username += alphabeta.charAt((int) (Math.random() * 26));

		User user = new User();
		user.setUsername(username);
		user.setPassword("123456");
		user.setMail(username + "@test.com");
		user.setRegistrationdate(new java.util.Date());

		if (userService.insert(user) != 0)
			System.out.println("User<" + username + "|" + user.getUid()
					+ "> is inserted!");
		else
			System.out.println("failed to insert a user!");

		/*
		 * 参考资料：
		 * 
		 * [MyBatis 插入时候获取自增主键方法]
		 * (http://lavasoft.blog.51cto.com/62575/1384959/)
		 */
	}
}