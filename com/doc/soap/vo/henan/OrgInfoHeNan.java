package com.cdhr.platform.user.utils.soap.vo.henan;

import com.cdhr.platform.user.utils.soap.vo.RemoteOrg;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author heyong
 * @date 2019/8/12
 */
@XmlRootElement(name = "orgInfo")
public class OrgInfoHeNan extends RemoteOrg {

    private String orgCode;
    private String orgName;
    private String parentCode;
    private String level;
    private String orgLeader;
    private String parentLeader;
    private String phone;
    private String fax;
    private String remark;
    private String order;

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getOrgLeader() {
        return orgLeader;
    }

    public void setOrgLeader(String orgLeader) {
        this.orgLeader = orgLeader;
    }

    public String getParentLeader() {
        return parentLeader;
    }

    public void setParentLeader(String parentLeader) {
        this.parentLeader = parentLeader;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
