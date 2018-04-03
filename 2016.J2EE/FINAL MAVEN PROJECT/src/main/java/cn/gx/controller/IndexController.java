package cn.gx.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.gx.helper.ListHelper;
import cn.gx.service.ArticleService;
import cn.gx.service.UserService;

@Controller
@RequestMapping("/")
public class IndexController {
	
	@RequestMapping("/index")
    public String index(HttpServletRequest request, ModelMap map){
		
		{ /* 检查参数 */
			if (request == null || map == null)
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
			
			articleService.searchArticleByKeyword("", listHelper, userService);
			
			listHelper.setTitle("最新作品");
			listHelper.setIntro(null);			
		}
		
		map.put("listHelper", listHelper);
		map.put("userDisplayType", "READ");
        return "index";
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
 * 
 * 关于获取主页 Controller
 * 
 * 修改 web.xml 中 welcome-file-list 标签为 @RequestMapping("/index")
 * 中的 index + 条件（例如后缀 *.html），则主页 "/" 的定向将会转向 "index.html"
 * 
 * 参考：
 * [Spring MVC: how to create a default controller for index page?]
 * (http://stackoverflow.com/questions/5252065/spring-mvc-how-to-create-a-default-controller-for-index-page)
 */