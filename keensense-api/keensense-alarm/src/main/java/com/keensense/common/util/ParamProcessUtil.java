package com.keensense.common.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.*;

import java.util.*;

/**
 * @author ycl
 * @date 2019/5/16
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParamProcessUtil {

    private static String processParam(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        if ("id".equalsIgnoreCase(name)) {
            name = "id";
        }
        if (name.contains("ID")) {
            name = name.replace("ID", "Id");
        }
        char[] chars = name.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }

    public static List<QueryParam> getQueryList(String param) {
        if (StringUtils.isEmpty(param)) {
            return Collections.emptyList();
        }
        List<QueryParam> queryParams = new ArrayList<>();
        ///统一请求参数格式为&间隔
        String[] ands = param.split("&");
        for (String sub : ands) {
            ///统一不加括号的参数请求
            String[] seps = {">=", "<=", "<", ">"};
            boolean isContinue = true;
            for (String sep : seps) {
                if (sub.contains(sep)) {
                    String[] split = sub.split(sep);
                    String[] keys = split[0].split("\\.");
                    String key = "";
                    if (keys.length >= 2) {
                        key = processParam(keys[1]);
                    }
                    QueryParam queryParam = new QueryParam();
                    queryParam.setColumn(StringUtils.camelToUnderline(key));
                    queryParam.setOperate(sep);
                    queryParam.setValue(split[1]);
                    queryParams.add(queryParam);
                    isContinue = false;
                    break;
                }
            }
            //继续解析
            if (isContinue && sub.contains("=")) {
                analysisParam(queryParams, sub);
            }
        }
        return queryParams;
    }

    private static void analysisParam(List<QueryParam> queryParams, String sub) {
        String[] split = sub.split("=");
        if ("RecordStartNo".equals(split[0])) {
            queryParams.add(new QueryParam("page", "pageNo", split[1]));
            return;
        }
        if ("PageRecordNum".equals(split[0])) {
            queryParams.add(new QueryParam("page", "pageSize", split[1]));
            return;
        }
        if ("Sort".equals(split[0])) {
            String op = split[1].contains("-") ? "desc" : "asc";
            String val = processParam(split[1].split("\\.")[1]);
            queryParams.add(new QueryParam("sort", op, StringUtils.camelToUnderline(val)));
            return;
        }
        String[] keys = split[0].split("\\.");
        String key = "";
        QueryParam queryParam = new QueryParam();
        if (keys.length == 2) {
            queryParam.setOperate("=");
            key = processParam(keys[1]);
        } else if (keys.length == 3) {
            key = processParam(keys[1]);
            switch (keys[2]) {
                case "In":
                    queryParam.setOperate("in");
                    break;
                case "Like":
                    queryParam.setOperate("like");
                    break;
                case "Gte":
                    queryParam.setOperate(">=");
                    break;
                case "Gt":
                    queryParam.setOperate(">");
                    break;
                case "Lte":
                    queryParam.setOperate("<=");
                    break;
                case "Lt":
                    queryParam.setOperate("<");
                    break;
                default:
                    break;
            }
        }
        queryParam.setColumn(StringUtils.camelToUnderline(key));
        queryParam.setValue(split[1]);
        queryParams.add(queryParam);
    }

    public static <T> QueryWrapper<T> getWrapper(List<QueryParam> queryParams) {
        QueryWrapper<T> query = Wrappers.query();
        for (QueryParam queryParam : queryParams) {
            switch (queryParam.getOperate()) {
                case "=":
                    query = query.eq(queryParam.getColumn(), queryParam.getValue());
                    break;
                case ">=":
                    query = query.ge(queryParam.getColumn(), queryParam.getValue());
                    break;
                case ">":
                    query = query.gt(queryParam.getColumn(), queryParam.getValue());
                    break;
                case "<=":
                    query = query.le(queryParam.getColumn(), queryParam.getValue());
                    break;
                case "<":
                    query = query.lt(queryParam.getColumn(), queryParam.getValue());
                    break;
                case "like":
                    query = query.like(queryParam.getColumn(), "%".concat(queryParam.getValue()).concat("%"));
                    break;
                case "in":
                    query = query.in(queryParam.getColumn(), queryParam.getValue().split(","));
                    break;
                case "desc":
                    query = query.orderByDesc(queryParam.getValue());
                    break;
                case "asc":
                    query = query.orderByAsc(queryParam.getValue());
                    break;
                default:
                    break;
            }

        }
        return query;

    }

    public static <T> Page<T> getQueryPage(List<QueryParam> queryParams) {
        int pageNo = 1;
        int pageSize = 10;
        for (QueryParam queryParam : queryParams) {
            if ("pageSize".equals(queryParam.getOperate())) {
                pageSize = Integer.parseInt(queryParam.getValue());
            }
            if ("pageNo".equals(queryParam.getOperate())) {
                pageNo = Integer.parseInt(queryParam.getValue());
            }
        }
        return new Page<T>(pageNo, pageSize);

    }

    public static List<Map<String, String>> pair2Map(String pair) {
        if (StringUtils.isEmpty(pair)) {
            return Collections.emptyList();
        }
        List<Map<String, String>> list = new ArrayList<>();
        String[] ors = pair.split("\\)or\\(");
        for (String or : ors) {
            Map<String, String> map = new HashMap<>();
            String[] ands = or.split("\\)and\\(");
            for (String and : ands) {
                String[] kv = and.replace("(", "").replace(")", "").split("=");
                if (kv.length > 1) {
                    map.put(kv[0], kv[1]);
                }
            }
            if (!map.isEmpty()) {
                list.add(map);
            }

        }
        return list;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class QueryParam {
        private String column;
        private String operate;
        private String value;
    }
}
