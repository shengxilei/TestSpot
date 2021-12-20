
package com.sheng.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "PxbmService", targetNamespace = "http://service.sheng.com/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface PxbmService {


    /**
     * 
     * @param arg1
     * @param arg0
     * @return
     *     returns int
     */
    @WebMethod(operationName = "updateP_BJ")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "updateP_BJ", targetNamespace = "http://service.sheng.com/", className = "com.sheng.service.UpdatePBJ")
    @ResponseWrapper(localName = "updateP_BJResponse", targetNamespace = "http://service.sheng.com/", className = "com.sheng.service.UpdatePBJResponse")
    public int updatePBJ(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1);

    /**
     * 
     * @param arg0
     * @return
     *     returns int
     */
    @WebMethod(operationName = "SelectPxbmIDSpid")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "SelectPxbmIDSpid", targetNamespace = "http://service.sheng.com/", className = "com.sheng.service.SelectPxbmIDSpid")
    @ResponseWrapper(localName = "SelectPxbmIDSpidResponse", targetNamespace = "http://service.sheng.com/", className = "com.sheng.service.SelectPxbmIDSpidResponse")
    public int selectPxbmIDSpid(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0);

    /**
     * 
     * @param arg3
     * @param arg2
     * @param arg4
     * @param arg1
     * @param arg0
     * @return
     *     returns int
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "updatePxbm", targetNamespace = "http://service.sheng.com/", className = "com.sheng.service.UpdatePxbm")
    @ResponseWrapper(localName = "updatePxbmResponse", targetNamespace = "http://service.sheng.com/", className = "com.sheng.service.UpdatePxbmResponse")
    public int updatePxbm(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        String arg2,
        @WebParam(name = "arg3", targetNamespace = "")
        String arg3,
        @WebParam(name = "arg4", targetNamespace = "")
        String arg4);

    /**
     * 
     * @param arg1
     * @param arg0
     * @return
     *     returns int
     */
    @WebMethod(operationName = "updatePxbmop_zt")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "updatePxbmop_zt", targetNamespace = "http://service.sheng.com/", className = "com.sheng.service.UpdatePxbmopZt")
    @ResponseWrapper(localName = "updatePxbmop_ztResponse", targetNamespace = "http://service.sheng.com/", className = "com.sheng.service.UpdatePxbmopZtResponse")
    public int updatePxbmopZt(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1);

    /**
     * 
     * @param arg0
     * @return
     *     returns int
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "selectPxbmId", targetNamespace = "http://service.sheng.com/", className = "com.sheng.service.SelectPxbmId")
    @ResponseWrapper(localName = "selectPxbmIdResponse", targetNamespace = "http://service.sheng.com/", className = "com.sheng.service.SelectPxbmIdResponse")
    public int selectPxbmId(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0);

}