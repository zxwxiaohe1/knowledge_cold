package com.cdhr.platform.user.utils.soap;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import sun.misc.BASE64Encoder;
import com.cdhr.platform.user.utils.soap.enums.InterConnectArea;
import com.cdhr.platform.user.utils.soap.init.Header;
import com.cdhr.platform.user.utils.soap.init.ReqBody;
import com.cdhr.platform.user.utils.soap.vo.Label;

import javax.xml.soap.SOAPMessage;
import javax.xml.soap.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * @author heyong
 * @date 2019/8/9
 */
@Component
public class SoapFactory {

    private static final Logger logger = LoggerFactory.getLogger(SoapFactory.class);
    private SOAPMessage soapMessage;
    private SOAPEnvelope soapEnvelope;

    public SoapFactory() {
    }

    /**
     * 创建soap请求信息,标签URL跟随信封地址
     *
     * @param area
     * @param increaseHeaders
     * @param increaseBody
     * @return
     * @throws SOAPException
     */
    public SOAPMessage createSoapMessage(InterConnectArea area, List<Label> increaseHeaders, List<Label> increaseBody) throws SOAPException {
        this.soapMessage = MessageFactory.newInstance().createMessage();
        this.soapEnvelope = soapMessage.getSOAPPart().getEnvelope();
        createPart(area);
        createHeader(soapEnvelope.getHeader(), area, increaseHeaders);
        createBody(soapEnvelope.getBody(), area, increaseBody);
        return soapMessage;
    }

    /**
     * 创建soap请求信息,可自定义标签URL
     *
     * @param area
     * @param increaseHeaders
     * @param increaseBody
     * @return
     * @throws SOAPException
     */
    public SOAPMessage createUrlSoapMessage(InterConnectArea area, List<Label> increaseHeaders, List<Label> increaseBody) throws SOAPException {
        this.soapMessage = MessageFactory.newInstance().createMessage();
        this.soapEnvelope = soapMessage.getSOAPPart().getEnvelope();
        createPart(area);
        createHeader(increaseHeaders, soapEnvelope.getHeader(), area);
        createBody(increaseBody, soapEnvelope.getBody(), area);
        return soapMessage;
    }

