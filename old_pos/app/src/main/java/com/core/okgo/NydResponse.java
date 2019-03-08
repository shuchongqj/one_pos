package com.core.okgo;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/22.
 */

public class NydResponse<T> implements Serializable {

    private static final long serialVersionUID = 5213230387175987834L;

    public int code;
    public String msg;
    public T response;
}