package com.cdhr.platform.user.utils.soap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.cdhr.platform.user.consts.SystemConfig;
import com.cdhr.platform.user.utils.soap.enums.InterConnectArea;
import com.cdhr.platform.user.utils.soap.vo.Label;
import com.cdhr.platform.user.utils.soap.vo.RemoteOrg;
import com.cdhr.platform.user.utils.soap.vo.RemoteTime;
import com.cdhr.platform.user.utils.soap.vo.RemoteUser;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author heyong
 * @date 2019/8/12
 */
public abstract class SyncService {

    @Autowired
    private SoapFactory soapFactory;

    protected List<Label> increaseHeaders;
    protected List<Label> increaseBody;
    private static final Logger logger = LoggerFactory.getLogger(SyncService.class);

    /**
     * 获取用户上次同步时间
     *
     * @param parammap
     * @param tagName
     * @param area
     * @return
     * @throws SOAPException
     * @throws TransformerException
     * @throws UnsupportedEncodingException
     */
    public RemoteTime getUserSyncTime(Map<String, String> parammap, Class clazz, String tagName, InterConnectArea area)
            throws SOAPException, TransformerException, UnsupportedEncodingException, JAXBException {
        String url = parammap.get("requesturl");
        String namespace = parammap.get("ns");
        String synusername = SystemConfig.GetConfigValue("ExternalService", "OSB.Appid");
        String synuserpw = SystemConfig.GetConfigValue("ExternalService", "OSB.Password");

        logger.info(String.format("获取更新时间。%s:%s@%s?PSCode=%s&BSCode=%s&namespace=%s",
                synusername, synuserpw, url, parammap.get("PSCode"), parammap.get("BSCode"), namespace));
        area.instance();
        return getSyncTime(area.initConnectInfo(url, synusername, synuserpw),
                tagName, clazz, increaseHeaders, increaseBody);
    }


    /**
     * 获取部门上次同步时间
     *
     * @param parammap
     * @param tagName
     * @param area
     * @return
     * @throws SOAPException
     * @throws TransformerException
     * @throws UnsupportedEncodingException
     */
    public RemoteTime getOrgSyncTime(Map<String, String> parammap, Class clazz, String tagName, InterConnectArea area)
            throws SOAPException, TransformerException, UnsupportedEncodingException, JAXBException {
        String url = parammap.get("requesturl");
        String namespace = parammap.get("ns");
        String synusername = SystemConfig.GetConfigValue("ExternalService", "OSB.Appid");
        String synuserpw = SystemConfig.GetConfigValue("ExternalService", "OSB.Password");

        logger.info(String.format("获取更新时间。%s:%s@%s?PSCode=%s&BSCode=%s&namespace=%s",
                synusername, synuserpw, url, parammap.get("PSCode"), parammap.get("BSCode"), namespace));

        area.instance();

        return getSyncTime(area.initConnectInfo(url, synusername, synuserpw),
                tagName, clazz, increaseHeaders, increaseBody);
    }


    /**
     * 获取用户
     *
     * @param parammap
     * @param tagName
     * @param area
     * @return
     * @throws SOAPException
     * @throws TransformerException
     * @throws UnsupportedEncodingException
     * @throws JAXBException
     */
    public List<RemoteUser> getUser(Map<String, String> parammap, String tagName,Class clazz, InterConnectArea area)
            throws SOAPException, TransformerException, UnsupportedEncodingException, JAXBException {

        // 初始化请求报文结构
        area.instance();
        String synusername = SystemConfig.GetConfigValue("ExternalService", "OSB.Appid");
        String synuserpw = SystemConfig.GetConfigValue("ExternalService", "OSB.Password");

        // 创建soap请求认证并获取远程数据
        return getSyncUserInfo(area.initConnectInfo(parammap.get("requesturl"), synusername, synuserpw),
                tagName, clazz, increaseHeaders, increaseBody);
    }


    /**
     * 获取部门
     *
     * @param parammap
     * @param tagName
     * @param area
     * @return
     * @throws SOAPException
     * @throws TransformerException
     * @throws UnsupportedEncodingException
     * @throws JAXBException
     */
    public List<RemoteOrg> getOrg(Map<String, String> parammap, String tagName,Class clazz, InterConnectArea area)
            throws SOAPException, TransformerException, UnsupportedEncodingException, JAXBException {
        // 初始化请求报文结构
        area.instance();

        String synusername = SystemConfig.GetConfigValue("ExternalService", "OSB.Appid");
        String synuserpw = SystemConfig.GetConfigValue("ExternalService", "OSB.Password");
        // 创建soap请求认证并获取远程数据
        return getSyncOrgInfo(area.initConnectInfo(parammap.get("requesturl"), synusername, synuserpw),
                tagName, clazz, increaseHeaders, increaseBody);
    }

