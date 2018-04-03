package cn.gx.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.gx.helper.ListHelper;
import cn.gx.model.User;
import cn.gx.service.ArticleService;
import cn.gx.service.UserService;

@Controller
@RequestMapping("/explore")
public class ExploreController {

	@RequestMapping(value = "")
	public String deletePaper(HttpServletRequest request, ModelMap map) {
		return "explore";
	}

	@RequestMapping(value="/search")
	public String deletePaper(HttpServletRequest request, ModelMap map,
			@RequestParam("keyword") String keyword) {

		{ /* 检查参数 */
			if (request == null || map == null)
				return "404";
			if (keyword == null || keyword.isEmpty())
				return "redirect:/explore.html";
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
			articleService.searchArticleByKeyword(keyword, listHelper,
					userService);
			
			listHelper.setTitle("关于：" + keyword);
			if (listHelper.getData().isEmpty()) {
				listHelper.setIntro("没有找到相关内容");
			} else {
				listHelper.setIntro("找到了 " + listHelper.getTotalElement() + " 个匹配的词条");
			}
		}

		map.put("listHelper", listHelper);

		return "explore";
	}

	@SuppressWarnings("unused")
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
