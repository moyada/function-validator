package io.moyada.medivh.annotation;

import java.lang.annotation.*;

/**
 * 是否允许为空，基础类型无效
 * @author xueyikang
 * @since 1.0
 **/
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.SOURCE)
public @interface Nullable {

}
