package com.rjx.regis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rjx.regis.dto.DishDto;
import com.rjx.regis.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {

    // 新增彩屏，同时插入菜品对应的口味数据
    public void saveWithFlover(DishDto dishDto);


    /**
     * 根据ID查询菜品信息以及对应的口味信息
     */
    public DishDto getByIdWithFlavor(Long id);

    /**
     * 更新菜品信息同时更新口味信息
     */
    public void updateWithFlavor(DishDto dishDto);

    /**
     * 根据ID删除菜品
     */
    void removeWithDish(List<Long> ids);
}
