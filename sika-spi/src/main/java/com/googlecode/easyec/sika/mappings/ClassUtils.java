package com.googlecode.easyec.sika.mappings;

import com.googlecode.easyec.sika.WorkingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 类对象工具类
 *
 * @author JunJie
 */
public final class ClassUtils {

    private static final Logger logger = LoggerFactory.getLogger(ClassUtils.class);

    /**
     * 解析泛型类型数据对象
     *
     * @param o     对象实例
     * @param index 泛型类型的索引号
     * @return 具体的实现类
     * @throws com.googlecode.easyec.sika.WorkingException
     */
    public static Class resolveGenericType(Object o, int index) throws WorkingException {
        Type genType = o.getClass().getGenericSuperclass();

        while (genType instanceof Class) {
            if (null == ((Class) genType).getSuperclass()) {
                String msg = "No superclass was found. [" + o.getClass().getName() + "].";
                logger.error(msg);

                throw new IllegalArgumentException(msg);
            }

            genType = ((Class) genType).getGenericSuperclass();
        }

        if (!(genType instanceof ParameterizedType)) {
            String msg = o.getClass().getSimpleName() + "'s superclass isn't class ParameterizedType";
            logger.error(msg);

            throw new IllegalArgumentException(msg);
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            String msg = "Index: " + index + ", Size of " + o.getClass().getSimpleName() +
                "'s Parameterized Type: " + params.length;

            logger.error(msg);

            throw new WorkingException(msg, false);
        }

        Type param = params[index];

        if (param instanceof Class) {
            return (Class) param;
        }

        return param.getClass();
    }
}
