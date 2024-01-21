package com.tu.hb.service;

import com.tu.hb.utils.AlgorithmUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class AlgorithmTest {

    @Test
    void test() {
        List<String> tagsList1 = Arrays.asList("java", "大一", "男");
        List<String> tagsList2 = Arrays.asList("java", "大二", "男");
        List<String> tagsList3 = Arrays.asList("java", "大三", "女");

        System.out.println(AlgorithmUtils.minDistance(tagsList1, tagsList2));
        System.out.println(AlgorithmUtils.minDistance(tagsList1, tagsList3));
    }
}
