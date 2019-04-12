package com.lance.es.eslearn.entity;

import lombok.Data;

import java.util.Date;

/**
 * 日志Entity，同时以其json字符串存到es中
 */
@Data
public class LogMessage {

    private String appId;

    private String level;

    private String logName;

    private String message;

    private String threadName;

    private Date timeStamp;

}
