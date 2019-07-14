package cn.com.jgyhw.sqm.pojo;

import java.util.Date;

/**
 * Created by WangLei on 2019/2/19 0019 20:47
 *
 * 参数配置Pojo
 */
public class ConfigurationPojo {

    /**
     * 标识
     */
    private String id;

    /**
     * 参数名
     */
    private String paramName;

    /**
     * 参数值
     */
    private String paramValue;

    /**
     * 分组
     */
    private String paramGroup;

    /**
     * 备注说明
     */
    private String remark;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 更新时间
     */
    private Long updateTimeLong;

    @Override
    public String toString() {
        return "ConfigurationPojo{" +
                "id='" + id + '\'' +
                ", paramName='" + paramName + '\'' +
                ", paramValue='" + paramValue + '\'' +
                ", paramGroup='" + paramGroup + '\'' +
                ", remark='" + remark + '\'' +
                ", updateTime=" + updateTime +
                ", updateTimeLong=" + updateTimeLong +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getParamGroup() {
        return paramGroup;
    }

    public void setParamGroup(String paramGroup) {
        this.paramGroup = paramGroup;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateTimeLong() {
        return updateTimeLong;
    }

    public void setUpdateTimeLong(Long updateTimeLong) {
        this.updateTimeLong = updateTimeLong;
    }
}
