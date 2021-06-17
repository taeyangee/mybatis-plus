package com.baomidou.mybatisplus.core.toolkit.support;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.ClassUtils;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;

/**
 * Created by hcl at 2021/5/14
 * 基于 jdk SerializedLambda实现的 lambdaMeta
 */
public class SerializedLambdaMeta implements LambdaMeta {
    private static final Field FIELD_CAPTURING_CLASS;

    static {
        try {
            Class<SerializedLambda> aClass = SerializedLambda.class;
            FIELD_CAPTURING_CLASS = ReflectionKit.setAccessible(aClass.getDeclaredField("capturingClass"));
        } catch (NoSuchFieldException e) {
            throw new MybatisPlusException(e);
        }
    }

    private final SerializedLambda lambda;

    public SerializedLambdaMeta(SerializedLambda lambda) {
        this.lambda = lambda; /* SerializedLambda[capturingClass=class com.baomidou.mybatisplus.core.conditions.QueryWrapperTest, functionalInterfaceMethod=com/baomidou/mybatisplus/core/toolkit/support/SFunction.apply:(Ljava/lang/Object;)Ljava/lang/Object;, implementation=invokeVirtual com/baomidou/mybatisplus/core/conditions/BaseWrapperTest$Entity.getName:()Ljava/lang/String;, instantiatedMethodType=(Lcom/baomidou/mybatisplus/core/conditions/BaseWrapperTest$Entity;)Ljava/lang/Object;, numCaptured=0] */
    }

    @Override
    public String getImplMethodName() {
        return lambda.getImplMethodName();
    }

    @Override
    public Class<?> getInstantiatedClass() {
        String instantiatedMethodType = lambda.getInstantiatedMethodType(); /* 比如：(Lcom/baomidou/mybatisplus/core/conditions/BaseWrapperTest$Entity;)Ljava/lang/Object; */
        String instantiatedType = instantiatedMethodType.substring(2, instantiatedMethodType.indexOf(';')).replace('/', '.'); /* 比如： com.baomidou.mybatisplus.core.conditions.BaseWrapperTest$Entity*/
        return ClassUtils.toClassConfident(instantiatedType, getCapturingClass().getClassLoader());
    }

    public Class<?> getCapturingClass() {
        try {
            return (Class<?>) FIELD_CAPTURING_CLASS.get(lambda);
        } catch (IllegalAccessException e) {
            throw new MybatisPlusException(e);
        }
    }

}
