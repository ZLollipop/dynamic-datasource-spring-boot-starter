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
package com.baomidou.dynamic.datasource.support;

import com.baomidou.dynamic.datasource.MyBaseMapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据源解析器
 *
 * @author TaoYu
 * @since 2.3.0
 */
@Slf4j
public class DataSourceClassResolver {

    private static boolean mpEnabled = false;

    private static Field mapperInterfaceField;

    static {
        Class<?> proxyClass = null;
        try {
            proxyClass = Class.forName("com.baomidou.mybatisplus.core.override.MybatisMapperProxy");
        } catch (ClassNotFoundException e1) {
            try {
                proxyClass = Class.forName("com.baomidou.mybatisplus.core.override.PageMapperProxy");
            } catch (ClassNotFoundException e2) {
                try {
                    proxyClass = Class.forName("org.apache.ibatis.binding.MapperProxy");
                } catch (ClassNotFoundException ignored) {
                }
            }
        }
        if (proxyClass != null) {
            try {
                mapperInterfaceField = proxyClass.getDeclaredField("mapperInterface");
                mapperInterfaceField.setAccessible(true);
                mpEnabled = true;
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 缓存方法对应的数据源
     */
    private final Map<String, String> dsCache = new ConcurrentHashMap<>();
    private final boolean allowedPublicOnly;

    /**
     * 加入扩展, 给外部一个修改aop条件的机会
     *
     * @param allowedPublicOnly 只允许公共的方法, 默认为true
     */
    public DataSourceClassResolver(boolean allowedPublicOnly) {
        this.allowedPublicOnly = allowedPublicOnly;
    }

    /**
     * 从缓存获取数据
     *
     * @param method       方法
     * @param targetObject 目标对象
     * @return ds
     */
    public String findDSKey(Method method, Object targetObject) {
        if (method.getDeclaringClass() == Object.class) {
            return "";
        }
        String cacheKey = method.getDeclaringClass().getName();
        // TODO: 2020/11/8 优化或者修正这里
        if (method.getDeclaringClass().equals(BaseMapper.class) || method.getDeclaringClass().equals(MyBaseMapper.class)) {
            // 是走的父类的方法，需要获取到实际的子类
            Class<?>[] interfaces = targetObject.getClass().getInterfaces();
            if (interfaces.length > 0) {
                cacheKey = interfaces[0].getName();
            }
        }
        String ds = this.dsCache.get(cacheKey);
        if (ds == null) {
            //  获取这个方法的数据源
            ds = computeDatasource(cacheKey);
            if (ds == null) {
                ds = "";
            }
            this.dsCache.put(cacheKey, ds);
        }
        return ds;
    }

    /**
     * 依据类全限定类名来确定数据源的key
     * <p>
     * 如果按照分包方式，就截取最后一个一个文件夹名
     * 如果不分包的话呢？
     * <p>
     * 核心： 决定这个方法属于哪个数据源就在这里面了
     *
     * @return 数据源的key名称
     */
    private String computeDatasource(String className) {
        if (StringUtils.isEmpty(className)) {
            return null;
        }
        return getDefaultDataSourceAttr(className);
    }


    /**
     * 默认的获取数据源名称方式
     * <p>
     * // TODO: 2020/11/6  暂时只提供这种方式，后面读写分离什么 最优秀的方案，以后再说，如何做到强约束(非默认的实现）
     *
     * @return ds
     */
    private String getDefaultDataSourceAttr(String className) {
        String[] split = className.split("\\.");
        // 这里只取第二个，后面可以，从后往前遍历，找到对应的  properties里面已经加载的 数据源 匹配上就ok
        return split[split.length - 2];
    }
}
