package com.lwc.domain.auth;

import java.util.Date;

/**
 * ClassName: AuthElementOperation
 * Description:
 *页面元素操作表
 * @Author 林伟朝
 * @Create 2024/10/14 22:04
 */
public class AuthElementOperation {
    private Long id;
    private String elementName;
    private String elementCode;//页面元素唯一编码
    private String operationType;//操作类型
    private Date createTime;
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public String getElementCode() {
        return elementCode;
    }

    public void setElementCode(String elementCode) {
        this.elementCode = elementCode;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
