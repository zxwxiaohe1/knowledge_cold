package com.cdhr.platform.user.utils.soap.init;

import com.cdhr.platform.user.utils.soap.vo.Label;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author heyong
 * @date 2019/8/9
 */
public class ReqBody {

    private String prefix;
    private String elePrefix;
    private String url;
    private Map<String, Object> attributes = new HashMap<>();
    private Label lab;

    public ReqBody() {
    }

    /**
     * 获得实例对象
     * @return
     */
    public static ReqBody getInstance() {
        return new ReqBody();
    }

    /**
     * 添加请求报文体元素标签
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
                if (StringUtils.isNotBlank(label.getValue())) {
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

    public String getElePrefix() {
        return elePrefix;
    }

    public void setElePrefix(String elePrefix) {
        this.elePrefix = elePrefix;
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

    public Label getLab() {
        return lab;
    }

    public void setLab(Label lab) {
        this.lab = lab;
    }
}
