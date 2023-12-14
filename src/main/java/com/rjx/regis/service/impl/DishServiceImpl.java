package com.rjx.regis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rjx.regis.common.CustomException;
import com.rjx.regis.dto.DishDto;
import com.rjx.regis.entity.Dish;
import com.rjx.regis.entity.DishFlavor;
import com.rjx.regis.mapper.DishMapper;
import com.rjx.regis.service.DishFlavorService;
import com.rjx.regis.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Resource
    private DishFlavorService dishFlavorService;


    /**
     * 新增菜品，同时插入菜品对应的口味数据
     */
    @Override
    @Transactional
    public void saveWithFlover(DishDto dishDto) {
        // 保存菜品基本信息到菜品表dish
        this.save(dishDto);
        // 获取菜品ID
        Long dishId = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        // 菜品ID设置进口味中
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        // 菜品口味数据保存
        dishFlavorService.saveBatch(flavors);
    }


    /**
     * 根据ID查询菜品信息以及对应的口味信息
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        // 查询菜品基本信息，从dish表查询
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        // 查询对应的口味信息
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> list = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(list);
        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        // 更新菜品表
        this.updateById(dishDto);
        // 清理当前菜品对应口味数据（删除操作）
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        // 加载当前提交过来的口味数据（插入操作）
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 根据ID删除菜品
     */
    @Override
    public void removeWithDish(List<Long> ids) {
        // 查询菜品状态确定是否可以删除
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dish::getId, ids);
        queryWrapper.eq(Dish::getStatus, 1);
        int count = this.count(queryWrapper);
        // 查到起售数据
        if (count > 0) {
            throw new CustomException("菜品正在售卖,无法删除");
        }
        // 可以删除，先删除菜品对应的口味表数据
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.in(DishFlavor::getDishId, ids);
        dishFlavorService.remove(dishFlavorLambdaQueryWrapper);
        // 再删除菜品
        this.removeByIds(ids);
    }

}
