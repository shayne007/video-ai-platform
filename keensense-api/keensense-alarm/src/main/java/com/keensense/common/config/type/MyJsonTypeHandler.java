package com.keensense.common.config.type;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author ycl
 * @date 2019/5/20
 */
@MappedTypes(JSONObject.class)
public class MyJsonTypeHandler extends BaseTypeHandler<JSONObject> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, JSONObject jsonObject, JdbcType jdbcType) throws SQLException {
        ps.setString(i, jsonObject.toJSONString());
    }

    @Override
    public JSONObject getNullableResult(ResultSet rs, String s) throws SQLException {
        return JSON.parseObject(rs.getString(s));
    }

    @Override
    public JSONObject getNullableResult(ResultSet rs, int i) throws SQLException {
        return JSON.parseObject(rs.getString(i));
    }

    @Override
    public JSONObject getNullableResult(CallableStatement cs, int i) throws SQLException {
        return JSON.parseObject(cs.getString(i));
    }
}
