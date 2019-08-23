package com.cdhr.platform.user.utils.soap.init;

import com.cdhr.platform.user.utils.soap.vo.Label;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author heyong
 * @date 2019/8/9
 */
public class Header {
    private String prefix;
    private String elePrefix;
    private String nameSpace;
    private String url;
    private Map<String, Object> attributes = new HashMap<>();
    private Label lab;

    public Header() {
    }

    /**
     * @return 获得实例对象
     */
    public static Header getInstance() {
        return new Header();
    }

    /**
     * 添加soap元素标签
     * @param label
     */
    public void addAttribute(Label label) {

        if (StringUtils.isNotBlank(label.getPrarent())) {
            if (label.getPrarent().contains(":")) {
                String[] keys = label.getPrarent().split(":");
                Map target = attributes;
                for (String key : keys) {
                    target = (Map) target.get(key);
                }
                if (target != null) {
                    if (label.isPrarent()) {
                        target.put(label.getName(), new HashMap<String, Map>());
                    } else {
                        if (StringUtils.isNotBlank(label.getValue())) {
                            target.put(label.getName(), label.getValue());
                        }
                    }
                }
            }
        } else {
            if (label.isPrarent()) {
                attributes.put(label.getName(), new HashMap<String, Map>());
            } else {
                if (StringUtils.isBlank(label.getValue())) {
                    attributes.put(label.getName(), label.getValue());
                }
            }
        }
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getElePrefix() {
        return elePrefix;
    }

    public void setElePrefix(String elePrefix) {
        this.elePrefix = elePrefix;
    }

    public Label getLab() {
        return lab;
    }

    public void setLab(Label lab) {
        this.lab = lab;
    }
}