    /**
     * 获得soap响应报文
     *
     * @param requestSoap
     * @param url
     * @return
     * @throws SOAPException
     * @throws TransformerException
     * @throws UnsupportedEncodingException
     */
    public SOAPBody getResponesSoapBody(SOAPMessage requestSoap, String url) throws SOAPException,
            TransformerException, UnsupportedEncodingException {
        try {
            requestSoap.writeTo(System.out);
        } catch (IOException e) {
            logger.error("failed to write reqest soap text!", e);
        }
        //实例化一个soap连接对象工厂
        SOAPConnectionFactory soapConnFactory = SOAPConnectionFactory.newInstance();
        //实例化一个连接对象
        SOAPConnection connection = null;
        SOAPMessage reply = null;
        try {
            logger.info("soap request url: " + url);
            connection = soapConnFactory.createConnection();
            reply = connection.call(requestSoap, url);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        if (reply != null) {
            //这部分的处理就是将返回的值转换为字符串的格式，也就是流和串之间的转换
            Source source = reply.getSOAPPart().getContent();
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            ByteArrayOutputStream myOutStr = new ByteArrayOutputStream();
            StreamResult res = new StreamResult();
            res.setOutputStream(myOutStr);
            transformer.transform(source, res);

            //针对于汉子的编码格式，需要自己制定
            String temp = myOutStr.toString("UTF-8");
            logger.debug("Soap Response Xml:" + temp);
            SOAPMessage rspMessage = formartSoapString(temp);
            SOAPBody rspbody = rspMessage.getSOAPBody();
            SOAPFault f = rspbody.getFault();
            if (f != null) {
                String exception = f.getFaultString();
                if (exception != null && !exception.isEmpty()) {
                    logger.error("Soap Response Error:" + exception);
                }
            }
            return rspbody;
        }
        return null;
    }

    /**
     * 添加soap请求头元素
     *
     * @param headEles
     * @param soapFactory
     * @param soapHeaderElm
     * @param header
     * @throws SOAPException
     */
    private void addHeaderEle(Map<String, Object> headEles, SOAPFactory soapFactory, SOAPElement soapHeaderElm, Header header)
            throws SOAPException {
        for (String key : headEles.keySet()) {
            SOAPElement ele = soapFactory.createElement(key, header.getElePrefix(), header.getUrl());
            if (headEles.get(key) instanceof Map) {
                addHeaderEle((Map<String, Object>) headEles.get(key), soapFactory, ele, header);
            } else {
                ele.addTextNode(String.valueOf(headEles.get(key)));
            }
            soapHeaderElm.addChildElement(ele);
        }
    }

    /**
     * 添加请求报文体
     *
     * @param headEles
     * @param soapFactory
     * @param soapHeaderElm
     * @param reqBody
     * @throws SOAPException
     */
    private void addBodyEle(Map<String, Object> headEles, SOAPFactory soapFactory, SOAPElement soapHeaderElm, ReqBody reqBody)
            throws SOAPException {
        for (String key : headEles.keySet()) {
            SOAPElement ele = soapFactory.createElement(key, reqBody.getElePrefix(), reqBody.getUrl());
            if (headEles.get(key) instanceof Map) {
                addBodyEle((Map<String, Object>) headEles.get(key), soapFactory, ele, reqBody);
            } else {
                ele.addTextNode(String.valueOf(headEles.get(key)));
            }
            soapHeaderElm.addChildElement(ele);
        }
    }


    /**
     * 格式化报文信息
     *
     * @param soapString
     * @return
     */
    public SOAPMessage formartSoapString(String soapString) {
        MessageFactory msgFactory;
        try {
            msgFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
            SOAPMessage reqMsg =
                    msgFactory.createMessage(new MimeHeaders(),
                            new ByteArrayInputStream(soapString.getBytes(Charset.forName("UTF-8"))));
            reqMsg.saveChanges();
            return reqMsg;
        } catch (SOAPException e) {
            logger.error("faild to initialize MessageFactory:", e);
        } catch (IOException e) {
            logger.error("an error occurs when create SOAPMessage :", e);
        }
        return null;
    }

    /**
     * 创建soap信封
     *
     * @param area
     */
    private void createPart(InterConnectArea area) {
        String auth = area.authName() + ":" + area.authPassword();
        if(area.authPassword().contains(":")){
            auth = area.authPassword();
        }
        //创建请求头信息，包括连接用户和密码
        String authorization = new BASE64Encoder().encode(auth.getBytes());
        logger.error("auth info!", area.authName() + ":" + area.authPassword());
        soapMessage.getMimeHeaders().addHeader("Authorization", "Basic " + authorization);
        //创建信封
        soapEnvelope.setPrefix(area.getPart().getPrefix());
        soapEnvelope.removeNamespaceDeclaration(area.getPart().getNameSpace());
        for (String key : area.getPart().getAttributes().keySet()) {
            soapEnvelope.setAttribute(key, area.getPart().getAttributes().get(key));
        }
    }

    /**
     * 创建报文头结构
     *
     * @param soapHeader
     * @param area
     * @param increaseHeader
     * @throws SOAPException
     */
    private void createHeader(SOAPHeader soapHeader, InterConnectArea area, List<Label> increaseHeader) throws SOAPException {
        if (!ObjectUtils.isEmpty(increaseHeader)) {
            for (Label label : increaseHeader) {
                area.getHeader().addAttribute(label);
            }
        }
        //创建header
        soapHeader.setPrefix(area.getHeader().getPrefix());
        Map<String, Object> headEles = area.getHeader().getAttributes();
        SOAPFactory soapFactory = SOAPFactory.newInstance();
        for (String key : headEles.keySet()) {
            SOAPElement soapHeaderElm = soapFactory.createElement(key, area.getHeader().getElePrefix(), area.getHeader().getUrl());
            if (headEles.get(key) instanceof Map) {
                try {
                    addHeaderEle((Map<String, Object>) headEles.get(key), soapFactory, soapHeaderElm, area.getHeader());
                } catch (SOAPException e) {
                    e.printStackTrace();
                }
            }
            soapHeader.addChildElement(soapHeaderElm);
            logger.info("header info " + soapHeader.getTextContent());
        }
    }

    /**
     * 创建soap请求体结构
     *
     * @param soapBody
     * @param area
     * @param increaseBody
     * @throws SOAPException
     */
    private void createBody(SOAPBody soapBody, InterConnectArea area, List<Label> increaseBody) throws SOAPException {

        if (!ObjectUtils.isEmpty(increaseBody)) {
            for (Label label : increaseBody) {
                area.getReqBody().addAttribute(label);
            }
        }
        SOAPFactory soapFactory = SOAPFactory.newInstance();
        // 创建body
        Map<String, Object> bodyEles = area.getReqBody().getAttributes();
        soapBody.setPrefix(area.getReqBody().getPrefix());
        for (String key : bodyEles.keySet()) {
            SOAPElement soapBodyElm = soapFactory.createElement(key, area.getReqBody().getElePrefix(), area.getReqBody().getUrl());
            if (bodyEles.get(key) instanceof Map) {
                addBodyEle((Map<String, Object>) bodyEles.get(key), soapFactory, soapBodyElm, area.getReqBody());
            }
            soapBody.addChildElement(soapBodyElm);
        }
    }

    /**
     * 创建报文头结构
     *
     * @param soapHeader
     * @param area
     * @param labs
     * @throws SOAPException
     */
    private void createHeader(List<Label> labs, SOAPHeader soapHeader, InterConnectArea area) throws SOAPException {
        //创建header
        soapHeader.setPrefix(area.getHeader().getPrefix());
        SOAPFactory soapFactory = SOAPFactory.newInstance();
        if (!ObjectUtils.isEmpty(labs)) {
            for (Label lab : labs) {
                SOAPElement soapHeaderElm = soapFactory.createElement(lab.getName(), area.getHeader().getElePrefix(),
                        StringUtils.isNotBlank(lab.getNameSpace()) ? lab.getNameSpace() : area.getHeader().getUrl());
                if (!ObjectUtils.isEmpty(lab.getLabs())) {
                    try {
                        addHeaderEle(lab.getLabs(), soapFactory, soapHeaderElm, area.getHeader());
                    } catch (SOAPException e) {
                        e.printStackTrace();
                    }
                }
                soapHeader.addChildElement(soapHeaderElm);
            }
            logger.info("header info " + soapHeader.getTextContent());
        }
    }

    /**
     * 创建soap请求体结构
     *
     * @param soapBody
     * @param area
     * @param labs
     * @throws SOAPException
     */
    private void createBody(List<Label> labs, SOAPBody soapBody, InterConnectArea area) throws SOAPException {

        // 创建body
        soapBody.setPrefix(area.getReqBody().getPrefix());
        SOAPFactory soapFactory = SOAPFactory.newInstance();
        for (Label lab : labs) {
            if (!ObjectUtils.isEmpty(lab)) {
                SOAPElement soapBodyElm = soapFactory.createElement(lab.getName(), area.getReqBody().getElePrefix(),
                        StringUtils.isNotBlank(lab.getNameSpace()) ? lab.getNameSpace() : area.getReqBody().getUrl());
                if (!ObjectUtils.isEmpty(lab.getLabs())) {
                    addBodyEle(lab.getLabs(), soapFactory, soapBodyElm, area.getReqBody());
                }
                soapBody.addChildElement(soapBodyElm);
            }
        }
    }

    /**
     * 添加soap请求头元素
     *
     * @param labs          List<Label>
     * @param soapFactory
     * @param soapHeaderElm
     * @param header
     * @throws SOAPException
     */
    private void addHeaderEle(List<Label> labs, SOAPFactory soapFactory, SOAPElement soapHeaderElm, Header header)
            throws SOAPException {
        if (!ObjectUtils.isEmpty(labs)) {
            for (Label lab : labs) {
                SOAPElement ele = soapFactory.createElement(lab.getName(), header.getElePrefix(),
                        StringUtils.isNotBlank(lab.getNameSpace()) ? lab.getNameSpace() : header.getUrl());
                if (!ObjectUtils.isEmpty(lab.getLabs())) {
                    addHeaderEle(lab.getLabs(), soapFactory, ele, header);
                } else {
                    if (StringUtils.isNotBlank(lab.getValue())) {
                        ele.addTextNode(lab.getValue());
                    }
                }
                soapHeaderElm.addChildElement(ele);
            }
        }
    }

    /**
     * 添加请求报文体
     *
     * @param labs          List<Label>
     * @param soapFactory
     * @param soapHeaderElm
     * @param reqBody
     * @throws SOAPException
     */
    private void addBodyEle(List<Label> labs, SOAPFactory soapFactory, SOAPElement soapHeaderElm, ReqBody reqBody)
            throws SOAPException {
        if (!ObjectUtils.isEmpty(labs)) {
            for (Label lab : labs) {
                SOAPElement ele = soapFactory.createElement(lab.getName(), reqBody.getElePrefix(),
                        StringUtils.isNotBlank(lab.getNameSpace()) ? lab.getNameSpace() : reqBody.getUrl());
                if (!ObjectUtils.isEmpty(lab.getLabs())) {
                    addBodyEle(lab.getLabs(), soapFactory, ele, reqBody);
                } else {
                    if (StringUtils.isNotBlank(lab.getValue())) {
                        ele.addTextNode(lab.getValue());
                    }
                }
                soapHeaderElm.addChildElement(ele);
            }
        }
    }
}
