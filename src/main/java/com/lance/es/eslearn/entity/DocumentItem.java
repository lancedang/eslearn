package com.lance.es.eslearn.entity;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class DocumentItem {

    private long id;

    private String name;

    private Date date;

    private Map<String, String> tags;

}
