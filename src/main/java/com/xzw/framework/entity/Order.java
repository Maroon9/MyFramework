package com.xzw.framework.entity;

import com.xzw.framework.annotation.Component;
import com.xzw.framework.annotation.Value;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author maroon
 * @date 2023/3/23 22:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
//@Component("myOrder")
@Component
public class Order {

    @Value("1")
    private String id;

    @Value("100.5")
    private Float price;
}
