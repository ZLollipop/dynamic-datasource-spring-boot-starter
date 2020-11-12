package com.baomidou.dynamic.datasource.mapper.method;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * 主要2个  tableInfo的 以及 SqlScriptUtils 的方法
 * @author zhaohaoren
 */
public class SaveBatch extends AbstractMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sql;
        // format
        SqlMethod sqlMethod = SqlMethod.INSERT_ONE;
        // field
        // TODO: 2020/11/12 这个不对劲，不需要排除那些null的
//        String columnScript = SqlScriptUtils.convertTrim(tableInfo.getAllInsertSqlColumnMaybeIf(),
//                LEFT_BRACKET, RIGHT_BRACKET, null, COMMA);

        String columnScript = prepareFieldSql(tableInfo);

        // TODO: 2020/11/12  这个东西怎么使用mp提供的来获取到？
        StringBuilder valueSql = new StringBuilder();
        valueSql.append(LEFT_BRACKET);
        tableInfo.getFieldList().forEach(x -> valueSql.append("#{item.").append(x.getProperty()).append("},"));
        valueSql.append(RIGHT_BRACKET);
        String valuesScript = SqlScriptUtils.convertForeach(valueSql.toString(), COLLECTION, null, "item", COMMA);

        sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(), columnScript, prepareValuesSqlForMysqlBatch(tableInfo));
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return this.addInsertMappedStatement(mapperClass, modelClass, "saveBatch", sqlSource, new NoKeyGenerator(), null, null);
    }

    /*（x,x,x,x,x）*/
    private String prepareFieldSql(TableInfo tableInfo) {
        StringBuilder fieldSql = new StringBuilder();

        fieldSql.append(tableInfo.getKeyColumn()).append(COMMA);

        tableInfo.getFieldList().forEach(x -> {
            fieldSql.append(x.getColumn()).append(COMMA);
        });

        fieldSql.delete(fieldSql.length() - 1, fieldSql.length());

        fieldSql.insert(0, LEFT_BRACKET);

        fieldSql.append(RIGHT_BRACKET);

        return fieldSql.toString();
    }

    private String prepareValuesSqlForMysqlBatch(TableInfo tableInfo) {
        final StringBuilder valueSql = new StringBuilder();
        valueSql.append("<foreach collection=\"coll\" item=\"item\" index=\"index\" open=\"(\" separator=\"),(\" close=\")\">");
        valueSql.append("#{item.").append(tableInfo.getKeyProperty()).append("},");
        tableInfo.getFieldList().forEach(x -> valueSql.append("#{item.").append(x.getProperty()).append("},"));


        valueSql.delete(valueSql.length() - 1, valueSql.length());
        valueSql.append("</foreach>");
        return valueSql.toString();
    }
}