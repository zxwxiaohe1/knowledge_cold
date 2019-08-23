package com.cdhr.platform.portal.struct.xml.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;


/**
 * @author heyong
 * @date 2019/08/22
 */
@XmlRootElement(name = "ArrayOfPlatformConfigSetting")
public class ConfigPropertyStorage {

    private List<ConfigProperty> platformConfigSettingList = new ArrayList<ConfigProperty>();

    @XmlElement(name = "PlatformConfigSetting")
    public List<ConfigProperty> getPlatformConfigSettingList() {
        return platformConfigSettingList;
    }

    public void setPlatformConfigSettingList(List<ConfigProperty> platformConfigSettingList) {
        this.platformConfigSettingList = platformConfigSettingList;
    }
}
