package com.keensense.task.async.task;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: TaskCallable
 * @Description: 抽象线程执行类
 * @Author: cuiss
 * @CreateDate: 2019/8/11 12:52
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Slf4j
public class TaskCallable<T> implements Callable<T> {

    @Getter
    @Setter
    private Object object;
    @Getter
    @Setter
    private Method executeMethod;
    @Getter
    private Object[] params = new Object[0];
    @Getter
    private String methodName;
    @Getter
    @Setter
    private Class<?> clazz;
    @Getter
    @Setter
    private T result;
    @Getter
    @Setter
    private String sessionId;
    @Getter
    @Setter
    private boolean stoped = false;
    @Getter
    @Setter
    private boolean finished = false;
    @Getter
    @Setter
    private long timeout = 0L;

    public void setMethodName(String methodName) throws NoSuchMethodError {
        if ("callback".equals(methodName)) {
            log.error(">>>error:不允许的方法名[callback]");
            throw new NoSuchMethodError("不允许的方法名[callback]");
        } else {
            this.methodName = methodName;
        }
    }

    public void setParam(Object[] params) {
        if (params != null) {
            this.params = params;
        }
    }

    /**
     * 停止线程
     */
    public void stop() {
        synchronized (this) {
            this.stoped = true;
        }
    }

    /**
     * 线程是否已经停止
     * 
     * @return
     */
    public synchronized boolean isStoped() {
        synchronized (this) {
            return this.stoped;
        }
    }

    @Override
    public T call() throws Exception {
        log.info("调用开始,超时阀值[{}]秒", this.timeout / 1000);
        long start = System.currentTimeMillis();
        if (this.executeMethod == null) {
            this.initMethod();
        }
        try {
            this.result = (T)this.executeMethod.invoke(this.object, this.params);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(">>>error:执行反射方法报错,Exception:" + e.getMessage());
        } finally {
            this.finished = true;
            log.info("调用完成,耗时[{}]ms", System.currentTimeMillis() - start);
        }
        return this.result;
    }

    /**
     * 初始化方法及参数对比
     * 
     * @throws Exception
     */
    private void initMethod() throws Exception {
        this.clazz = this.object.getClass();
        Method[] methods = this.clazz.getMethods();
        for (int i = 0; i < methods.length; i++) {
            String methodName = methods[i].getName();
            if (methodName.equals(this.methodName)) {
                this.executeMethod = methods[i];
                this.executeMethod.setAccessible(true);
                break;
            }
        }
        if (this.executeMethod == null) {
            log.error(">>>error:找不到方法名[{}.{}]", this.clazz.getName(), this.methodName);
        } else {
            Class<?>[] types = this.executeMethod.getParameterTypes();
            int typesLen = types.length;
            if (typesLen != this.params.length) {
                log.error(">>>error:找不到方法名，参数个数不匹配[{}.{}.{}]", this.clazz.getName(), this.methodName,
                    Arrays.toString(this.params));
            } else {
                for (int i = 0; i < typesLen; ++i) {
                    // 比较参数类型，任一参数类型不对，并且也不是其子类，则抛出异常
                    if (this.params[i] != null && !types[i].equals(this.params[i].getClass())
                        && !types[i].isAssignableFrom(this.params[i].getClass())) {
                        log.error("找不到方法名，第[{}]个参数类型[{}]不匹配[{}.{}.{}]", i, this.params[i].getClass().getName(),
                            this.clazz.getName(), this.methodName, Arrays.toString(types));
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("当前线程:").append(this.sessionId);
        sb.append("实例:").append(this.clazz).append("->").append(null == this.object ? null : this.object.toString());
        sb.append("函数:").append(null == this.executeMethod ? null : this.executeMethod.getName());
        sb.append("函数名:").append(this.methodName);
        sb.append("入参:").append(null == this.params ? null : Arrays.toString(this.params));
        return sb.toString();
    }

}
