package com.meetcity.moon.learn.intf;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by admin on 2017/12/22.
 */
public class TestMain {

    public static void main(String[] args) {
        TestBean b = new TestBean();
        b.setTestInterface(new TestImp());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(b);
            System.out.println("");

            TestBean testBean1 = objectMapper.readValue(json,TestBean.class);
            String a = testBean1.getTestInterface().dof();
            System.out.println("");
        }catch (Throwable t){
            t.printStackTrace();
        }
    }
}
