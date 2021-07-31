package com.ssk.hfb.controller;

import com.ssk.hfb.common.enums.ExceptioneEnum;
import com.ssk.hfb.common.enums.SucessEnum;
import com.ssk.hfb.common.exception.CommonAdviceException;
import com.ssk.hfb.common.result.SucessHandler;
import com.ssk.hfb.common.result.SucessHandler;
import com.ssk.hfb.common.sucess.ResultSucess;
import com.ssk.hfb.common.utils.NumberUtils;
import com.ssk.hfb.common.vo.PageResult;
import com.ssk.hfb.pojo.*;
import com.ssk.hfb.service.*;
import com.ssk.hfb.utils.AddressUtils;
import com.ssk.hfb.utils.UserContext;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;
    @Autowired
    private AttentionService attentionService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private AccessService accessService;
    @Autowired
    private TopicService topicService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private CollectService collectService;
    @Autowired
    private SignInService signInService;
    private static boolean isEffective(final String remoteAddr) {
        boolean isEffective = false;
        if ((null != remoteAddr) && (!"".equals(remoteAddr.trim()))
                && (!"unknown".equalsIgnoreCase(remoteAddr.trim()))) {
            isEffective = true;
        }
        return isEffective;
    }
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (ipAddress.equals("127.0.0.1")) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    ipAddress = inet.getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress="";
        }
        // ipAddress = this.getRequest().getRemoteAddr();

        return ipAddress;
    }
    @PostMapping("register")
    public ResponseEntity<SucessHandler> register(@RequestBody User u){
        userService.registerUser(u);
        return ResponseEntity.ok(new SucessHandler(SucessEnum.RESULT_POST_OR_PUT_SUCESS));
    }
    @PostMapping("register/v2")
    public ResponseEntity<SucessHandler> registerV2(@RequestBody User u){
        userService.registerUserV2(u);
        return ResponseEntity.ok(new SucessHandler(SucessEnum.RESULT_POST_OR_PUT_SUCESS));
    }
    @PostMapping("signin")
    public ResponseEntity<SucessHandler> signin(@RequestBody SignIn signIn,HttpServletRequest request){
        int uId = UserContext.getId();
        signIn.setUId(uId);
        String ipAddress = AddressUtils.getIpAddress(request);
        String adress = AddressUtils.getAdress(ipAddress);
        signIn.setAdress(adress);
        signInService.saveSignIn(signIn);
        SucessHandler sucessHandler = new SucessHandler(SucessEnum.RESULT_POST_OR_PUT_SUCESS);
        sucessHandler.setMessage(adress);
        return ResponseEntity.ok(sucessHandler);
    }
    @GetMapping("signin")
    public ResponseEntity<List<SignIn>> getSignin(){
        int uId = UserContext.getId();
        List<SignIn> signIns = signInService.querySignLisyByUId(uId);
        return ResponseEntity.ok(signIns);
    }
    @GetMapping("adress")
    public ResponseEntity<JSONObject> getAdress(HttpServletRequest request){
        String ipAddr = getIpAddr(request);
        String addresses = AddressUtils.getIpAddress(request);
        log.info(addresses);
        String url = " http://api.map.baidu.com/location/ip?ak=cXOksuw9T7E6iVYdith7qPlrbW2o3fKc&ip="+ipAddr+"&coor=bd09ll";//HTTP协议 ";
           log.info(url);
            try {
                URL urlGet = new URL(url);
                HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
                http.setRequestMethod("GET"); // 必须是get方式请求
                http.setDoOutput(true);
                http.setDoInput(true);
                http.connect();
                InputStream is = http.getInputStream();
                int size = is.available();
                byte[] jsonBytes = new byte[size];
                is.read(jsonBytes);
                String message = new String(jsonBytes);
                log.info("access_token:"+message);
                JSONObject demoJson = JSONObject.fromObject(message);
                log.info("access_token:"+demoJson);

                is.close();
                return ResponseEntity.ok(demoJson);
            } catch (Exception e) {
                e.printStackTrace();
            }
        return ResponseEntity.ok(null);

    }
    @GetMapping("register/{phone}")
    public ResponseEntity<SucessHandler> registerCode(@PathVariable(value = "phone") String phone){
        SucessHandler sucessHandler = userService.sendRegisterCode(phone);
        return ResponseEntity.ok(sucessHandler);
    }
    @GetMapping("forget/{phone}")
    public ResponseEntity<SucessHandler> forgetCode(@PathVariable(value = "phone") String phone){
        SucessHandler sucessHandler = userService.sendForgetCode(phone);
        return ResponseEntity.ok(sucessHandler);
    }
    @PostMapping("forget/code")
    public ResponseEntity< ResultSucess> forgetPwd(
            @RequestParam(value = "phone", required = true) String phone,
            @RequestParam(value = "code", required = true) String code,
            @RequestParam(value = "password", required = true) String password
    ){
        User user = userService.forgetPwd(phone, code, password);
        ResultSucess resultSucess = new ResultSucess(SucessEnum.PWD_UPDATE_SUCESS);
        return ResponseEntity.ok(resultSucess);
    }

    @PostMapping("register/phone")
    public ResponseEntity<User> registerPhone(
            @RequestParam(value = "phone", required = true) String phone,
            @RequestParam(value = "code", required = true) String code,
            @RequestParam(value = "password", required = true) String password

    ){
        User user = userService.registerPhone(phone, code,password);
        return ResponseEntity.ok(user);
    }
    @PostMapping("login/code")
    public ResponseEntity< ResultSucess<String,Object>> loginCode(
            @RequestParam(value = "phone", required = true) String phone,
            @RequestParam(value = "code", required = true) String code
    ){
        User user = this.userService.loginCode(phone, code);
        String token = authService.generateToken(user);
        Map<String, Object> map = new HashMap<>();
        map.put("token",token);
        map.put("userInfo", user);
        ResultSucess<String,Object> resultSucess = new ResultSucess(SucessEnum.USER_LOGIN_SUCESS,map);
        return ResponseEntity.ok(resultSucess);
    }

    @PostMapping("login")
    public ResponseEntity<ResultSucess<String,Object>> login(@RequestBody @Valid User u, HttpServletRequest request){
        String ipAddress = AddressUtils.getIpAddress(request);
        String adress = AddressUtils.getAdress(ipAddress);
        LoginLog loginLog = new LoginLog();
        loginLog.setAdress(adress);
        loginLog.setIp(ipAddress);
        User login = userService.login(u,loginLog);
        String token = authService.generateToken(login);
        Map<String, Object> map = new HashMap<>();
        map.put("token",token);
        map.put("userInfo", login);
        ResultSucess<String,Object> resultSucess = new ResultSucess(SucessEnum.USER_LOGIN_SUCESS,map);
        return ResponseEntity.ok(resultSucess);
    }
    @PostMapping("login/{phone}")
    public ResponseEntity<SucessHandler> loginPhone(@PathVariable(value = "phone", required = true) String phone){
        SucessHandler sucessHandler = userService.sendCode(phone);

        return ResponseEntity.ok(sucessHandler);
    }
    @PostMapping("active")
    public ResponseEntity<SucessHandler> userActive(@RequestBody Attention t){
        attentionService.saveAttention(t);
        return ResponseEntity.ok(new SucessHandler(SucessEnum.RESULT_POST_OR_PUT_SUCESS));
    }
    @DeleteMapping("active")
    public ResponseEntity<SucessHandler> deleteActive(@RequestBody Attention t){

        attentionService.deleteAttention(t);
        return ResponseEntity.ok(new SucessHandler(SucessEnum.RESULT_POST_OR_PUT_SUCESS));
    }
    @PutMapping("password")
    public ResponseEntity<SucessHandler> reSetPassword(
            @RequestHeader(value = "Authorization", defaultValue = "") String token,
            @RequestParam(value = "oldpassword") String oldpassword,
            @RequestParam(value = "newpassword") String newpassword,
            @RequestParam(value = "renewpassword") String renewpassword
    ){
        int uId = authService.getUId(token);
        userService.reSetPassword(uId, oldpassword, newpassword, renewpassword);
        return ResponseEntity.ok(new SucessHandler(SucessEnum.RESULT_POST_OR_PUT_SUCESS));
    }
    @PostMapping("email")
    public ResponseEntity<SucessHandler> bindEmail(
            @RequestHeader(value = "Authorization", defaultValue = "") String token,
            @RequestParam(value = "email") String email,
            @RequestParam(value = "code", required = false) String code
    ){
        if (code!=null){
            int uId = authService.getUId(token);
            userService.bindEmail(uId,email, code);
        }else {
            String cd = NumberUtils.generateCode(4);
            emailService.sendSimpleMailMessge(email,cd);
        }
        return ResponseEntity.ok(new SucessHandler(SucessEnum.RESULT_POST_OR_PUT_SUCESS));
    }

    @PutMapping("info")
    public ResponseEntity<SucessHandler> updateUserInfo(
            @RequestBody User user){
        int uId = UserContext.getId();
        if (uId == -1){
            throw new CommonAdviceException(ExceptioneEnum.TOKEN_IS_EXPIRED);
        }
        user.setId(uId);
        userService.updateUserInfo(user);
        return ResponseEntity.ok(new SucessHandler(SucessEnum.RESULT_POST_OR_PUT_SUCESS));
    }
    @GetMapping("info")
    public ResponseEntity<ResultSucess<String,Object>> getInfo(){

            int uId = UserContext.getId();
            User user = userService.searchUserById(uId);
            String newToken = authService.generateToken(user);
            Map<String, Object> map = new HashMap<>();
            map.put("token",newToken);
            map.put("userInfo",user);
            ResultSucess<String,Object> resultSucess = new ResultSucess(SucessEnum.USER_LOGIN_SUCESS,map);
            return ResponseEntity.ok(resultSucess);

    }
    @GetMapping("/att/list")
    public ResponseEntity<List<User>> getAttList(){
            int uId = UserContext.getId();
            List<User> users = attentionService.selAttListByUid(uId);
            return ResponseEntity.ok(users);

    }
    @GetMapping("/fans/list")
    public ResponseEntity<List<User>> getFansList(){
            int uId = UserContext.getId();
            List<User> users = attentionService.selFansByUid(uId);
            return ResponseEntity.ok(users);
    }
    @GetMapping("/each/list")
    public ResponseEntity<List<User>> selEachAttListByUid(){
            int uId = UserContext.getId();
            List<User> users = attentionService.selEachAttListByUid(uId);
            return ResponseEntity.ok(users);
    }
    @DeleteMapping("att")
    public ResponseEntity<SucessHandler> deleteAtt(@RequestBody Attention attention){
        return ResponseEntity.ok(new SucessHandler(SucessEnum.RESULT_DELETE_SUCESS));
    }
    @GetMapping("willinfo/{id}")
    public ResponseEntity<ResultSucess> getWillInfo(
            @PathVariable(value = "id") Integer id){
        Map<String, Object> map;
        int uId = UserContext.getId();
            map = userService.selUserInfoById(id,uId);
        return ResponseEntity.ok(new ResultSucess(SucessEnum.DATA_GET_SUCESS, map));

    }
    @PostMapping("access")
    public ResponseEntity<ResultSucess> saveAccess(
            @RequestBody Access access
            ){
        Boolean aBoolean = accessService.saveAccess(access);
        return ResponseEntity.ok(new ResultSucess(SucessEnum.DATA_GET_SUCESS));

    }
    @PostMapping("auth/login")
    public ResponseEntity<ResultSucess> authLogin(
            @RequestBody User user
    ){
        System.out.println(user);
        User u = userService.authLogin(user);
        String token = authService.generateToken(u);
        Map<String, Object> map = new HashMap<>();
        map.put("token",token);
        map.put("userInfo", u);
        ResultSucess<String,Object> resultSucess = new ResultSucess(SucessEnum.USER_LOGIN_SUCESS,map);
        return ResponseEntity.ok(resultSucess);

    }
    @GetMapping("access")
    public ResponseEntity<Map<String, Integer>> saveAccess(
    ){
        Map<String, Integer> map;
            int uId = UserContext.getId();
             map = accessService.queryAccess(uId);
            int topicCount = topicService.queryCountByUId(uId);
            int commCount = commentService.queryCountByUId(uId);
            int collCount = collectService.queryCountByUid(uId);
            map.put("topicCount",topicCount);
            map.put("commCount",commCount);
            map.put("collCount",collCount);
        return ResponseEntity.ok(map);
    }
    @GetMapping("list")
    public ResponseEntity< List<User>> queryUserList(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "10") Integer rows,
            @RequestParam(value = "search", required = true) String search
    ){
            int uId = UserContext.getId();
            List<User> users = userService.queryUserList(page, rows, search,uId);
            return ResponseEntity.ok(users);
    }
    @GetMapping("v2/list")
    public ResponseEntity< PageResult> searchUserList(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "10") Integer rows,
            @RequestParam(value = "search", required = false) String search
    ){
        int uId = UserContext.getId();
        PageResult pageResult = userService.queryUserListV2(page, rows, search, uId);
        return ResponseEntity.ok(pageResult);
    }
    @PutMapping("freeze")
    public ResponseEntity<ResultSucess> freezeUserList(
            @RequestParam(value = "ids") List<Integer> ids,
            @RequestParam(value = "freeze") Boolean freeze

            ){
        int uId = UserContext.getId();
        userService.freezeUserList(ids, freeze);
        return ResponseEntity.ok(new ResultSucess(SucessEnum.RESULT_POST_OR_PUT_SUCESS));
    }
    @GetMapping("trend")
    public ResponseEntity<ResultSucess> selUserIncreaseTrend(){
        Map map = userService.selUserIncreaseTrend();
        return ResponseEntity.ok(new ResultSucess(SucessEnum.RESULT_GET_SUCESS,map));
    }

}
