package com.keensense.admin.enums;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 结构化-对接佳都管理案件的状态
 * 0 已处警 1 已受理 2 已立案 3 已破案 4 已结案 5 已销案 6 已不立 7 已移交 8 已破未结 9 撤案转行政处罚
 * 50 不处理 51 已调解 52 已终止 59 已终结
 * 60 已处罚 61 已受理未结 62 当场处罚
 * 20 审查中 21 已审查
 * 99 其他
 *
 * @author:duf
 * @version:1.0.0
 * @date 2018/12/17
 */
public enum CaseJdStatus {
    REPORT_POLICE(0, "已处警"),
    ACCEPTED_POLICE(1, "已受理"),
    FILING_CASE(2, "已立案"),
    SOLVE_CASE(3, "已破案"),
    CLOSE_CASE(4, "已结案"),
    DESTROY_CASE(5, "已销案"),
    NOT_STANDING_CASE(6, "已不立"),
    TRANSFER_CASE(7, "已移交"),
    SOLVE_NOT_CLOSE_CASE(8, "已破未结"),
    WITHDRAWAL_TO_PUNISHMENT_CASE(9, "撤案转行政处罚"),
    NOT_PROCESSED_CASE(50, "不处理"),
    MEDIATION_CASE(51, "已调解"),
    TERMINATION_CASE(52, "已终止"),
    END_CASE(59, "已终结"),
    PUNISHED_CASE(60, "已处罚"),
    ACCEPTED_NOT_CLOSE_CASE(61, "已受理未结"),
    IMMEDIATELY_PUNISHMENT_CASE(62, "当场处罚"),
    UNDER_REVIEW_CASE(20, "审查中"),
    REVIEWED_CASE(21, "已审查"),
    OTHERS_CASE(99, "其他");

    private int value;
    private String desc;

    CaseJdStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }


    public String getDesc() {
        return desc;
    }


    public static String getDescByValue(int value) {
        for (CaseJdStatus caseStatus : CaseJdStatus.values()) {
            if (caseStatus.getValue() == value) {
                return caseStatus.getDesc();
            }
        }
        return null;
    }

    public static List<Map<Integer, String>> getAllDescAndValue() {

        List<Map<Integer, String>> restList = new ArrayList<>();
        for (CaseJdStatus caseStatus : CaseJdStatus.values()) {
            Map<Integer, String> restMap = new TreeMap<>();
            restMap.put(caseStatus.getValue(), caseStatus.getDesc());
            restList.add(restMap);
        }
        return restList;
    }

    public static boolean isExit(int value) {
        for (CaseJdStatus caseStatus : CaseJdStatus.values()) {
            if (caseStatus.getValue() == value) {
                return true;
            }
        }
        return false;
    }

}
