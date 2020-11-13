package com.baomidou.dynamic.datasource.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * @see com.baomidou.mybatisplus.core.enums.SqlMethod
 * @author zhaohaoren
 */

@Getter
@AllArgsConstructor
public enum EnhanceMethodSql{

    /**
     * 批量插入
     */
    SAVE_BATCH("saveBatch", "批量插入", "<script>\nINSERT INTO %s %s VALUES %s\n</script>"),

    SELECT_BY_IDS("selectByIds", "根据ID集合，批量查询数据", "<script>SELECT %s FROM %s WHERE %s IN (%s) %s</script>");




    private final String method;
    private final String desc;
    private final String sql;



}
