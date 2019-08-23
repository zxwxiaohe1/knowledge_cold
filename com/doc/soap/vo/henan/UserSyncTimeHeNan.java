package com.cdhr.platform.user.utils.soap.vo.henan;

import com.cdhr.platform.user.utils.soap.vo.RemoteTime;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author heyong
 * @date 2019/8/20
 */
@XmlRootElement(name = "responseBody")
public class UserSyncTimeHeNan extends RemoteTime {

    private String userUpdateTime;

    public String getUserUpdateTime() {
        return userUpdateTime;
    }

    public void setUserUpdateTime(String userUpdateTime) {
        this.userUpdateTime = userUpdateTime;
    }
}
