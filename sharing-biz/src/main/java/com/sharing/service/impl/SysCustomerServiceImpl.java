package com.sharing.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sharing.common.service.impl.CrudServiceImpl;
import com.sharing.dao.SysCustomerDao;
import com.sharing.dto.SysCustomerDto;
import com.sharing.entity.SysCustomerEntity;
import com.sharing.service.SysCustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
public class SysCustomerServiceImpl extends CrudServiceImpl<SysCustomerDao, SysCustomerEntity, SysCustomerDto> implements SysCustomerService {
    @Override
    public QueryWrapper<SysCustomerEntity> getWrapper(Map<String, Object> params) {
        return null;
    }
}
