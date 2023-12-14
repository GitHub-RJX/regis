package com.rjx.regis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rjx.regis.dto.SetmealDto;
import com.rjx.regis.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    /**
     * 新增套餐以及新增套餐关联关系
     */
    void saveWithDish(SetmealDto setmealDto);


    /**
     * 根据ID删除套餐信息，同时删除所关联的菜品
     */
    void removeWithDish(List<Long> ids);

    /**
     * 修改套餐
     */
    void updateWithSetmeal(SetmealDto setmealDto);

    /**
     * 根据ID查套餐信息
     */
    SetmealDto getByIdWithDish(Long id);
}
