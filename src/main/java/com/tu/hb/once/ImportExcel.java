package com.tu.hb.once;

import com.alibaba.excel.EasyExcel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class ImportExcel {
    public static void main(String[] args) {
        String fileName = "D:\\codeByPlanet\\hb\\hb-backend\\src\\main\\resources\\testExcel.xlsx";
        ReadByListener(fileName);
        synchronousRead(fileName);
    }

    /**
     * 监听器读取
     * @param fileName
     */
    public static void ReadByListener(String fileName) {
        // 写法1：JDK8+ ,不用额外写一个TestUserInfoListener
        // since: 3.0.0-beta1

        // 这里默认每次会读取100条数据 然后返回过来 直接调用使用数据就行
        // 具体需要返回多少行可以在`PageReadListener`的构造函数设置
        EasyExcel.read(fileName, TestUserInfo.class, new TableListener()).sheet().doRead();
    }

    /**
     * 同步的返回，不推荐使用，如果数据量大会把数据放到内存里面
     */
    public static void synchronousRead(String fileName) {
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 同步读取会自动finish
        List<TestUserInfo> totalDataList = EasyExcel.read(fileName).head(TestUserInfo.class).sheet().doReadSync();
        System.out.println("总数：" + totalDataList.size());
        Map<String, List<TestUserInfo>> listMap = totalDataList.stream()
                .filter(total -> StringUtils.isNotEmpty(total.getUsername()))
                .collect(Collectors.groupingBy(TestUserInfo::getUsername));
        for (Map.Entry<String, List<TestUserInfo>> stringListEntry: listMap.entrySet()) {
            if (stringListEntry.getValue().size() > 1) {
                System.out.println("username = " + stringListEntry.getKey());
            }
        }
        System.out.println("不重复昵称数：" + listMap.keySet().size());
    }
}
