package cn.gx.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.gx.helper.ListHelper;
import cn.gx.model.User;
import cn.gx.service.ArticleService;
import cn.gx.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@RequestMapping(value = "/{username:(?:[\\w]|[\\u4e00-\\u9fa5])+}")
	public String userInfo(HttpServletRequest request, ModelMap map,
			@ModelAttribute("username") String username) {

		User hostUser = null;
		{ /* 检查参数 */
			if (username == null || request == null || map == null
					|| username.length() < 4 || username.length() > 16)
				return "404";
			hostUser = userService.getUserByMailOrUsername(username);
			if (hostUser == null)
				return "404";
		}

		ListHelper listHelper = new ListHelper();
		{ /* 设定页面 */
			Integer pageNumber;
			try {				
				pageNumber = Integer.valueOf(request.getParameter("page"));
			} catch (NumberFormatException e) {
				pageNumber = 1;
			}
			listHelper.setCurrentPage(pageNumber);
			listHelper.setUrl(request.getRequestURI());
			
			User user = getUser(request);
			boolean isAll = user != null && hostUser.equals(user);
			articleService.searchArticleByUid(hostUser.getUid(), isAll, listHelper, userService);
			
			if (!isAll) {
				listHelper.setTitle(hostUser.getUsername() + " 的作品");
				if (listHelper.getData().isEmpty()) {
					listHelper.setIntro("OTZ，没有一个字");
				} else {
					listHelper.setIntro(null);
				}
			} else {
				listHelper.setTitle("我的作品");
				if (listHelper.getData().isEmpty()) {
					listHelper.setIntro("写点什么吧？");
				} else {
					listHelper.setIntro(null);
				}
			}
			
		}
		
		map.put("hostUser", hostUser);
		map.put("listHelper", listHelper);
		map.put("userDisplayType", "READ");
		return "user";
	}

	@RequestMapping(value = "/{username:(?:[\\w]|[\\u4e00-\\u9fa5])+}/settings")
	public String userSettings(HttpServletRequest request, ModelMap map,
			@ModelAttribute("username") String username) {

		User hostUser = null;
		{ /* 检查参数 */
			if (username == null || request == null || map == null
					|| username.length() < 4 || username.length() > 16)
				return "404";
			hostUser = userService.getUserByMailOrUsername(username);
			if (hostUser == null)
				return "404";
		}

		User user = getUser(request);
		if (user != null && user.equals(hostUser)) { /* 修改信息 */
			map.put("hostUser", hostUser);
			map.put("userDisplayType", "EDIT");
			return "user";
		} else
			/* 其他人没有权限 */
			return "404";
	}

	private User getUser(HttpServletRequest request) {
		if (request == null)
			return null;
		HttpSession session = request.getSession();
		if (session == null)
			return null;
		else
			return (User) session.getAttribute("user");
	}

	
	public ArticleService getArticleService() {
		return articleService;
	}

	@Autowired
	public void setArticleService(ArticleService articleService) {
		this.articleService = articleService;
	}

	public UserService getUserService() {
		return userService;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	private ArticleService articleService;
	private UserService userService;
}

/*
 * 参考资料：
 * 
 * [Can't map regular expression - java.lang.IllegalArgumentException: The
 * number of capturing groups in the pattern segment]
 * (http://stackoverflow.com/questions
 * /20323848/cant-map-regular-expression-java-
 * lang-illegalargumentexception-the-number-of)
 */
