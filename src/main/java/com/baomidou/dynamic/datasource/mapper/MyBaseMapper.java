package com.baomidou.dynamic.datasource.mapper;

import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface MyBaseMapper<T> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T> {
    /**
     * 默认批次提交数量
     */
    int DEFAULT_BATCH_SIZE = 1000;

    int saveBatch(@Param(Constants.COLLECTION) Collection<T> entityList);
}