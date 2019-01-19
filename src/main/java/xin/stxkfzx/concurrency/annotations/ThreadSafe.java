package xin.stxkfzx.concurrency.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用来标记【线程安全】的类或写法
 *
 * @author fmy
 * @date 2019-01-19 10:34
 */
@Target(ElementType.TYPE) // 标记到类中
@Retention(RetentionPolicy.RUNTIME) //编译时忽略
public @interface ThreadSafe {
    String value() default "";
}
