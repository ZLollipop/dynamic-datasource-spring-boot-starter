package com.baomidou.dynamic.datasource.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@SuppressWarnings("All")
@Repository
public interface MyBaseMapper<T> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T> {
    /**
     * 默认批次提交数量
     */
    int DEFAULT_BATCH_SIZE = 1000;

    //region 查
    // 通过主键id查询
    T selectById(Serializable id); // ok
    // 多个主键id获取多个值
    List<T> selectByIds(@Param(Constants.COLLECTION) Collection<? extends Serializable> idList); //ok
    // 通过传递对象中有值的字段进行匹配查询
    List<T> selectListByEntity(@Param(Constants.ENTITY) T entity);
    // 通过传递对象中有值的字段进行匹配查询，结果以某个字段转为Map
    Map<Object, List<T>> selectMapByEntitiy(@Param(Constants.ENTITY) T entity);
    // 通过传递对象中有值的字段进行匹配查询，结果分页获取
    <E extends IPage<T>> E selectPage(E page, @Param(Constants.WRAPPER) Wrapper<T> queryWrapper); //ok
    int count();
    //endregion

    //region 增
    int insert();
    int insertSelective();
    int insertBatch(@Param(Constants.COLLECTION) Collection<T> entityList, int batchSize);
    //endregion

    //region 改
    int update(T entity);
    // 实体类的条件删除
    int updateById(T entity); //ok
    int updateBatchByIds();
    int insertOrUpdateById();
    int insertOrUpdateBatchByIds();

    //endregion

    //region 删
    // id删除
    int deleteById(Serializable id); //ok
    // ids 删除
    int deleteByIds();
    // 实体类的条件删除
    int deleteByEntity(@Param(Constants.ENTITY) T entity);
    //endregion
}