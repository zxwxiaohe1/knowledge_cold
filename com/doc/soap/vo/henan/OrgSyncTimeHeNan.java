package com.cdhr.platform.user.utils.soap.vo.henan;

import com.cdhr.platform.user.utils.soap.vo.RemoteTime;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author heyong
 * @date 2019/8/20
 */
@XmlRootElement(name = "responseBody")
public class OrgSyncTimeHeNan extends RemoteTime {
    private String orgUpdateTime;

    public String getOrgUpdateTime() {
        return orgUpdateTime;
    }

    public void setOrgUpdateTime(String orgUpdateTime) {
        this.orgUpdateTime = orgUpdateTime;
    }
}
