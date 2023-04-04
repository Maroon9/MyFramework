package com.xzw.framework.entity;

import com.xzw.framework.annotation.Autowired;
import com.xzw.framework.annotation.Component;
import com.xzw.framework.annotation.Qualifier;
import com.xzw.framework.annotation.Value;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author maroon
 * @date 2023/3/23 21:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component("a")
public class Account {

    @Autowired
    //@Qualifier("myOrder")
    private Order order;

    @Value("1")
    private int id;

    @Value("xx")
    private String name;

    @Value("3")
    private int age;
}
