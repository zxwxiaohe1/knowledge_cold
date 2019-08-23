package com.cdhr.platform.user.utils.soap.vo.henan;



import com.cdhr.platform.user.utils.soap.vo.RemoteUser;

import javax.xml.bind.annotation.*;

/**
 * @author heyong
 * @date 2019/8/9
 */

@XmlRootElement(name = "userInfo")
public class UserInfoHeNan extends RemoteUser {
    private String userCode;
    private String userName;
    private String invalidDate;
    private String sex;
    private String birthday;
    private String countryEmail;
    private String localEmail;
    private String userType;
    private String mobile;
    private String phone;
    private String position;
    private String technical;
    private String ethnicity;
    private String politic;
    private String education;
    private DepartmentHeNan orgInfo;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getInvalidDate() {
        return invalidDate;
    }

    public void setInvalidDate(String invalidDate) {
        this.invalidDate = invalidDate;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCountryEmail() {
        return countryEmail;
    }

    public void setCountryEmail(String countryEmail) {
        this.countryEmail = countryEmail;
    }

    public String getLocalEmail() {
        return localEmail;
    }

    public void setLocalEmail(String localEmail) {
        this.localEmail = localEmail;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getTechnical() {
        return technical;
    }

    public void setTechnical(String technical) {
        this.technical = technical;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }

    public String getPolitic() {
        return politic;
    }

    public void setPolitic(String politic) {
        this.politic = politic;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public DepartmentHeNan getOrgInfo() {
        return orgInfo;
    }

    public void setOrgInfo(DepartmentHeNan orgInfo) {
        this.orgInfo = orgInfo;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }
}
