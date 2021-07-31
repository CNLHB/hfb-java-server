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
public enum  SucessEnum {
    RESULT_GET_SUCESS(0,200,"sucess"),
    RESULT_POST_OR_PUT_SUCESS(0,201,"sucess"),
    RESULT_DELETE_SUCESS(0,204,"sucess"),
    IMAGE_UPLOAD_SUCESS(0,200,"图片上传成功！"),
    SPEC_GROUP_SUCESS(0,201,"商品分组创建成功！"),
    SPEC_GROUP_PARAM_SUCESS(0,201,"商品分组参数创建成功！"),
    USER_LOGIN_SUCESS(0,201,"用户登录成功！"),
    DATA_GET_SUCESS(0,200,"数据获取成功！"),
    TOKEN_VERIFY_SUCESS(0,200,"token验证有效！"),
    PWD_UPDATE_SUCESS(0,201,"密码修改成功！"),
    ;
    private Integer code;
    private Integer status;
    private String msg;
}
