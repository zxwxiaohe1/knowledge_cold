package com.cdhr.platform.user.utils.soap.vo;

import java.util.List;

/**
 * @author heyong
 * @date 2019/8/9
 */
public class Label {

    private String name;
    private String value;
    private boolean isPrarent;
    private String prarent;
    private String nameSpace;
    private List<Label> labs;

    public Label(String name, String value, boolean isPrarent, String prarent) {
        this.name = name;
        this.value = value;
        this.isPrarent = isPrarent;
        this.prarent = prarent;
    }

    public Label(String name, String value, boolean isPrarent, String prarent, String nameSpace) {
        this.name = name;
        this.value = value;
        this.isPrarent = isPrarent;
        this.prarent = prarent;
        this.nameSpace = nameSpace;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isPrarent() {
        return isPrarent;
    }

    public void setPrarent(boolean prarent) {
        isPrarent = prarent;
    }

    public String getPrarent() {
        return prarent;
    }

    public void setPrarent(String prarent) {
        this.prarent = prarent;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public List<Label> getLabs() {
        return labs;
    }

    public void setLabs(List<Label> labs) {
        this.labs = labs;
    }
}
