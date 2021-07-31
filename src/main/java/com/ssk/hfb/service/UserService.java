package com.ssk.hfb.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ssk.hfb.common.enums.ExceptioneEnum;
import com.ssk.hfb.common.enums.TemplateEnum;
import com.ssk.hfb.common.exception.CommonAdviceException;
import com.ssk.hfb.common.pojo.InfoNum;
import com.ssk.hfb.common.result.SucessHandler;
import com.ssk.hfb.common.utils.CategoryId;
import com.ssk.hfb.common.utils.NumberUtils;
import com.ssk.hfb.common.vo.PageResult;
import com.ssk.hfb.mapper.*;
import com.ssk.hfb.pojo.*;
import com.ssk.hfb.utils.FormatDate;
import com.ssk.hfb.utils.SmsUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.concurrent.TimeUnit;
@Slf4j
@Service
public class UserService {


    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SmsUtil smsUtil;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private TopicActiveService activeService;
    @Autowired
    private AttentionService attentionService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private LoginLogService loginLogService;
    @Autowired
    private ChatListMapper chatListMapper;
    static final String KEY_PREFIX = "user:code:phone:";
    private static final String KEY_PREFIX_EMAIl = "user:code:email:";

    public User login(User u,LoginLog loginLog) {
        String ps = u.getPassword() + KEY_PREFIX;
        String md5Pass = DigestUtils.md5DigestAsHex(ps.getBytes());
        u.setPassword(md5Pass);

        User user = userMapper.selectOne(u);
        if (user == null) {
            throw new CommonAdviceException(ExceptioneEnum.NUMBER_OR_PASSWORD_ERROR);
        }
        loginLog.setAccount(u.getPhoneNumber());
        loginLog.setCreateTime(new Date());
        loginLog.setName(user.getUserName());
        if (StringUtils.isBlank(user.getIdentity())){
            loginLogService.save(loginLog);
        }
        return user;
    }


    public Boolean saveUser(User user) {
        int count = userMapper.insert(user);
        if (count == 0) {
            throw new CommonAdviceException(ExceptioneEnum.USER_SAVE_ERROR);
        }

        return true;
    }

    public User searchUserById(Integer id) {
        User user = new User();
        user.setId(id);
        User u = userMapper.selectOne(user);
        return u;
    }

    public void registerUser(User u) {
        String ps = u.getPassword() + KEY_PREFIX;
        if (u.getPassword().equals(u.getConfirmPassword())) {
            User user = new User();
            user.setPhoneNumber(u.getPhoneNumber());
            User user1 = userMapper.selectOne(user);
            if (user1 != null) {
                throw new CommonAdviceException(ExceptioneEnum.USER_NUMBER_EXIST);
            }
            String md5Pass = DigestUtils.md5DigestAsHex(ps.getBytes());
            u.setPassword(md5Pass);
            u.setCreateTime(new Date());
            u.setAuthorUrl("http://image.xquery.cn/2020eb384401aa616ba134126357_th.jpg");
            u.setGender(0);
            int count = userMapper.insert(u);
            if (count == 0) {
                throw new CommonAdviceException(ExceptioneEnum.USER_REGISTER_ERROR);
            }
            ChatList chatList = new ChatList();
            chatList.setFromId(u.getId());
            chatList.setToId(10000);
            chatList.setStatus(true);
            chatList.setCreateTime(new Date());
            chatList.setAfterTime(new Date());
            int insert = chatListMapper.insert(chatList);

            Message message = new Message();
            message.setBelong(u.getId());
            message.setCId(chatList.getId());
            message.setFromId(10000);
            message.setToId(u.getId());
            message.setStatus(true);
            message.setSendTime(new Date());
            message.setMessage("欢迎注册韩府帮！有什么问题都可以及时进行反馈！");
            Message message1 = messageService.saveMsg(message);
        } else {
            throw new CommonAdviceException(ExceptioneEnum.USER_PASSWORD_OTHER);
        }


    }