    /**
     * 连接并获取远程同步数据(公共方法)
     *
     * @param area
     * @param TagName
     * @param clazz
     * @return
     * @throws SOAPException
     * @throws TransformerException
     * @throws UnsupportedEncodingException
     * @throws JAXBException
     */
    public List<RemoteUser> getSyncUserInfo(InterConnectArea area,
                                            String TagName,
                                            Class clazz,
                                            List<Label> increaseHeaders,
                                            List<Label> increaseBody) throws SOAPException,
            TransformerException, UnsupportedEncodingException, JAXBException {
        List<RemoteUser> remoteUser = new ArrayList<>();
        SOAPMessage requestSoap = soapFactory.createSoapMessage(area, increaseHeaders, increaseBody);
        SOAPBody soapBody = soapFactory.getResponesSoapBody(requestSoap, area.reqUrl());
        Document doc = soapBody.extractContentAsDocument();
        NodeList childNodes = doc.getElementsByTagName(area.getReqBody().getElePrefix() + ":" + TagName);
        StringWriter sw = null;
        for (int i = 0; i < childNodes.getLength(); i++) {
            try {
                Node item = childNodes.item(i);
                JAXBContext context = JAXBContext.newInstance(clazz);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                Transformer serializer = TransformerFactory.newInstance().newTransformer();
                sw = new StringWriter();
                serializer.transform(new DOMSource(item), new StreamResult(sw));
                String xml = sw.toString().replaceAll(area.getReqBody().getElePrefix() + ":", "");
                remoteUser.add((RemoteUser) unmarshaller.unmarshal(new StringReader(xml)));
            } finally {
                try {
                    if (sw != null) {
                        sw.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return remoteUser;
    }

    /**
     * 连接并获取远程同步数据(公共方法)
     *
     * @param area
     * @param TagName
     * @param clazz
     * @return
     * @throws SOAPException
     * @throws TransformerException
     * @throws UnsupportedEncodingException
     * @throws JAXBException
     */
    public List<RemoteOrg> getSyncOrgInfo(InterConnectArea area,
                                          String TagName,
                                          Class clazz,
                                          List<Label> increaseHeaders,
                                          List<Label> increaseBody) throws SOAPException,
            TransformerException, UnsupportedEncodingException, JAXBException {
        List<RemoteOrg> remoteOrgs = new ArrayList<>();
        SOAPMessage requestSoap = soapFactory.createSoapMessage(area, increaseHeaders, increaseBody);
        SOAPBody soapBody = soapFactory.getResponesSoapBody(requestSoap, area.reqUrl());
        Document doc = soapBody.extractContentAsDocument();
        NodeList childNodes = doc.getElementsByTagName(area.getReqBody().getElePrefix() + ":" + TagName);
        StringWriter sw = null;
        for (int i = 0; i < childNodes.getLength(); i++) {
            try {
                Node item = childNodes.item(i);
                JAXBContext context = JAXBContext.newInstance(clazz);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                Transformer serializer = TransformerFactory.newInstance().newTransformer();
                sw = new StringWriter();
                serializer.transform(new DOMSource(item), new StreamResult(sw));
                String xml = sw.toString().replaceAll(area.getReqBody().getElePrefix() + ":", "");
                remoteOrgs.add((RemoteOrg) unmarshaller.unmarshal(new StringReader(xml)));
            } finally {
                try {
                    if (sw != null) {
                        sw.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return remoteOrgs;
    }

    /**
     * 连接并获取远程同步数据(公共方法)
     *
     * @param area
     * @param TagName
     * @return Date
     * @throws SOAPException
     * @throws TransformerException
     * @throws UnsupportedEncodingException
     */
    public RemoteTime getSyncTime(InterConnectArea area,
                                  String TagName,
                                  Class clazz,
                                  List<Label> increaseHeaders,
                                  List<Label> increaseBody)
            throws SOAPException, TransformerException, UnsupportedEncodingException, JAXBException {
        SOAPMessage requestSoap = soapFactory.createSoapMessage(area, increaseHeaders, increaseBody);
        SOAPBody soapBody = soapFactory.getResponesSoapBody(requestSoap, area.reqUrl());
        Document doc = soapBody.extractContentAsDocument();
        NodeList childNodes = doc.getElementsByTagName(area.getReqBody().getElePrefix() + ":" + TagName);
        StringWriter sw = null;
        for (int i = 0; i < childNodes.getLength(); i++) {
            try {
                Node item = childNodes.item(i);
                JAXBContext context = JAXBContext.newInstance(clazz);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                Transformer serializer = TransformerFactory.newInstance().newTransformer();
                sw = new StringWriter();
                serializer.transform(new DOMSource(item), new StreamResult(sw));
                String xml = sw.toString().replaceAll(area.getReqBody().getElePrefix() + ":", "");
                return ((RemoteTime) unmarshaller.unmarshal(new StringReader(xml)));
            } finally {
                try {
                    if (sw != null) {
                        sw.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public String createTranstId() {
        int startantLength = 14;
        String dataTime = new SimpleDateFormat("yyyyMMddhhmmssSSS").format(new Date());
        if (dataTime.length() > startantLength) {
            dataTime = dataTime.substring(dataTime.length() - startantLength);
        } else if (dataTime.length() < startantLength) {
            for (int i = 0; i < startantLength - dataTime.length(); i++) {
                dataTime += 0;
            }
        }
        return (new Random().nextInt(88) + 11) + dataTime;
    }
}
