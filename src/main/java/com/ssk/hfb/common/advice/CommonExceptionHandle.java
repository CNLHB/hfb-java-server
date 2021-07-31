package com.ssk.hfb.common.advice;


import com.ssk.hfb.common.enums.ExceptioneEnum;
import com.ssk.hfb.common.exception.*;
import com.ssk.hfb.common.result.ResultException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;

@Slf4j
@ControllerAdvice
public class CommonExceptionHandle {
    /**
     * 数据库访问异常，一般为传递字段错误
     * @param e
     * @return
     */
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ResultException> handle(SQLException e){
        log.error("数据库异常");
        log.error(e.getMessage());
        ResultException sql = new ResultException(ExceptioneEnum.QUERY_PARM_ERROR);
        return ResponseEntity.status(sql.getStatus()).body(sql);
    }

    /**
     * 通用异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(CommonAdviceException.class)
    public ResponseEntity<ResultException> handle(CommonAdviceException e){
        return ResponseEntity.status(e.getExceptioneEnum().getStatus()).body(new ResultException(e.getExceptioneEnum()));
    }

}
