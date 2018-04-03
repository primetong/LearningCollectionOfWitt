package cn.gx.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.gx.model.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer uid);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer uid);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

	List<User> getAll();
	
    Integer mail2Uid(String mail);

    Integer username2Uid(String username);

    User mail2User(String mail);
    
    User username2User(String username);
    
    Boolean isPasswordCorrect(@Param("uid")Integer uid, @Param("password")String password);
}