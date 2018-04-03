package cn.gx.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.gx.model.User;
import cn.gx.service.UserService;

@Controller
@RequestMapping("/console")
public class ConsoleController {

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(HttpServletRequest request,
			RedirectAttributes redirectAttrs,
			@RequestParam("username") String username,
			@RequestParam("password") String password) {
		HttpSession session = getSession(request);
		{ /* 获取 session */
			if (session == null || redirectAttrs == null)
				return "400";
		}

		/* 获取 UID 并验证登录 */
		User user = (User) session.getAttribute("user");
		if (user == null) {

			if (username != null && password != null && !username.isEmpty()
					&& !password.isEmpty()) {
				Integer uid = userService.getUidByMailOrUsername(username);
				if (uid != null && userService.isPasswordCorrect(uid, password)) { /* 登陆成功 */
					user = userService.selectByPrimaryKey(uid);
					session.setAttribute("user", user);
					System.out.println(uid + "：登录成功");
				} else { /* 用户名或者密码错误 */
					redirectAttrs.addFlashAttribute("login_feedback_message",
							formatFeedback("用户名或者密码错误！"));
				}
			} else {
				if (username == null || username.isEmpty())
					redirectAttrs.addFlashAttribute("login_feedback_username",
							formatFeedback("（请输入用户名）"));
				if (password == null || password.isEmpty())
					redirectAttrs.addFlashAttribute("login_feedback_password",
							formatFeedback("（请输入密码）"));
			}
		} else { /* 用户已经登录 */
			redirectAttrs.addFlashAttribute("login_feedback_message",
					formatFeedback("您已经登录！"));
		}

		return getLastUrl(request);
	}

