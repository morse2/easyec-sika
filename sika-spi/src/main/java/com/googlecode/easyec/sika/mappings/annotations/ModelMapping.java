package com.googlecode.easyec.sika.mappings.annotations;

import com.googlecode.easyec.sika.converters.ColumnConverter;
import com.googlecode.easyec.sika.converters.NoOpColumnConverter;
import com.googlecode.easyec.sika.validations.ColumnValidator;

import java.lang.annotation.*;

/**
 * 模型映射器注解类。
 * <p>
 * 该类用于映射复杂模型定义。例如：
 * <pre>
 * public class Foo {
 *   private Bar bar;<br/>
 *   @\ModelMapper(Bar.class)
 *   public Bar getBar() { return this.bar; }
 * }<br/>
 * class Bar {
 *   private String key1;
 *   private String key2;<br/>
 *   public String getKey1() { return this.key1; }
 *   public String getKey2() { return this.key2; }
 * }
 * </pre>
 * </p>
 *
 * @author JunJie
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ModelMapping {

    /**
     * 定义了映射的模型的实现类。
     *
     * @return 返回一个具体的实现模型
     */
    Class<?> value();

    /**
     * 定义实现了接口{@link ColumnValidator}的验证器。
     *
     * @return 返回列验证器的实现类
     * @see ColumnValidator
     */
    Class<? extends ColumnValidator>[] validators() default { };

    /**
     * 定义一个列数据转换器的实现类。该类可以用来在正式为属性赋值前转换其值类型。<br/>
     * <b>注意：该属性只在填充对象时才有用，反向生成<code>{@link com.googlecode.easyec.sika.WorkData}</code>
     * 时，则无效。</b>
     *
     * @return 返回列数据转换器的实现类
     * @see ColumnConverter
     */
    Class<? extends ColumnConverter> converter() default NoOpColumnConverter.class;
}
