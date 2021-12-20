
package com.sheng.service;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.sheng.service package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _SelectPxbmIDSpidResponse_QNAME = new QName("http://service.sheng.com/", "SelectPxbmIDSpidResponse");
    private final static QName _UpdatePBJ_QNAME = new QName("http://service.sheng.com/", "updateP_BJ");
    private final static QName _UpdatePxbmResponse_QNAME = new QName("http://service.sheng.com/", "updatePxbmResponse");
    private final static QName _SelectPxbmIDSpid_QNAME = new QName("http://service.sheng.com/", "SelectPxbmIDSpid");
    private final static QName _SelectPxbmId_QNAME = new QName("http://service.sheng.com/", "selectPxbmId");
    private final static QName _UpdatePxbmopZtResponse_QNAME = new QName("http://service.sheng.com/", "updatePxbmop_ztResponse");
    private final static QName _SelectPxbmIdResponse_QNAME = new QName("http://service.sheng.com/", "selectPxbmIdResponse");
    private final static QName _UpdatePBJResponse_QNAME = new QName("http://service.sheng.com/", "updateP_BJResponse");
    private final static QName _UpdatePxbm_QNAME = new QName("http://service.sheng.com/", "updatePxbm");
    private final static QName _UpdatePxbmopZt_QNAME = new QName("http://service.sheng.com/", "updatePxbmop_zt");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.sheng.service
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SelectPxbmIdResponse }
     * 
     */
    public SelectPxbmIdResponse createSelectPxbmIdResponse() {
        return new SelectPxbmIdResponse();
    }

    /**
     * Create an instance of {@link UpdatePxbmopZtResponse }
     * 
     */
    public UpdatePxbmopZtResponse createUpdatePxbmopZtResponse() {
        return new UpdatePxbmopZtResponse();
    }

    /**
     * Create an instance of {@link UpdatePBJResponse }
     * 
     */
    public UpdatePBJResponse createUpdatePBJResponse() {
        return new UpdatePBJResponse();
    }

    /**
     * Create an instance of {@link UpdatePxbm }
     * 
     */
    public UpdatePxbm createUpdatePxbm() {
        return new UpdatePxbm();
    }

    /**
     * Create an instance of {@link UpdatePxbmopZt }
     * 
     */
    public UpdatePxbmopZt createUpdatePxbmopZt() {
        return new UpdatePxbmopZt();
    }

    /**
     * Create an instance of {@link UpdatePxbmResponse }
     * 
     */
    public UpdatePxbmResponse createUpdatePxbmResponse() {
        return new UpdatePxbmResponse();
    }

    /**
     * Create an instance of {@link SelectPxbmIDSpid }
     * 
     */
    public SelectPxbmIDSpid createSelectPxbmIDSpid() {
        return new SelectPxbmIDSpid();
    }

    /**
     * Create an instance of {@link SelectPxbmIDSpidResponse }
     * 
     */
    public SelectPxbmIDSpidResponse createSelectPxbmIDSpidResponse() {
        return new SelectPxbmIDSpidResponse();
    }

    /**
     * Create an instance of {@link UpdatePBJ }
     * 
     */
    public UpdatePBJ createUpdatePBJ() {
        return new UpdatePBJ();
    }

    /**
     * Create an instance of {@link SelectPxbmId }
     * 
     */
    public SelectPxbmId createSelectPxbmId() {
        return new SelectPxbmId();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SelectPxbmIDSpidResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sheng.com/", name = "SelectPxbmIDSpidResponse")
    public JAXBElement<SelectPxbmIDSpidResponse> createSelectPxbmIDSpidResponse(SelectPxbmIDSpidResponse value) {
        return new JAXBElement<SelectPxbmIDSpidResponse>(_SelectPxbmIDSpidResponse_QNAME, SelectPxbmIDSpidResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdatePBJ }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sheng.com/", name = "updateP_BJ")
    public JAXBElement<UpdatePBJ> createUpdatePBJ(UpdatePBJ value) {
        return new JAXBElement<UpdatePBJ>(_UpdatePBJ_QNAME, UpdatePBJ.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdatePxbmResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sheng.com/", name = "updatePxbmResponse")
    public JAXBElement<UpdatePxbmResponse> createUpdatePxbmResponse(UpdatePxbmResponse value) {
        return new JAXBElement<UpdatePxbmResponse>(_UpdatePxbmResponse_QNAME, UpdatePxbmResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SelectPxbmIDSpid }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sheng.com/", name = "SelectPxbmIDSpid")
    public JAXBElement<SelectPxbmIDSpid> createSelectPxbmIDSpid(SelectPxbmIDSpid value) {
        return new JAXBElement<SelectPxbmIDSpid>(_SelectPxbmIDSpid_QNAME, SelectPxbmIDSpid.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SelectPxbmId }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sheng.com/", name = "selectPxbmId")
    public JAXBElement<SelectPxbmId> createSelectPxbmId(SelectPxbmId value) {
        return new JAXBElement<SelectPxbmId>(_SelectPxbmId_QNAME, SelectPxbmId.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdatePxbmopZtResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sheng.com/", name = "updatePxbmop_ztResponse")
    public JAXBElement<UpdatePxbmopZtResponse> createUpdatePxbmopZtResponse(UpdatePxbmopZtResponse value) {
        return new JAXBElement<UpdatePxbmopZtResponse>(_UpdatePxbmopZtResponse_QNAME, UpdatePxbmopZtResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SelectPxbmIdResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sheng.com/", name = "selectPxbmIdResponse")
    public JAXBElement<SelectPxbmIdResponse> createSelectPxbmIdResponse(SelectPxbmIdResponse value) {
        return new JAXBElement<SelectPxbmIdResponse>(_SelectPxbmIdResponse_QNAME, SelectPxbmIdResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdatePBJResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sheng.com/", name = "updateP_BJResponse")
    public JAXBElement<UpdatePBJResponse> createUpdatePBJResponse(UpdatePBJResponse value) {
        return new JAXBElement<UpdatePBJResponse>(_UpdatePBJResponse_QNAME, UpdatePBJResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdatePxbm }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sheng.com/", name = "updatePxbm")
    public JAXBElement<UpdatePxbm> createUpdatePxbm(UpdatePxbm value) {
        return new JAXBElement<UpdatePxbm>(_UpdatePxbm_QNAME, UpdatePxbm.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdatePxbmopZt }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sheng.com/", name = "updatePxbmop_zt")
    public JAXBElement<UpdatePxbmopZt> createUpdatePxbmopZt(UpdatePxbmopZt value) {
        return new JAXBElement<UpdatePxbmopZt>(_UpdatePxbmopZt_QNAME, UpdatePxbmopZt.class, null, value);
    }

}
