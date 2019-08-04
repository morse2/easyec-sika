package com.googlecode.easyec.sika.mappings.annotations;

import com.googlecode.easyec.sika.converters.ColumnConverter;
import com.googlecode.easyec.sika.converters.NoOpConverter;
import com.googlecode.easyec.sika.validations.ColumnValidator;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Documented
@ColumnMapping
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PaddingColumnMapping {

    int position();

    String columnForRead() default "";

    String columnForWrite() default "";

    /**
     * 定义实现了接口{@link ColumnValidator}的验证器。
     * <p>
     * 此类只可在解析工作本的时候被调用，
     * 当数据写入工作本时候，
     * 此属性定义的验证器类都将被忽略。
     * </p>
     *
     * @return 返回列验证器的实现类
     * @see ColumnValidator
     */
    @AliasFor(annotation = ColumnMapping.class)
    Class<? extends ColumnValidator>[] validators() default { };

    /**
     * 定义一个列数据转换器的实现类。该类可以用来在正式为属性赋值前转换其值类型。
     *
     * @return 返回列数据转换器的实现类
     * @see ColumnConverter
     */
    @AliasFor(annotation = ColumnMapping.class)
    Class<? extends ColumnConverter> converter() default NoOpConverter.class;
}
