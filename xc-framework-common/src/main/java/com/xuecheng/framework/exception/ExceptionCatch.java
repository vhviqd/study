package com.xuecheng.framework.exception;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局捕获异常类：只要作用在@RequestMapping上，所有的异常都会被捕获
 */
@ResponseBody   //在下面添加也可以
@ControllerAdvice
public class ExceptionCatch {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionCatch.class);

    /**
     * @ExceptionHandler(value = CustomException.class ) 捕获ExceptionCast 异常,只要是这个类抛出的异常，这里就会捕获
     * @param e  @ResponseBody这个注解是将参数转换成 json格式,否则会报错，因为这个是json格式
     * @return
     */
    @ExceptionHandler(value = CustomException.class )
    @ResponseBody
    public ResponseResult customException(CustomException e) {
        logger.error("catch exception : {}\r\n exception: ",e.getMessage(), e);
        ResultCode resultCode = e.getResultCode();
        ResponseResult responseResult = new ResponseResult(resultCode);
        return responseResult;
    }

    //使用EXCEPTIONS存放异常类型和错误代码的映射，ImmutableMap的特点的一旦创建不可改变，并且线程安全
    private  static ImmutableMap<Class<? extends Throwable>,ResultCode> EXCEPTIONS;
    //使用builder来构建一个异常类型和错误代码的异常
    protected static ImmutableMap.Builder<Class<? extends Throwable>,ResultCode> builder = ImmutableMap.builder();

    //捕获Exception异常
    @ExceptionHandler(value = Exception.class )
    @ResponseBody
    public ResponseResult customException(Exception e) {
        logger.error("catch exception : {}\r\n exception: ",e.getMessage(), e);
        ResponseResult responseResult =  null;
        //这里先判断在map中有没有这个异常，如果有，就抛出错误代码，如果没有就抛出统一的错误提示代码
        if(EXCEPTIONS == null){
            EXCEPTIONS = builder.build();
            ResultCode resultCode = EXCEPTIONS.get(e.getClass());
            if(resultCode != null){
                //抛出在map中定义的异常
                responseResult = new ResponseResult(resultCode);
            }else {
                //抛出统一的异常
                responseResult = new ResponseResult(CommonCode.SERVER_ERROR);
            }
        return responseResult;
        }
        return null;
    }

    //定义一个代码块： 因为代码块会先执行
    static{
        //在这里加入一些基础的异常类型判断  ,比如框架抛出来的自己又知道的一些异常
        builder.put(HttpMessageNotReadableException.class, CommonCode.INVALIDPARAM);
    }


}
