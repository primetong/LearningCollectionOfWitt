package cn.gx.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.gx.dao.UserMapper;
import cn.gx.model.User;
import cn.gx.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {

    private UserMapper userMapper;
    
    public UserMapper getMuserMapper() {
		return userMapper;
	}

	@Autowired
	public void setMuserMapper(UserMapper userMapper) {
		this.userMapper = userMapper;
	}
	
	@Override
	public int insert(User user) {
		return userMapper.insert(user);
	}

	@Override
	public int update(User user) {
		return userMapper.updateByPrimaryKeySelective(user);
	}

	@Override
	public int delete(Integer id) {
		return userMapper.deleteByPrimaryKey(id);
	}

	@Override
	public List<User> getAll() {
		return userMapper.getAll();
	}

	@Override
	public User selectByPrimaryKey(Integer id) {
		return userMapper.selectByPrimaryKey(id);
	}

	@Override
	public Integer getUidByMailOrUsername(String mailOrUsername) {
		if (mailOrUsername.contains("@"))
			return userMapper.mail2Uid(mailOrUsername);
		else 
			return userMapper.username2Uid(mailOrUsername);
	}

	@Override
	public boolean isPasswordCorrect(int uid, String password) {
		return userMapper.isPasswordCorrect(uid, password) != null;
	}

	@Override
	public User getUserByMailOrUsername(String mailOrUsername) {
		if (mailOrUsername.contains("@"))
			return userMapper.mail2User(mailOrUsername);
		else 
			return userMapper.username2User(mailOrUsername);
	}
    
}
