package com.xzw.framework.bean;

import com.xzw.framework.annotation.Component;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author maroon
 * @date 2023/3/23 20:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BeanDefinition {

    private String beanName;

    private Class beanClass;
}
