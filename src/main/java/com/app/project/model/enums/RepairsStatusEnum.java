package com.app.project.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户角色枚举
 *
 * @author 
 * @from 
 */
public enum RepairsStatusEnum {

    IN_REVIEW("审核中", 0),
    REJECTED("已驳回", 1),
    UNDER_REPAIR("维修中", 2),
    UNREPAIRABLE("无法维修", 3),
    PENDING_EVALUATION("待评价", 4),
    COMPLETED("已完成", 5);

    private final String text;

    private final Integer value;

    RepairsStatusEnum(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static RepairsStatusEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (RepairsStatusEnum anEnum : RepairsStatusEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public Integer getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
