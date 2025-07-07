package org.progingo.progingobi.domain.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.Data;

@Data
public class Chart implements Serializable {

    private String chartId;
    private String goal;
    private String name;
    private String chartData;
    private String chartType;
    private String genChart;
    private String genResult;
    private String status;
    private String execMessage;
    private Integer userId;
    private LocalDateTime createTime;
}
