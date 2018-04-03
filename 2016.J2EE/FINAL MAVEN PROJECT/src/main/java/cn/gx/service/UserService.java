package cn.gx.service;

import java.util.List;

import cn.gx.model.User;


public interface UserService {
	
	List<User> getAll();
	
	User selectByPrimaryKey(Integer id);
	
    int insert(User user);
    
    int update(User user);
    
    int delete(Integer id);
    
    Integer getUidByMailOrUsername(String mailOrUsername);
    
    User getUserByMailOrUsername(String mailOrUsername);
    
    boolean isPasswordCorrect(int uid, String password);
}