package com.zorzanelli.repositoryinfo.entity;

import java.io.Serializable;

public class FileInfo implements Serializable {

    private Long totalLines = 0L;
    private Double size = 0D;

    public FileInfo(Long totalLines, Double size) {
        this.totalLines = totalLines;
        this.size = size;
    }

    public Long getTotalLines() {
        return totalLines;
    }

    public void setTotalLines(Long totalLines) {
        this.totalLines = totalLines;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }
}
