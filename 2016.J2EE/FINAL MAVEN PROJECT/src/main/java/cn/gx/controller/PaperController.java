package cn.gx.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.gx.model.Article;
import cn.gx.model.User;
import cn.gx.service.ArticleService;

@Controller
@RequestMapping("/paper")
public class PaperController {

	@RequestMapping(value = "/edit")
	public String createPaper(HttpServletRequest request, ModelMap map) {
		System.out.println("new paper!" + request.getRequestURI());

		Article paper = new Article();
		map.put("article", paper);
		map.put("paperDisplayType", "EDIT");
		return "paper";
	}

	@RequestMapping(value = "/edit/{aid:[\\d]+}")
	public String editPaper(HttpServletRequest request, ModelMap map,
			@ModelAttribute("aid") Integer aid) {
		
		System.out.println("edit paper!");
		
		Article paper;
		{ /* 检查参数是否合法 */
			if (map == null || aid == null || aid <= 0)
				return "404";
			paper = articleService.selectArticleById(aid);
			if (paper == null)
				return "404";
		}
	
		map.put("article", paper);
		map.put("paperDisplayType", "EDIT");
		return "paper";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String savePaper(HttpServletRequest request,	
			RedirectAttributes redirectAttrs, Article paper) {

		System.out.println("saving paper!");
		
		System.out.println("Ti:" + paper.getTitle());
		System.out.println("Co:" + paper.getContent());
		System.out.println("CD:" + paper.getCreationdate());
		System.out.println("Ai:" + paper.getAid());
		System.out.println("Ui:" + paper.getUid());
		System.out.println("Ip:" + paper.getIsprivate());
		
		/* 验证参数 */
		if (request == null || paper == null || redirectAttrs == null)
			return "404";
		else if (paper.getTitle() == null || paper.getTitle().isEmpty())
			redirectAttrs.addFlashAttribute("edit_feedback_message", "标题不能为空");
		else if (paper.getContent() == null || paper.getContent().isEmpty())
			redirectAttrs.addFlashAttribute("edit_feedback_message", "内容不能为空");
		else {			
			/* 获取 User */
			User user = getUser(request);
			
			if (user == null)
				redirectAttrs.addFlashAttribute("edit_feedback_message", "您还没有登陆");
			else { /* 保存 Article */
				boolean isOK = false;
				
				paper.setIsprivate(request.getParameter("isprivate") != null);
				if (paper.getAid() != null && paper.getAid() > 0) { /* 更新文章 */
					if (paper.getUid().equals(user.getUid()))
						isOK = articleService.update(paper) > 0;
					else
						redirectAttrs.addFlashAttribute("edit_feedback_message", "这篇文章不属于您");
				} else { /* 创建文章 */
					paper.setUid(user.getUid());
					paper.setCreationdate(new java.util.Date());
					isOK = articleService.insert(paper) > 0;
				}
				
				if (isOK) { /* 修改成功 */
					return "redirect:/paper/" + paper.getAid() + ".html";
				} else {
					redirectAttrs.addFlashAttribute("edit_feedback_message", "修改失败，请稍后再试");
				}
			}
		}

		redirectAttrs.addFlashAttribute("article", paper);
		return getLastUrl(request);
	}

	@RequestMapping(value = "/{aid:[\\d]+}")
	public String fetchPaper(ModelMap map, @ModelAttribute("aid") Integer aid) {

		System.out.println("fetch paper!:" + aid);
		
		Article paper;
		{ /* 检查参数是否合法 */
			if (map == null || aid == null || aid <= 0)
				return "404";
			paper = articleService.selectArticleById(aid);
			if (paper == null)
				return "404";
		}

		map.put("article", paper);
		map.put("paperDisplayType", "READ");
		return "paper";
	}
	
	@RequestMapping(value = "/delete/{aid:[\\d]+}")
	public String deletePaper(HttpServletRequest request, ModelMap map,
			@ModelAttribute("aid") Integer aid) {
		
		User user;
		{ /* 检查参数 */
			if (aid == null || request == null)
				return "404";
			user = getUser(request);
			if (user == null)
				return "404";
			Article paper = articleService.selectArticleById(aid);
			if (paper == null || paper.getUid().compareTo(user.getUid()) != 0)
				return "404";
		}
		
		/* 
		 * 删除多余的 aid，这个 aid 被 @ModelAttribute 加入到 Model 中
		 * 如果不删除，将会作为参数显示在跳转链接
		 */
		map.remove("aid");
		
		String url = getLastUrl(request);
		if (articleService.delete(aid) <= 0)
			return url;
		else
			return "redirect:/user/" + user.getUsername() + ".html";
	}

	private String getLastUrl(HttpServletRequest request) {
		String url = request.getHeader("Referer");
		System.out.println("跳转：" + url);

		if (url != null)
			return "redirect:" + url;
		else
			return "redirect:/";
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

	private ArticleService articleService;
}

/*
 * 参考资料：
 * 
 * [Spring mvc 中 @Requestmapping 再探]
 * (http://blog.csdn.net/jackyrongvip/article/details/9287281)
 */