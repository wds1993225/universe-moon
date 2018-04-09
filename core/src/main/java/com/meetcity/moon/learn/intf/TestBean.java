package com.meetcity.moon.learn.intf;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by admin on 2017/12/22.
 */

public class TestBean implements Serializable{

    private TestInterface testInterface  = new TestImp();

    private String name;
    private int age = 6;

    public TestInterface getTestInterface() {
        return testInterface;
    }

    public void setTestInterface(TestInterface testInterface) {
        this.testInterface = testInterface;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
