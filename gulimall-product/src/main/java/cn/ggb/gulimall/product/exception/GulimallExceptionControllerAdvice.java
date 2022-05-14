package cn.ggb.gulimall.product.exception;

import cn.ggb.common.exception.BizCodeEnume;
import cn.ggb.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@Slf4j
@RestControllerAdvice(basePackages = "cn.ggb.gulimall.product.controller")
public class GulimallExceptionControllerAdvice {

    /**
     * 捕获定义的异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleVaildException(MethodArgumentNotValidException e){
        log.error("数据效验出现问题{},异常类型:{}",e.getMessage(),e.getClass());
        HashMap<String, String> errorMap = new HashMap<>();
        BindingResult bindingResult = e.getBindingResult();
        bindingResult.getFieldErrors().forEach((fieldError) -> {
            errorMap.put(fieldError.getField(),fieldError.getDefaultMessage());
        });
        return R.error(BizCodeEnume.VAILD_EXCEPTION.getCode(),BizCodeEnume.VAILD_EXCEPTION.getMsg()).put("data",errorMap);
    }

    /**
     * 兜底异常
     * @param throwable
     * @return
     */
    @ExceptionHandler(value = Throwable.class)
    public R handleException(Throwable throwable){
        return R.error(BizCodeEnume.UNKNOW_EXCEPTION.getCode(),BizCodeEnume.UNKNOW_EXCEPTION.getMsg());
    }

}
