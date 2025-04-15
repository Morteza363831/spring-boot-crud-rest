package org.example.springbootcrudrest.config;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class ResultApi<T> {

    private String message;
    private T data;
    private String path;
    private Date timestamp;

    public ResultApi(String message, T data, String path) {
        this.message = message;
        this.data = data;
        this.path = path;
        this.timestamp  = new Date();
    }
}
