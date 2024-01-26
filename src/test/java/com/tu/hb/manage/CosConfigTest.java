package com.tu.hb.manage;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class CosConfigTest {

    @Resource
    private CosManager cosManager;

    @Test
    void putObject() {
        String fileName = "D:\\codeByPlanet\\hb\\hb-backend\\src\\main\\resources\\test.png";
        cosManager.putObject("test.png", fileName);
    }
}
