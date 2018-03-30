package com.datadigger.datainsight.http;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.datadigger.datainsight.exception.DataDiggerException;


@ControllerAdvice
public class ExceptionAdvisor {

	
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus
    public Result exceptionHandler(Exception e) {
        return new Result(false, e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = NullPointerException.class)
    @ResponseStatus
    public Result nullPointerExceptionHandler(NullPointerException e) {
        return new Result(false, "空指針异常:" +e.getMessage());
    }
    
    @ResponseBody
    @ExceptionHandler(value = DataDiggerException.class)
    public Result formatCheckExceptionHandler(DataDiggerException e) {
        return new Result(false, e.getDetail());
    }

}
