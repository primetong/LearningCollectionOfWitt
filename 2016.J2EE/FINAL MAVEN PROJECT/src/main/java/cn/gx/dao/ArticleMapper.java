package cn.gx.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.gx.model.Article;

public interface ArticleMapper {
	int deleteByPrimaryKey(Integer aid);

	int insert(Article record);

	int insertSelective(Article record);

	Article selectByPrimaryKey(Integer aid);

	int updateByPrimaryKeySelective(Article record);

	int updateByPrimaryKeyWithBLOBs(Article record);

	int updateByPrimaryKey(Article record);

	List<Article> selectAllByUid(@Param("uid") Integer uid,
			@Param("offset") Integer offset, @Param("count") Integer count);

	List<Article> selectPublicByUid(@Param("uid") Integer uid,
			@Param("offset") Integer offset, @Param("count") Integer count);

	List<Article> selectPublicByKeyword(@Param("keyword") String keyword,
			@Param("offset") Integer offset, @Param("count") Integer count);

	Integer countAllByUid(@Param("uid") Integer uid);
	
	Integer countPublicByUid(@Param("uid") Integer uid);
	
	Integer countPublicByKeyword(@Param("keyword") String keyword);

}