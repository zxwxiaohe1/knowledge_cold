package com.cdhr.platform.portal.struct.xml.model;

import org.springframework.stereotype.Repository;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author heyong
 * @date 2019/08/22
 */
@XmlRootElement(name = "KeyValue")
public class ValueInfo {

    @XmlElement(name = "Key")
    private String Key;
    @XmlElement(name = "Value")
    private  String Value;


    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        this.Key = Key;
    }


    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        this.Value = Value;
    }
}
