package cn.gx.helper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import cn.gx.model.Article;
import cn.gx.model.ArticleInfo;
import cn.gx.model.User;
import cn.gx.service.UserService;

/**
 * 使用的时候，记得 setCurrentPage, setUrl
 */
@Component
public class ListHelper {
	
	public int getOffset() {
		if (currentPage < 0)
			currentPage = 0;
		return currentPage == 0 ? 0 : (currentPage - 1) * elementPerPage;
	}
	
	public String getCurrentPageString() {
		return String.valueOf(currentPage);
	}
	
	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public int getTotalElement() {
		return totalElement;
	}

	public void setTotalElement(int totalElement) {
		totalPage = ((totalElement + elementPerPage - 1) / elementPerPage);
		if (currentPage > totalPage)
			currentPage = totalPage;
		this.totalElement = totalElement;
	}

	public static int getElementPerPage() {
		return elementPerPage;
	}

	public static void setElementPerPage(int elementPerPage) {
		ListHelper.elementPerPage = elementPerPage;
	}

	public List<ArticleInfo> getData() {
		return data;
	}

	public void setData(List<Article> data, UserService userService) {
		this.data = new ArrayList<ArticleInfo>();
		for (Article article : data) {
			ArticleInfo info = new ArticleInfo();
			info.convert(article);
			
			/* 获取作者 */
			if (userService != null) {
				User user = userService.selectByPrimaryKey(article.getUid());
				if (user != null)
					info.setAuthor(user.getUsername());				
			}
			
			this.data.add(info);
		}
	}

	public List<String> getPageList() {
		if (totalPage == 0)
			return null;
		else if (currentPage >= totalPage)
			currentPage = totalPage;
		else if (currentPage < 0)
			currentPage = 1;
		
		int min = Math.max(1, currentPage - numberOfPageNumber / 2);
		int max = Math.min(totalPage, currentPage + numberOfPageNumber / 2);
		
		List<String> ret = new ArrayList<String>();
		
		String param = "?page=";
		
		if (currentPage != 1)
			ret.add("<a href=\"" + url + param + (currentPage - 1)  + "\"><span>上一页</span></a>");
		
		for (int i = min; i <= max; i++)
			if (currentPage != i)
				ret.add("<a href=\"" + url + param + i  + "\"><span>" + i + "</span></a>");
			else 
				ret.add("<span>" + String.valueOf(i) + "</span>");
	
		if (currentPage != totalPage)
			ret.add("<a href=\"" + url + param + (currentPage + 1)  + "\"><span>下一页</span></a>");
		
		return ret;
	}
	
	public static int getNumberOfPageNumber() {
		return numberOfPageNumber;
	}

	public static void setNumberOfPageNumber(int numberOfPageNumber) {
		ListHelper.numberOfPageNumber = numberOfPageNumber;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	private int currentPage;
	
	private int totalPage;
	
	private int totalElement;
	
	private static int elementPerPage = 10;
	
	private static int numberOfPageNumber = 7;
	
	private List<ArticleInfo> data;
	
	private String url;
	
	private String title;
	
	private String intro;
}
