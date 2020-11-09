package com.baomidou.dynamic.datasource.plugin;

import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.stereotype.Component;

import java.sql.Statement;

/**
 * 打印SQL执行时间插件
 * TODO: 2020/10/12 慢查询配置，打印SQL
 *
 * @author zhaohaoren
 */
@Slf4j
@Component
@Intercepts({
        @Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}),
//        @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
        @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class})
})
public class SqlInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            return invocation.proceed();
        } finally {
            if (invocation.getTarget() instanceof RoutingStatementHandler) {
                RoutingStatementHandler target = (RoutingStatementHandler) invocation.getTarget();
                ParameterHandler parameterHandler = target.getParameterHandler();
                if (parameterHandler != null) {
                    MappedStatement mappedStatement = (MappedStatement) ReflectionKit.getFieldValue(parameterHandler, "mappedStatement");
                    String statementId = mappedStatement.getId();
                    if (StringUtils.isNotBlank(statementId)) {
                        String[] split = StringUtils.split(statementId, ".");
                        if (split.length >= 2) {
                            String mapper = split[split.length - 2];
                            String method = split[split.length - 1];
                            log.info("[SQL ELAPSED TIME] 耗时：{} ms\t=> 方法：{}#{}", (System.currentTimeMillis() - startTime), mapper, method);
                        }
                    }
                }
            }
        }
    }
}
