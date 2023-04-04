package com.xzw.framework.core;

import com.xzw.framework.annotation.Autowired;
import com.xzw.framework.annotation.Component;
import com.xzw.framework.annotation.Qualifier;
import com.xzw.framework.annotation.Value;
import com.xzw.framework.bean.BeanDefinition;
import com.xzw.framework.utils.MyTools;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author maroon
 * @date 2023/3/23 20:25
 */
public class MyAnnotationConfigApplicationContext {

    private Map<String, Object> cache = new HashMap<>();

    public MyAnnotationConfigApplicationContext(String pack) {
        //遍历包，找到目标类
        Set<BeanDefinition> beanDefinitions = findBeanDefinitions(pack);
        createObject(beanDefinitions);
        autowiredObject(beanDefinitions);
    }

    /**
     * 自动装载
     * @param beanDefinitions
     */
    private void autowiredObject(Set<BeanDefinition> beanDefinitions) {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            Class clazz = beanDefinition.getBeanClass();
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                Autowired autowiredAnnotation = declaredField.getAnnotation(Autowired.class);
                if (autowiredAnnotation != null) {
                    Qualifier qualifierAnnotation = declaredField.getAnnotation(Qualifier.class);
                    if (qualifierAnnotation != null) {
                        //byName
                        String beanName = qualifierAnnotation.value();
                        Object bean = getBean(beanName);
                        String fieldName = declaredField.getName();
                        String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                        try {
                            Method method = clazz.getDeclaredMethod(methodName, declaredField.getType());
                            Object object = getBean(beanDefinition.getBeanName());
                            method.invoke(object, bean);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        //byType
                        for (String beanName : cache.keySet()){
                            if(cache.get(beanName).getClass() == declaredField.getType()) {
                                String fieldName = declaredField.getName();
                                String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                                try {
                                    Method method = clazz.getMethod(methodName, declaredField.getType());
                                    Object object = getBean(beanDefinition.getBeanName());
                                    method.invoke(object,cache.get(beanName));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * //根据目标类创建对象
     * @param beanName
     * @return
     */
    public Object getBean(String beanName) {
        return cache.get(beanName);
    }

    /**
     * 创建实例
     * @param beanDefinitions
     */
    public void createObject(Set<BeanDefinition> beanDefinitions) {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            Class clazz = beanDefinition.getBeanClass();
            String beanName = beanDefinition.getBeanName();
            try {
                //创建的对象
                Object object = clazz.getConstructor().newInstance();
                //完成属性赋值
                Field[] declaredFields = clazz.getDeclaredFields();
                for (Field declaredField : declaredFields) {
                    Value valueAnnotation = declaredField.getAnnotation(Value.class);
                    if (valueAnnotation != null) {
                        String value = valueAnnotation.value();
                        String fieldName = declaredField.getName();
                        String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                        Method method = clazz.getDeclaredMethod(methodName, declaredField.getType());
                        //完成数据类型转换
                        Object val = convertVal(value, declaredField);
                        method.invoke(object, val);
                    }
                }
                //存入缓存
                cache.put(beanName, object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 封装 BeanDefinitions
     * @param pack
     * @return
     */
    public Set<BeanDefinition> findBeanDefinitions(String pack) {
        //获取包下的类
        Set<Class<?>> classes = MyTools.getClasses(pack);
        Set<BeanDefinition> beanDefinitions = new HashSet<>();
        for (Class<?> clazz : classes) {
            //遍历类，拿到有注解的类
            Component componentAnnotation = clazz.getAnnotation(Component.class);
            if (componentAnnotation != null) {
                //获取注解的值
                String beanName = componentAnnotation.value();
                if ("".equals(beanName)) {
                    //获取类名首字母小写
                    String className = clazz.getSimpleName();
                    beanName = className.substring(0, 1).toLowerCase() + className.substring(1);
                }
                //讲这些类封装成 BeanDefinition，装载到集合中
                beanDefinitions.add(new BeanDefinition(beanName, clazz));
            }
        }
        return beanDefinitions;
    }

    /**
     * 将@Value注解中String类型的值转化为相应的值
     * @param value
     * @param field
     * @return
     */
    private Object convertVal(String value, Field field) {
        Object val;
        switch (field.getType().getName()) {
            case "int":
            case "java.lang.Integer":
                    val = Integer.parseInt(value);
                break;
            case "java.lang.String":
                val = value;
                break;
            case "long":
            case "java.lang.Long":
                val = Long.parseLong(value);
                break;
            case "char":
                val = value.charAt(0);
                break;
            case "short":
            case "java.lang.Short":
                val = Short.parseShort(value);
                break;
            case "double":
            case "java.lang.Double":
                val = Double.parseDouble(value);
                break;
            case "boolean":
            case "java.lang.Boolean":
                val = Boolean.parseBoolean(value);
                break;
            case "float":
            case "java.lang.Float":
                val = Float.parseFloat(value);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + field.getType().getName());
        }
        return val;
    }
}
