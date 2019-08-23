package com.cdhr.platform.user.utils.soap.enums;

import com.cdhr.platform.user.utils.soap.init.Header;
import com.cdhr.platform.user.utils.soap.init.Part;
import com.cdhr.platform.user.utils.soap.init.ReqBody;

/**
 * @author heyong
 * @date 2019/8/9
 */
public enum InterConnectArea {

    /**
     * 河南请求报文处理
     */
    HENAN() {

        private Part part;
        private Header header;
        private ReqBody reqBody;
        private String reqUrl;
        private String authName;
        private String authPassword;

        @Override
        public void instance() {
            part = new Part();
            header = new Header();
            reqBody = new ReqBody();
            soapReqBody();
            soapHeader();
            soapEnvelope();
        }

        @Override
        public InterConnectArea initConnectInfo(String reqUrl, String authName, String authPassword) {
            this.reqUrl = reqUrl;
            this.authName = authName;
            this.authPassword = authPassword;
            return this;
        }

        @Override
        public Header getHeader() {
            return header;
        }

        @Override
        public Part getPart() {
            return part;
        }

        @Override
        public ReqBody getReqBody() {
            return reqBody;
        }

        @Override
        public String areaNameEn() {
            return "henan";
        }

        private void soapEnvelope() {
            part.setPrefix("soap");
            part.setNameSpace("SOAP-ENV");
            part.addAttribute("xmlns:srrc", "http://www.srrc.org.cn");
        }

        private void soapReqBody() {
            reqBody.setPrefix("soap");
            reqBody.setElePrefix("srrc");
            reqBody.setUrl("http://www.srrc.org.cn");
        }

        private void soapHeader() {
            header.setPrefix("soap");
            header.setElePrefix("srrc");
            header.setNameSpace("Envelope");
            header.setUrl("http://www.srrc.org.cn");
        }

        @Override
        public String reqUrl() {
            return reqUrl;
        }

        @Override
        public String authName() {
            return authName;
        }

        @Override
        public String authPassword() {
            return authPassword;
        }
    };

    public abstract String reqUrl();

    public abstract String authName();

    public abstract String authPassword();

    public abstract String areaNameEn();

    public abstract void instance();

    public abstract InterConnectArea initConnectInfo(String reqUrl, String authName, String authPassword);

    public abstract Header getHeader();

    public abstract Part getPart();

    public abstract ReqBody getReqBody();
}