    public SucessHandler sendCode(String phone) {
        String code = NumberUtils.generateCode(4);
        String templateID = TemplateEnum.PHONE_LOGIN.getTemplateID();
        int time = TemplateEnum.PHONE_LOGIN.getTime();
        smsUtil.sendMessage(templateID, phone, code);
        this.redisTemplate.opsForValue().set(KEY_PREFIX + phone, code, time, TimeUnit.MINUTES);
        SucessHandler sucessHandler = new SucessHandler(new Integer(code), 200, "手机登录验证码");
        return sucessHandler;
    }
    public SucessHandler sendForgetCode(String phone) {
        String code = NumberUtils.generateCode(4);
        String templateID = TemplateEnum.PASSWORD_RESET.getTemplateID();
        int time = TemplateEnum.PASSWORD_RESET.getTime();
        smsUtil.sendMessage(templateID, phone, code);
        this.redisTemplate.opsForValue().set(KEY_PREFIX + phone, code, time, TimeUnit.MINUTES);
        SucessHandler sucessHandler = new SucessHandler(new Integer(code), 200, "手机重置验证码");
        return sucessHandler;
    }
    public User forgetPwd(String phone, String code, String password) {
        String key = KEY_PREFIX + phone;
        String ps = password + KEY_PREFIX;
        String md5Pass = DigestUtils.md5DigestAsHex(ps.getBytes());
        // 从redis取出验证码
        String codeCache = this.redisTemplate.opsForValue().get(key);
        // 检查验证码是否正确

        if (!code.equals(codeCache)) {
            // 不正确，返回
            throw new CommonAdviceException(ExceptioneEnum.CODE_IS_ERROR);
        }
        User user1 = new User();
        user1.setPhoneNumber(phone);
        User user = userMapper.selectOne(user1);
        if (user == null) {
            throw new CommonAdviceException(ExceptioneEnum.NUMBER_OR_PASSWORD_ERROR);
        }
        user.setPassword(md5Pass);
        int i = userMapper.updateByPrimaryKeySelective(user);
        return user;

    }

    public User loginCode(String phone, String code) {
        String key = KEY_PREFIX + phone;
        // 从redis取出验证码
        String codeCache = this.redisTemplate.opsForValue().get(key);
        // 检查验证码是否正确
        if (!code.equals(codeCache)) {
            // 不正确，返回
            throw new CommonAdviceException(ExceptioneEnum.CODE_IS_ERROR);
        }
        User user1 = new User();
        user1.setPhoneNumber(phone);
        User user = userMapper.selectOne(user1);
        if (user == null) {
            throw new CommonAdviceException(ExceptioneEnum.NUMBER_OR_PASSWORD_ERROR);
        }
        return user;

    }

    public void reSetPassword(int uId, String oldpassword, String newpassword, String renewpassword) {
        String ps = oldpassword + KEY_PREFIX;
        String ps1 = newpassword + KEY_PREFIX;

        if (!newpassword.equals(renewpassword)) {
            throw new CommonAdviceException(ExceptioneEnum.NUMBER_OR_PASSWORD_ERROR);
        }
        User user = new User();
        user.setId(uId);
        String md5Pass = DigestUtils.md5DigestAsHex(ps.getBytes());
        String md5Pass1 = DigestUtils.md5DigestAsHex(ps1.getBytes());
//        user.setPassword(md5Pass);
        User user1 = userMapper.selectByPrimaryKey(user);
        if (user1 != null) {
            if (!user1.getPassword().equals(md5Pass)) {
                throw new CommonAdviceException(ExceptioneEnum.INVALID_PASSWORD_ERROR);
            }
            user.setPassword(md5Pass1);

            int i = userMapper.updateByPrimaryKeySelective(user);
            if (i == 0) {
                throw new CommonAdviceException(ExceptioneEnum.NUMBER_OR_PASSWORD_ERROR);
            }
        } else {
            throw new CommonAdviceException(ExceptioneEnum.NUMBER_OR_PASSWORD_ERROR);
        }

    }

