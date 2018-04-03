package cn.gx.service;

import cn.gx.helper.ListHelper;
import cn.gx.model.Article;

public interface ArticleService {

	int insert(Article article);

	int update(Article article);

	int delete(int id);

	Article selectArticleById(int id);

	void searchArticleByUid(int uid, boolean isAll, ListHelper listHelper,
			UserService userService);

	void searchArticleByKeyword(String keyword, ListHelper listHelper,
			UserService userService);
}
