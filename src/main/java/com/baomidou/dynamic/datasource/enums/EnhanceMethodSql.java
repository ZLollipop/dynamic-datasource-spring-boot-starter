package com.baomidou.dynamic.datasource.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnhanceMethodSql {

    /**
     * 批量插入
     */
    SAVE_BATCH("saveBatch", "批量插入", "<script>\nINSERT INTO %s %s VALUES %s\n</script>");


    private final String method;
    private final String desc;
    private final String sql;
}
