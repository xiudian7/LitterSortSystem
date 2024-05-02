package cn.litterSortSystem.xiudian7.member.service.impl;

import cn.litterSortSystem.xiudian7.common.exception.LogicException;
import cn.litterSortSystem.xiudian7.common.redis.service.RedisService;
import cn.litterSortSystem.xiudian7.common.redis.util.RedisKeys;
import cn.litterSortSystem.xiudian7.common.util.AssertUtil;
import cn.litterSortSystem.xiudian7.member.UserInfo;
import cn.litterSortSystem.xiudian7.member.mapper.UserInfoMapper;
import cn.litterSortSystem.xiudian7.member.service.IUserInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static cn.litterSortSystem.xiudian7.common.util.Consts.VERIFY_CODE_VAI_TIME;

@Service
@Transactional
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {

    @Autowired
    private RedisService redisService;
    @Override
    //判断用户名是否唯一
    public boolean checkPhone(String username) {
        QueryWrapper<UserInfo> wrapper=new QueryWrapper<>();
        //LambdaQueryWrapper<UserInfo> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        wrapper.eq("username",username);
        UserInfo one=super.getOne(wrapper);
        return one != null;
    }

    @Override
    public Map<String, Object> login(String username, String password) {
        System.out.println(username);
        System.out.println(password);
        //判断参数不能为空
        AssertUtil.hasText(username,"用户名不能为空");
        AssertUtil.hasText(password,"密码不能为空");
        //手机号码格式正确
        if(!AssertUtil.validPhone(username))
            throw new LogicException("用户名格式错误！");
        //查询账号密码是否存在
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        wrapper.eq("password",password);
        UserInfo user = super.getOne(wrapper);
        if(user==null)
        throw new LogicException("账号或者密码错误！");
        //设置token返回前端
        String token= UUID.randomUUID().toString();
        Map<String,Object>map=new HashMap<>();
        map.put("token",token);
        map.put("user",user);
        System.out.println("前端得到的数据为：token:"+token+"+user:"+user);
        return map;
    }
    @Override
    public void sendVerifyCode(String username) {
        //创建验证码
        //设置六位的验证码
        String code=UUID.randomUUID().toString().replaceAll("-","").substring(0,6);
        //拼接验证码
        StringBuilder sb =new StringBuilder(80);
        sb.append("您注册的验证码是：").append(code).append(",请在").append(VERIFY_CODE_VAI_TIME).append("分钟之内使用！");
        //发送短信
        System.out.println(sb);
        String key=RedisKeys.VERIFY_CODE.join(username);
        //把上面key打印下
        //存入redis
        //redisService.setCacheObject(key,code);

        System.out.println(key);
        System.out.println(RedisKeys.VERIFY_CODE.getTime());
        System.out.println("redisservice is: "+redisService);
        //这一句报错，也没把数据搞到redis里面
        redisService.setCacheObject(key,code, RedisKeys.VERIFY_CODE.getTime(), TimeUnit.SECONDS);
//        redisService.setCacheObject(key,"agxs");
    }

    @Override
    public void regist(String username, String password, String rpassword, int gender, String verifyCode) {
        System.out.println(username);
        System.out.println(password);
        System.out.println(rpassword);
        System.out.println(gender);
        System.out.println(verifyCode);
        //判断参数不为空
        AssertUtil.hasText(username,"手机号不能为空！");
        AssertUtil.hasText(password,"密码不能为空！");
        AssertUtil.hasText(rpassword,"密码确认不能为空！");
        AssertUtil.hasText(verifyCode,"验证码不能为空！");
        //判断两次密码是否一致
        AssertUtil.isEquals(password,rpassword,"两次密码不一致！");
        //手机格式是否正确
        if(!AssertUtil.validPhone(username))
            throw new LogicException("手机号码格式错误！");
        //判断手机号码是否唯一
        if(this.checkPhone(username)){
            throw new LogicException("该手机号码已经被注册！");
        }
        //判断验证码是否一致
        String code=redisService.getCacheObject(username);
        if(verifyCode.equalsIgnoreCase(code)){
            throw new LogicException("验证码失效或验证码不正确！");
        }
        //用户注册
        UserInfo userInfo=new UserInfo();
        userInfo.setUsername(username);
        userInfo.setPassword(password);
        userInfo.setGender(gender);
        //添加进数据库
        super.save(userInfo);
    }


}
