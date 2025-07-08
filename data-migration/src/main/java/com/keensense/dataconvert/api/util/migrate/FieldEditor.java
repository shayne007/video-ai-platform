package com.keensense.dataconvert.api.util.migrate;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.api.util.migrate
 * @Description： <p> FieldEditor 属性转换  </p>
 * @Author： - Jason
 * @CreatTime：2019/7/27 - 15:12
 * @Modify By：
 * @ModifyTime： 2019/7/27
 * @Modify marker：
 */
public interface FieldEditor<T> {

    /**
     * 修改过滤后的结果
     * @param t 被过滤的对象
     * @return 修改后的对象，如果被过滤返回<code>null</code>
     */
    T edit(T t);


}
