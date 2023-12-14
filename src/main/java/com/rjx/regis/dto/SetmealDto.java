package com.rjx.regis.dto;

import com.rjx.regis.entity.Setmeal;
import com.rjx.regis.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
