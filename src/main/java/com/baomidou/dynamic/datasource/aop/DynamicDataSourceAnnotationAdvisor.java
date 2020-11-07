/**
 * Copyright © 2018 organization baomidou
 * <pre>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <pre/>
 */
package com.baomidou.dynamic.datasource.aop;

import lombok.NonNull;
import org.aopalliance.aop.Advice;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationClassFilter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;

/**
 * AOP 拦截@Repository注解的类
 *
 * @author TaoYu
 * @since 1.2.0
 */
public class DynamicDataSourceAnnotationAdvisor extends AbstractPointcutAdvisor implements BeanFactoryAware {

    private final Advice advice;

    private final Pointcut pointcut;

    /**
     * 这个和DynamicDataSourceAnnotationInterceptor 如何关联的？ 拦截到这些注解的类之后，好像就交给这个Interceptor处理了
     */
    public DynamicDataSourceAnnotationAdvisor(@NonNull DynamicDataSourceAnnotationInterceptor dynamicDataSourceAnnotationInterceptor) {
        this.advice = dynamicDataSourceAnnotationInterceptor;
        this.pointcut = buildPointcut();
    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        return this.advice;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (this.advice instanceof BeanFactoryAware) {
            ((BeanFactoryAware) this.advice).setBeanFactory(beanFactory);
        }
    }

    private Pointcut buildPointcut() {
        // 只过滤类
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("@within(org.springframework.stereotype.Repository) ||" +
                " target(com.baomidou.mybatisplus.core.mapper.BaseMapper)");

        // TODO: 2020/11/6 这个可以用？
//        pointcut.setLocation();
        return pointcut;
//        return new AnnotationClassPoint(Repository.class);
    }

    /**
     * 匹配所有以@Repository的类
     */
    private static class AnnotationClassPoint implements Pointcut {

        private final Class<? extends Annotation> annotationType;

        public AnnotationClassPoint(Class<? extends Annotation> annotationType) {
            Assert.notNull(annotationType, "Annotation type must not be null");
            this.annotationType = annotationType;
        }

        @Override
        public ClassFilter getClassFilter() {
//            return ClassFilter.TRUE;
            return new AnnotationClassFilter(annotationType);
        }

        @Override
        public MethodMatcher getMethodMatcher() {
            // actually is org.springframework.aop.TrueMethodMatcher
            return MethodMatcher.TRUE;
//            return new AnnotationMethodMatcher(DynamicTest.class);
        }
    }
}
