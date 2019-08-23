package com.cdhr.platform.portal.struct.xml.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author heyong
 * @date 2019/08/22
 */
@XmlRootElement
public class ConfigProperty {

    private String groupid;
    private String groupname;
    private String key;
    private String displayName;
    private String value;
    private List<ValueInfo> listValue = new ArrayList<>();

    @XmlElement(name = "groupid")
    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    @XmlElement(name = "groupname")
    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    @XmlElement(name = "key")
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @XmlElement(name = "displayName")
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @XmlElement(name = "value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @XmlElement(name = "KeyValue")
    @XmlElementWrapper(name = "list")
    public List<ValueInfo> getListValue() {
        return listValue;
    }

    public void setListValue(List<ValueInfo> listValue) {
        this.listValue = listValue;
    }
}
