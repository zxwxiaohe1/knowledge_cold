package com.cdhr.platform.portal.struct.xml.factory;

import com.cdhr.platform.portal.struct.xml.model.ConfigProperty;
import com.cdhr.platform.portal.struct.xml.model.ConfigPropertyStorage;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author heyong
 * @date 2019/8/23
 */
@Component
public class CreateXmlModel {

    private final String FILES_XML_BASE_DATA = "base_data_config.xml";

    public ConfigPropertyStorage getConfigModel() throws JAXBException {
        ConfigPropertyStorage cps = new ConfigPropertyStorage();
        List<ConfigProperty> cplist = new ArrayList<>();
        String[] files = FILES_XML_BASE_DATA.split(",");
        for (String file : files) {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("configxml/" + file);
            if (inputStream == null) {
                return null;
            }
            cplist.addAll(getBeanFromStream(inputStream));
        }
        cps.setPlatformConfigSettingList(cplist);
        return cps;
    }

    /**
     * @param inputStream
     * @return List<ConfigProperty>
     * @throws JAXBException
     */
    public List<ConfigProperty> getBeanFromStream(InputStream inputStream) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(ConfigPropertyStorage.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Object object = unmarshaller.unmarshal(inputStream);
        ConfigPropertyStorage cs = (ConfigPropertyStorage) object;
        return cs.getPlatformConfigSettingList();
    }
}
