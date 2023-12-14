package com.rjx.regis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rjx.regis.entity.Category;

public interface CategoryService extends IService<Category> {

    /**
     * 根据ID删除分类
     */
    public void remove(Long id);

}
