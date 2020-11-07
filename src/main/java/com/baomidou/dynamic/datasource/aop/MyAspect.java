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

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

//@Aspect
//@Component
public class MyAspect {

//    public static final String a = "execution(@within(org.springframework.stereotype.Repository) || target(com.baomidou.mybatisplus.core.mapper.BaseMapper))";
//
//    @Around(a)
//    public Object before(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
//        System.out.println("进入环绕通知");
//        Object proceed = proceedingJoinPoint.proceed();//放行执行目标方法  这一步必须存在
//        System.out.println("目标方法执行之后回到环绕通知");
//        return proceed;//返回目标方法返回值
//    }
}