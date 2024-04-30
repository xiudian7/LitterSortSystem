package cn.litterSortSystem.xiudian7.member.service.impl;

import cn.litterSortSystem.xiudian7.common.exception.LogicException;
import cn.litterSortSystem.xiudian7.common.util.AssertUtil;
import cn.litterSortSystem.xiudian7.member.UserInfo;
import cn.litterSortSystem.xiudian7.member.mapper.UserInfoMapper;
import cn.litterSortSystem.xiudian7.member.service.IUserInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {
    @Override
    public Map<String, Object> login(String username, String password) {
        //判断参数不能为空
        AssertUtil.hasText(username,"用户名不能为空");
        AssertUtil.hasText(password,"密码不能为空");
        //手机号码格式正确
        if(!AssertUtil.validPhone(username))
            throw new LogicException("用户名格式错误！");
        //查询账号密码是否存在
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("phone",username);
        wrapper.eq("password",password);
        UserInfo user = super.getOne(wrapper);
        if(user==null)
        throw new LogicException("账号或者密码错误！");
        //设置token返回前端
        String token= UUID.randomUUID().toString();
        Map<String,Object>map=new HashMap<>();
        map.put("token",token);
        map.put("user",user);
        return map;
    }
}
