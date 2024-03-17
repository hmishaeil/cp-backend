package com.cp.backend.exception;

import java.util.Date;

import lombok.Data;

@Data
public class ExceptionMessage {
    private Integer status;
    private Date date = new Date();
    private String message;
    private String path;
}
