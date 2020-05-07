
package com.sharing.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.sharing.common.constant.Constant;
import com.sharing.common.service.CrudService;
import com.sharing.common.utils.ConvertUtils;
import com.sharing.common.utils.PageData;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *  CRUD基础服务类
 *
 * @author tecsmile@outlook.com
 */
public abstract class CrudServiceImpl<M extends BaseMapper<T>, T, D> extends BaseServiceImpl<M, T> implements CrudService<T, D> {

    protected Class<D> currentDtoClass() {
        return (Class<D>) ReflectionKit.getSuperClassGenericType(getClass(), 2);
    }

    @Override
    public PageData<D> page(Map<String, Object> params) {
        IPage<T> page = baseDao.selectPage(
            getPage(params, Constant.CREATE_TIME, false),
            getWrapper(params)
        );

        return getPageData(page, currentDtoClass());
    }

    @Override
    public List<D> list(Map<String, Object> params) {
        List<T> entityList = null;
        try {
            entityList = baseDao.selectList(getWrapper(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<D> ds = null;
        try {
            ds = ConvertUtils.sourceToTarget(entityList, currentDtoClass());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;

    }

    public abstract QueryWrapper<T> getWrapper(Map<String, Object> params);

    @Override
    public D get(Long id) {
        T entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, currentDtoClass());
    }

    @Override
    public void save(D dto)  {
        T entity = ConvertUtils.sourceToTarget(dto, currentModelClass());
        insert(entity);
    }

    @Override
    public void update(D dto) {
        T entity = ConvertUtils.sourceToTarget(dto, currentModelClass());
        updateById(entity);
    }

    @Override
    public void delete(Long[] ids) {
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }
}
