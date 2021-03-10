package com.shouzhi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import javax.management.RuntimeErrorException;
import java.io.IOException;
import java.util.ArrayList;

public class AssertTest {

    @Test
    public void contextLoads() {
        /*String str = "123";
        str=null;
        Assert.notNull(str,"字符串不能为空！");*/

        //---------------------------------
        /*ArrayList<String> list = new ArrayList<>();
        Assert.notEmpty(list,"集合不能为空！");*/

        //---------------------------------
        /*String str = "123";
        str=null;
        str="";
        str=" ";
        Assert.hasLength(str, "不能为NULL或长度必须大于0(空格会通过检查哦~)");*/

        //---------------------------------
        /*String str = "123";
        str=null;
        str="";
        str=" ";
        str=" 1";
        Assert.hasText(str, "str不能为Null且必须至少包含一个非空格的字符，否则抛出异常");*/

        //---------------------------------
        /*String str = "123";
        Assert.isInstanceOf(Integer.class, str, "如果 obj 不能被正确转型为 clazz 指定的类将抛出异常");*/

        //---------------------------------
        // Class<?> superType, Class<?> subType
        // Assert.isAssignable(RuntimeException.class, IllegalArgumentException.class);
        // IllegalArgumentException的超类不属于IOException，IOException和RuntimeException是同级
        //Assert.isAssignable(IOException.class, IllegalArgumentException.class);


        //---------------------------------
        // Assert.doesNotContain("hello word", "o r", "给定的文本不包含给定的子串才通过");


        //---------------------------------
        /*String [] array = new String[2];
        array[0]="123";
        // array[1]="456";
        Assert.noNullElements(array, "一个数组没有null元素才通过");*/


        //---------------------------------
        Integer logOperCount = 1;
        Integer logOperDetailCount = 1;

        // Assert.isTrue(logOperCount!=1||logOperDetailCount!=1, "");
        System.out.println(logOperCount==1&&logOperDetailCount==1);
        System.out.println("我通过检查了");
    }

}
