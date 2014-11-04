package com.redditspider.biz.manager.task;

import java.lang.reflect.Method;

/**
 * Thread to start thread in parallel.
 */
public class ParallelTask implements Runnable {
    private Object objectToExecute;
    private String callback;

    public ParallelTask(Object objectToExecute, String callback) {
        this.objectToExecute = objectToExecute;
        this.callback = callback;
    }

    public void run() {
        Method method = getMethodToInvoke();
        invoke(method);
    }

    private void invoke(Method method) {
        try {
            method.invoke(objectToExecute);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Method getMethodToInvoke() {
        Method method = null;
        for (Method m : objectToExecute.getClass().getDeclaredMethods()) {    //try only (including private) declared methods by this class
            if (m.getName().equals(callback)) {
                method = m;
            }
        }
        return method;
    }
}