    public void bindEmail(int uId, String email, String code) {
        User user = new User();
        user.setId(uId);
        String key = KEY_PREFIX_EMAIl + email;
        String codeCache = this.redisTemplate.opsForValue().get(key);
        if (code.equals(codeCache)) {
            user.setEmail(email);
            int i = userMapper.updateByPrimaryKeySelective(user);
        }
    }

    public void updateUserInfo(User user) {
        user.setUpdateTime(new Date());
        int i = userMapper.updateByPrimaryKeySelective(user);
        if (i == 0) {
            throw new CommonAdviceException(ExceptioneEnum.USERINFO_UPDATE_RENEW_ERROR);
        }
    }

    public List<User> selUserListByIds(List<Long> ids) {
        List<User> users = userMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(users)) {
            throw new CommonAdviceException(ExceptioneEnum.QUERY_RESULT_EMPTY);
        }
        return users;
    }

    public Map<String, Object> selUserInfoById(Integer uid, Integer meUid) {
        User user = new User();
        user.setId(uid);
        User user1 = userMapper.selectOne(user);
        if (user1 == null) {
            throw new CommonAdviceException(ExceptioneEnum.QUERY_RESULT_EMPTY);
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("userName", user1.getUserName());
        map.put("userPic", user1.getAuthorUrl());
        map.put("gender", user1.getGender());
        map.put("createTime", user1.getCreateTime());
        map.put("id", user1.getId());
        map.put("job", user1.getOccupation());
        int likeNum = activeService.selUserLikeNum(uid);
        int attNum = attentionService.selAttCountByUid(uid);
        int fansNum = attentionService.selFansCountByUid(uid);
        map.put("likeNum", likeNum);
        map.put("attNum", attNum);
        map.put("fansNum", fansNum);
        Boolean att = attentionService.isAtt(meUid, uid);
        map.put("isguanzhu", att);
        map.put("path", user1.getAddress());
        return map;
    }

    public List<User> queryUserList(Integer page, Integer rows, String search, int uid) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        //关键字过滤
        if (StringUtils.isNoneBlank(search)) {
            criteria.orLike("userName", "%" + search + "%");
            criteria.orLike("phoneNumber", "%" + search + "%");
        }
        List<User> users = userMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(users)) {
            return users;
        }
        for (User user : users) {
            Boolean att = attentionService.isAtt(uid, user.getId());
            user.setIsguanzhu(att);
        }