	@RequestMapping(value = "/logout")
	public String logout(HttpServletRequest request) {
		HttpSession session = getSession(request);
		{ /* 获取 session */
			if (session == null)
				return "400";
		}

		User user = (User) session.getAttribute("user");
		if (user != null) { /* 顺利登出 */
			System.out.println(user.getUsername() + "：完成登出");
			session.removeAttribute("user");
		} else { /* 没有用户登录 */
			System.out.println("无法登出"); // TODO: 回执
		}

		return getLastUrl(request);
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signin(HttpServletRequest request,
			RedirectAttributes redirectAttrs,
			@RequestParam("email") String email,
			@RequestParam("username") String username,
			@RequestParam("password") String password,
			@RequestParam("password2nd") String password2nd) {

		HttpSession session = getSession(request);
		{ /* 获取 session */
			if (request == null || redirectAttrs == null)
				return "400";
		}

		int count = 0;
		{
			/* 验证用户名 */
			String msg = checkUsername(username);
			if (msg.isEmpty())
				count++;
			else
				redirectAttrs.addFlashAttribute("signup_feedback_username",
						formatFeedback(msg));

			/* 验证邮箱 */
			msg = checkEmail(email);
			if (msg.isEmpty())
				count++;
			else
				redirectAttrs.addFlashAttribute("signup_feedback_email",
						formatFeedback(msg));

			/* 验证密码 */
			msg = checkPassword(password, password2nd);
			if (msg.isEmpty())
				count++;
			else
				redirectAttrs.addFlashAttribute("signup_feedback_password",
						formatFeedback(msg));
		}

		if (count == 3) { /* 注册并登陆用户 */
			User user = new User();
			user.setUsername(username);
			user.setPassword(password);
			user.setMail(email);
			user.setRegistrationdate(new java.util.Date());

			if (userService.insert(user) != 0) /* 注册成功 */
				session.setAttribute("user", user);
			else
				redirectAttrs.addFlashAttribute("signup_feedback_message",
						formatFeedback("注册失败，请稍后再试"));
		}

		return getLastUrl(request);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(HttpServletRequest request,
			RedirectAttributes redirectAttrs,
			@RequestParam("email") String email,
			@RequestParam("username") String username,
			@RequestParam("passwordold") String passwordold,
			@RequestParam("password1st") String password1st,
			@RequestParam("password2nd") String password2nd) {

		HttpSession session = getSession(request);
		User user;
		{ /* 获取 session */
			if (request == null || redirectAttrs == null)
				return "400";
			user = (User) session.getAttribute("user");
			if (user == null)
				return "400";
		}

		boolean noError = true, hasChanged = false;
		User newUser = new User();
		{
			/* 验证用户名 */
			if (username != null && !username.isEmpty() && !username.equals(user.getUsername())) {
				String msg = checkUsername(username);
				if (msg.isEmpty()){
					newUser.setUsername(username);
					hasChanged = true;
				} else {
					redirectAttrs.addFlashAttribute("update_feedback_username",
							formatFeedback(msg));
					noError = false;
				}
			}

			/* 验证邮箱 */
			if (email != null && !email.isEmpty() && !email.equals(user.getMail())) {
				String msg = checkEmail(email);
				if (msg.isEmpty()) {
					newUser.setMail(email);
					hasChanged = true;
				} else {
					redirectAttrs.addFlashAttribute("update_feedback_email",
							formatFeedback(msg));
					noError = false;
				}
			}

			/* 验证密码 */

			if ((passwordold != null && !passwordold.isEmpty())
					|| (password1st != null && !password1st.isEmpty())
					|| (password2nd != null && !password2nd.isEmpty())) {
				
				if (passwordold != null && !passwordold.isEmpty()
						&& password1st != null && !password1st.isEmpty()
						&& password2nd != null && !password2nd.isEmpty()) {
				
					String msg = checkPassword(password1st, password2nd);
					if (msg.isEmpty()) {
						if (userService.isPasswordCorrect(user.getUid(), passwordold))  {
							newUser.setPassword(password1st);
							hasChanged = true;							
						} else {
							redirectAttrs.addFlashAttribute("update_feedback_password",
									formatFeedback("（密码错误）"));
							noError = false;
						}
					} else {
						redirectAttrs
						.addFlashAttribute("update_feedback_password",
								formatFeedback(msg));
						noError = false;
					}
				} else {
					redirectAttrs.addFlashAttribute("update_feedback_password",
							formatFeedback("（当前的填写不完整）"));
					noError = false;
				}
			}
		}

		String oldUsername = user.getUsername();
		if (noError && hasChanged) { /* 更新用户 */
			if (userService.update(newUser) > 0) {
				user = userService.selectByPrimaryKey(user.getUid());
				session.setAttribute("user", user);
			} else
				redirectAttrs.addFlashAttribute("update_feedback_message",
						formatFeedback("修改失败，请稍后再试"));
		}
		
		return getLastUrl(request).replace(oldUsername, user.getUsername());
	}

	private String formatFeedback(String message) {
		return "<em class=\"alert\">" + message + "</em>";
	}

	private HttpSession getSession(HttpServletRequest request) {
		if (request == null)
			return null;
		return request.getSession();
	}

	private String getLastUrl(HttpServletRequest request) {
		String url = request.getHeader("Referer");
		System.out.println("跳转：" + url);

		if (url != null)
			return "redirect:" + url;
		else
			return "redirect:/";
	}

	public String checkEmail(String email) {

		Pattern p = Pattern
				.compile("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$");
		Matcher m = p.matcher(email);
		String msg = new String();

		if (!m.matches())
			msg = "（邮箱格式不正确）";
		else if (userService.getUidByMailOrUsername(email) != null)
			msg = "（该邮箱已经被使用）";

		return msg;
	}

	public String checkPassword(String password, String password2nd) {
		Pattern p = Pattern.compile("([\\w])+");
		Matcher m = p.matcher(password);

		String msg = new String();
		if (!password.equals(password2nd))
			msg = "（两次输入的密码不一致）";
		else if (password.length() < 6 || password.length() > 32)
			msg = "（应该为 6 至 32 位）";
		else if (!m.matches())
			msg = "（当前密码包含了非法字符）";

		return msg;
	}

	public String checkUsername(String username) {

		String msg = new String();
		Pattern p = Pattern.compile("([\\w]|[\\u4e00-\\u9fa5])+");
		Matcher m = p.matcher(username);

		if (username.length() < 4 || username.length() > 16)
			msg = "（长度应该为 4 至 16 位）";
		else if (!m.matches())
			msg = "（当前用户名包含了非法字符）";
		else if (userService.getUidByMailOrUsername(username) != null)
			msg = "（该用户名已经被使用）";
		else if (username.equals("user"))
			msg = "（您不能使用这个名字）";
		
		return msg;
	}

	private UserService userService;

	public UserService getUserService() {
		return userService;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
