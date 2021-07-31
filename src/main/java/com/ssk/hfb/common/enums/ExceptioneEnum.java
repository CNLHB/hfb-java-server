package com.ssk.hfb.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 200 OK - [GET]：服务器成功返回用户请求的数据，该操作是幂等的（Idempotent）。
 * 201 CREATED - [POST/PUT/PATCH]：用户新建或修改数据成功。
 * 202 Accepted - [*]：表示一个请求已经进入后台排队（异步任务）
 * 204 NO CONTENT - [DELETE]：用户删除数据成功。
 * 400 INVALID REQUEST - [POST/PUT/PATCH]：用户发出的请求有错误，服务器没有进行新建或修改数据的操作，该操作是幂等的。
 * 401 Unauthorized - [*]：表示用户没有权限（令牌、用户名、密码错误）。
 * 403 Forbidden - [*] 表示用户得到授权（与401错误相对），但是访问是被禁止的。
 * 404 NOT FOUND - [*]：用户发出的请求针对的是不存在的记录，服务器没有进行操作，该操作是幂等的。
 * 406 Not Acceptable - [GET]：用户请求的格式不可得（比如用户请求JSON格式，但是只有XML格式）。
 * 410 Gone -[GET]：用户请求的资源被永久删除，且不会再得到的。
 * 422 Unprocesable entity - [POST/PUT/PATCH] 当创建一个对象时，发生一个验证错误。
 * 500 INTERNAL SERVER ERROR - [*]：服务器发生错误，用户将无法判断发出的请求是否成功。
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum ExceptioneEnum {
    INVALID_FILE_TYPE(10004,400, "文件类型错误或文件为空"),
    TOPIC_UPDATA_ERROR(10004,404, "修改Topic失败"),
    TOPIC_SAVE_ERROR(10004,404, "新增Topic失败"),
    USER_SAVE_ERROR(10004,404, "新增用户失败"),
    SAVE_ERROR(10004,404, "新增失败"),
    DETELE_ERROR(10004,404, "删除失败"),
    UPDATA_ERROR(10004,404, "更新失败"),
    COMMENT_SAVE_ERROR(10004,404, "新增评论失败"),
    NUMBER_OR_PASSWORD_ERROR(10004,404, "账号或密码错误!"),
    USER_REGISTER_ERROR(10004,404, "用户注册失败!"),
    USER_NUMBER_EXIST(10004,404, "用户已存在!"),
    USER_PASSWORD_OTHER(10004,404, "密码不一致!"),
    TOKEN_IS_EXPIRED(10004,401, "TOKEN已过期!"),
    CODE_IS_ERROR(10004,404, "验证码错误!"),
    INVALID_PASSWORD_ERROR(10004,404, "密码错误！!"),
    USERINFO_UPDATE_RENEW_ERROR(10004,404, "用户信息更新失败！!"),
    QUERY_PARM_ERROR(10004,404, "查询参数错误！!"),
    QUERY_RESULT_EMPTY(10004,404, "查询结果为空！!"),
    SUCESS_QUERY_RESULT_EMPTY(0,200, "查询结果为空！!"),
    REDIS_CONNECT_ERROR(10005,500, "服务器错误！!"),
    ;
    private Integer code;
    private Integer status;
    private String msg;

}