//        PageInfo<User> info = new PageInfo<>(users);
//        return new PageResult<>(info.getTotal(),info.getPageNum(), users);
        return users;
    }
    public PageResult queryUserListV2(Integer page, Integer rows, String search, int uid) {
        PageHelper.startPage(page, rows);
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        //关键字过滤
        if (StringUtils.isNoneBlank(search)) {
            criteria.orLike("userName", "%" + search + "%");
            criteria.orLike("phoneNumber", "%" + search + "%");
        }
        String orderByClause = "create_time" + " DESC" ;
        example.setOrderByClause(orderByClause);
        List<User> users = userMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(users)) {
            return new PageResult<>();
        }
        PageInfo<User> info = new PageInfo<>(users);
        return new PageResult<>(info.getTotal(),info.getPageNum(), users);
    }
    public User queryUserById(Integer id) {
        User user = new User();
        user.setId(id);
        User user1 = userMapper.selectByPrimaryKey(user);
        if (user1 == null) {
            return null;
        }
        return user1;
    }

    public User authLogin(User user) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("openId",user.getOpenId());
        User user1 = new User();
        user1.setOpenId(user.getOpenId());
        User user2 = userMapper.selectOneByExample(example);
        if (user2 == null) {
            user.setCreateTime(new Date());
            int insert = userMapper.insert(user);
            return user;
        }
        return user2;
    }

    public SucessHandler sendRegisterCode(String phone) {
        String code = NumberUtils.generateCode(4);
        String templateID = TemplateEnum.PHONE_REGISTER.getTemplateID();
        System.out.println(templateID);
        int time = TemplateEnum.PHONE_REGISTER.getTime();
        smsUtil.sendMessage(templateID, phone, code);
        this.redisTemplate.opsForValue().set(KEY_PREFIX + phone, code, time, TimeUnit.MINUTES);

        SucessHandler sucessHandler = new SucessHandler(new Integer(code), 200, "手机注册验证码");
        return sucessHandler;
    }

    public User registerPhone(String phone, String code, String password) {
        String key = KEY_PREFIX + phone;
        // 从redis取出验证码
        String codeCache = this.redisTemplate.opsForValue().get(key);
        // 检查验证码是否正确
        if (!code.equals(codeCache)) {
            // 不正确，返回
            throw new CommonAdviceException(ExceptioneEnum.CODE_IS_ERROR);
        }
        User user = new User();
        user.setPhoneNumber(phone);
        User user1 = userMapper.selectOne(user);
        if (user1 != null) {
            throw new CommonAdviceException(ExceptioneEnum.USER_NUMBER_EXIST);
        }
        String ps = password + KEY_PREFIX;
        User u = new User();
        u.setStatus(true);
        u.setPhoneNumber(phone);
        u.setUserName(phone);
        String md5Pass = DigestUtils.md5DigestAsHex(ps.getBytes());
        u.setPassword(md5Pass);
        u.setCreateTime(new Date());
        u.setAuthorUrl("http://image.xquery.cn/2020eb384401aa616ba134126357_th.jpg");
        u.setGender(0);
        int count = userMapper.insert(u);
        if (count == 0) {
            throw new CommonAdviceException(ExceptioneEnum.USER_REGISTER_ERROR);
        }
        ChatList chatList = new ChatList();
        chatList.setFromId(u.getId());
        chatList.setToId(10000);
        chatList.setStatus(true);
        chatList.setCreateTime(new Date());
        chatList.setAfterTime(new Date());
        int insert = chatListMapper.insert(chatList);

        Message message = new Message();
        message.setBelong(u.getId());
        message.setCId(chatList.getId());
        message.setFromId(10000);
        message.setToId(u.getId());
        message.setStatus(true);
        message.setSendTime(new Date());
        message.setMessage("欢迎注册韩府帮！有什么问题都可以及时进行反馈！");
        Message message1 = messageService.saveMsg(message);
        return u;
    }


    public void freezeUserList(List<Integer> ids, Boolean freeze) {
        for(Integer id : ids){
            User user = new User();
            user.setId(id);
            user.setStatus(freeze);
            int i = userMapper.updateByPrimaryKeySelective(user);
        }
    }
    public Map selUserIncreaseTrend() {
        int i = userMapper.queryUserCount();
        List<Map<String, Object>> weekList = userMapper.queryUserCountWeek();
        List<Map<String, Object>> monthList = userMapper.queryUserCountMonth();
        List<Map<String, Object>> yearList = userMapper.queryUserCountYear();
        int day = userMapper.queryCountByTime(0);
        int yesterday = userMapper.queryCountByTime(1);
        int week = userMapper.queryCountByTime(7);
        int upWeek = userMapper.queryCountByTime(15);
        int year = userMapper.queryCountByTime(30);
        User user = new User();
        user.setGender(0);
        int manCount = userMapper.selectCount(user);
        Map<String, Object> map = new HashMap<>();
        map.put("day",day);
        map.put("total",i);
        map.put("week",week);
        map.put("upWeek",upWeek-week);
        map.put("year",year);
        map.put("yesterday",yesterday-day);
        map.put("weekList",weekList);
        map.put("monthList",monthList);
        map.put("yearList",yearList);
        map.put("man",manCount);
        map.put("woman",i-manCount);
        return map;
    }

    public void registerUserV2(User u) {
        u.setCreateTime(new Date());
        u.setStatus(false);
        u.setAuthorUrl("http://image.xquery.cn/2020eb384401aa616ba134126357_th.jpg");
        int count = userMapper.insert(u);
        if (count == 0) {
            throw new CommonAdviceException(ExceptioneEnum.USER_REGISTER_ERROR);
        }

    }
}