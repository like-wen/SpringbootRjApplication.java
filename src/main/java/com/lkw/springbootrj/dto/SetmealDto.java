package com.lkw.springbootrj.dto;

import com.lkw.springbootrj.entity.Setmeal;
import com.lkw.springbootrj.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
