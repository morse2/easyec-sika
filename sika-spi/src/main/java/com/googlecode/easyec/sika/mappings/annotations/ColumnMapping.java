package com.googlecode.easyec.sika.mappings.annotations;

import com.googlecode.easyec.sika.converters.ColumnConverter;
import com.googlecode.easyec.sika.converters.NoOpColumnConverter;
import com.googlecode.easyec.sika.validations.ColumnValidator;

import java.lang.annotation.*;

/**
 * 针对于<code>Workbook</code>一行中的某一列的注解类。
 *
 * @author JunJie
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnMapping {

    /**
     * 映射到工作本中的列索引号。
     * <p>
     * Max columns in Excel 2003：256
     * <br/>
     * Max columns in Excel 2007：16384
     * </p>
     *
     * @return 列的索引编号
     */
    int index() default Integer.MIN_VALUE;

    /**
     * 映射到工作本中的列字母。
     * 列用根据Excel中定义的字母为准，且字母必须为大写。
     * <p>
     * Max column name in Excel 2003：IV
     * <br/>
     * Max column name in Excel 2007：XFD
     * </p>
     *
     * @return 列字母
     */
    String column() default "";

    /**
     * 定义实现了接口{@link ColumnValidator}的验证器。
     *
     * @return 返回列验证器的实现类
     * @see ColumnValidator
     */
    Class<? extends ColumnValidator>[] validators() default { };

    /**
     * 定义一个列数据转换器的实现类。该类可以用来在正式为属性赋值前转换其值类型。
     *
     * @return 返回列数据转换器的实现类
     * @see ColumnConverter
     */
    Class<? extends ColumnConverter> converter() default NoOpColumnConverter.class;
}
