package org.progingo.progingobi.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.progingo.progingobi.domain.dto.UserDTO;
import org.progingo.progingobi.domain.entity.User;
import org.progingo.progingobi.mapper.UserMapper;
import org.progingo.progingobi.util.JsonResult;
import org.progingo.progingobi.util.MyUtil;
import org.progingo.progingobi.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private MyUtil myUtil;


    public JsonResult login(User userVO) {
        if (userVO == null || userVO.getUsername() == null || userVO.getPassword() == null) {
            return JsonResult.ok(201,"登录失败","");
        }

        User user = userMapper.selectList(new LambdaQueryWrapper<User>()
                        .eq(User::getUsername, userVO.getUsername()))
                .stream().findFirst().orElse(null);

        if (user == null){
            return JsonResult.ok(202,"用户名或密码错误","");
        }
        String salt = user.getSalt();
        String password = new Md5Hash(userVO.getPassword(), salt, 3).toString();
        if (!user.getPassword().equals(password)){
            return JsonResult.ok(202,"用户名或密码错误","");
        }


        UserDTO userDTO = UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
        String token = tokenUtil.saveUserCache(userDTO);
        return JsonResult.ok(token);
    }

    public JsonResult sign(User user) {

        Long uNum = userMapper.selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, user.getUsername()));

        if (uNum != 0)
            return JsonResult.ok(202,"用户名重复","");


        String salt = UUID.randomUUID().toString().substring(0, 8);

        user.setPassword(new Md5Hash(user.getPassword(), salt, 3).toString());
        user.setSalt(salt);

        user.setCreateTime(LocalDateTime.now());

        int r = userMapper.insert(user);
        if (r != 1){
            return JsonResult.ok(203,"注册失败","");
        }
        return JsonResult.ok();
    }

    public JsonResult logout(String token) {
        tokenUtil.deleteUserCache(token);
        return JsonResult.ok();
    }

}
