package com.ssk.hfb.common.exception;

import com.ssk.hfb.common.enums.ExceptioneEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommonAdviceException extends  RuntimeException{
    private ExceptioneEnum exceptioneEnum;
}
