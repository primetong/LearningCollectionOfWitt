package cn.gx.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.gx.dao.ArticleMapper;
import cn.gx.helper.ListHelper;
import cn.gx.model.Article;
import cn.gx.service.ArticleService;
import cn.gx.service.UserService;

@Service("articleService")
public class ArticleServiceImpl implements ArticleService {

	@Override
	public int insert(Article article) {

		return mapper.insert(article);
	}

	@Override
	public int update(Article article) {
		return mapper.updateByPrimaryKeySelective(article);
	}

	@Override
	public int delete(int id) {
		return mapper.deleteByPrimaryKey(id);
	}

	@Override
	public Article selectArticleById(int id) {
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public void searchArticleByUid(int uid, boolean isAll,
			ListHelper listHelper, UserService userService) {
		if (isAll)
			listHelper.setTotalElement(mapper.countAllByUid(uid));
		else
			listHelper.setTotalElement(mapper.countPublicByUid(uid));

		int offset = listHelper.getOffset();
		int count = ListHelper.getElementPerPage();

		if (isAll)
			listHelper.setData(mapper.selectAllByUid(uid, offset, count),
					userService);
		else
			listHelper.setData(mapper.selectPublicByUid(uid, offset, count),
					userService);
	}

	@Override
	public void searchArticleByKeyword(String keyword, ListHelper listHelper,
			UserService userService) {
		listHelper.setTotalElement(mapper.countPublicByKeyword(keyword));
		int offset = listHelper.getOffset();
		int count = ListHelper.getElementPerPage();
		listHelper.setData(
				mapper.selectPublicByKeyword(keyword, offset, count),
				userService);
	}

	private ArticleMapper mapper;

	public ArticleMapper getMuserMapper() {
		return mapper;
	}

	@Autowired
	public void setArticleMapper(ArticleMapper mapper) {
		this.mapper = mapper;
	}
}
