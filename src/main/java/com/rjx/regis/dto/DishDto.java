package com.rjx.regis.dto;

import com.rjx.regis.entity.Dish;
import com.rjx.regis.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    /** 菜品口味*/
    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
