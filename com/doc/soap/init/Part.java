package com.cdhr.platform.user.utils.soap.init;


import java.util.HashMap;
import java.util.Map;

/**
 * @author heyong
 * @date 2019/8/9
 */
public class Part {

    private String prefix;
    private String nameSpace;
    private Map<String, String> attributes = new HashMap<>();

    public  Part() {
    }

    public static Part getInstance() {
        return new Part();
    }

    public void addAttribute(String name, String value) {
        attributes.put(name, value);
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }
}
