/**
 * This document is a part of the source code and related artifacts
 * for CollectionSpace, an open source collections management system
 * for museums and related institutions:
 *
 * http://www.collectionspace.org
 * http://wiki.collectionspace.org
 *
 * Copyright © 2009 Regents of the University of California
 *
 * Licensed under the Educational Community License (ECL), Version 2.0.
 * You may not use this file except in compliance with this License.
 *
 * You may obtain a copy of the ECL 2.0 License at
 * https://source.collectionspace.org/collection-space/LICENSE.txt
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.collectionspace.services.client.test;

import java.io.IOException;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.methods.OptionsMethod;
import org.apache.commons.httpclient.methods.TraceMethod;
import org.collectionspace.services.client.TestServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * ServiceLayerTest, tests high-level functionality of
 * the Services layer.
 *
 * $LastChangedRevision: 566 $
 * $LastChangedDate: 2009-08-26 15:24:17 -0800 (Wed, 26 Aug 2009) $
 */
public class ServiceLayerTest {

    final Logger logger = LoggerFactory.getLogger(ServiceTest.class);
    private HttpClient httpClient = new HttpClient();
    private TestServiceClient serviceClient = new TestServiceClient();

    @Test
    public void servicesExist() {
        //use ID service that should always be present in a working service layer
        String url = serviceClient.getBaseURL() + "idgenerators";
        OptionsMethod method = new OptionsMethod(url);
        try{
            int statusCode = httpClient.executeMethod(method);
            if(logger.isDebugEnabled()){
                logger.debug("servicesExist url=" + url + " status=" + statusCode);
            }
            Assert.assertEquals(statusCode, HttpStatus.SC_OK,
                    "expected " + HttpStatus.SC_OK);
        }catch(HttpException e){
            logger.error("Fatal protocol violation: ", e);
        }catch(IOException e){
            logger.error("Fatal transport error", e);
        }catch(Exception e){
            logger.error("unknown exception ", e);
        }finally{
            // Release the connection.
            method.releaseConnection();
        }
    }

    @Test
    public void methodNotAllowed() {
        //delete is not allowed on the root URL of the id service
        String url = serviceClient.getBaseURL() + "idgenerators";
        DeleteMethod method = new DeleteMethod(url);
        try{
            int statusCode = httpClient.executeMethod(method);
            if(logger.isDebugEnabled()){
                logger.debug(" methodNotAllowed url=" + url + " status=" + statusCode);
            }
            Assert.assertEquals(statusCode, HttpStatus.SC_METHOD_NOT_ALLOWED,
                    "expected " + HttpStatus.SC_METHOD_NOT_ALLOWED);
        }catch(HttpException e){
            logger.error("Fatal protocol violation: ", e);
        }catch(IOException e){
            logger.error("Fatal transport error", e);
        }catch(Exception e){
            logger.error("unknown exception ", e);
        }finally{
            // Release the connection.
            method.releaseConnection();
        }
    }

    @Test
    public void noService() {

        String url = serviceClient.getBaseURL() + "fake-service";
        GetMethod method = new GetMethod(url);
        try{
            int statusCode = httpClient.executeMethod(method);
            if(logger.isDebugEnabled()){
                logger.debug("noService url=" + url + " status=" + statusCode);
            }
            Assert.assertEquals(statusCode, HttpStatus.SC_NOT_FOUND,
                    "expected " + HttpStatus.SC_NOT_FOUND);
        }catch(HttpException e){
            logger.error("Fatal protocol violation: ", e);
        }catch(IOException e){
            logger.error("Fatal transport error", e);
        }catch(Exception e){
            logger.error("unknown exception ", e);
        }finally{
            // Release the connection.
            method.releaseConnection();
        }
    }

    @Test
    public void serviceSecure() {
        if(!serviceClient.isServerSecure()){
            logger.warn("set -Dcspace.server.secure=true to run security tests");
            return;
        }
        String url = serviceClient.getBaseURL() + "collectionobjects";
        GetMethod method = new GetMethod(url);
        try{
            int statusCode = httpClient.executeMethod(method);
            if(logger.isDebugEnabled()){
                logger.debug("serviceSecure url=" + url + " status=" + statusCode);
            }
            Assert.assertEquals(statusCode, HttpStatus.SC_UNAUTHORIZED,
                    "expected " + HttpStatus.SC_UNAUTHORIZED);
        }catch(HttpException e){
            logger.error("Fatal protocol violation: ", e);
        }catch(IOException e){
            logger.error("Fatal transport error", e);
        }catch(Exception e){
            logger.error("unknown exception ", e);
        }finally{
            // Release the connection.
            method.releaseConnection();
        }
    }

    @Test
    public void traceSupported() {
        String url = serviceClient.getBaseURL() + "collectionobjects";
        TraceMethod method = new TraceMethod(url);
        try{
            int statusCode = httpClient.executeMethod(method);

            if(logger.isDebugEnabled()){
                logger.debug("traceSupported url=" + url + " status=" + statusCode);
                logger.debug("traceSupported response=" + new String(method.getResponseBody()));
                for(Header h : method.getResponseHeaders()){
                    logger.debug("traceSupported header name=" + h.getName() + " value=" + h.getValue());
                }
            }
            Assert.assertEquals(statusCode, HttpStatus.SC_METHOD_NOT_ALLOWED,
                    "expected " + HttpStatus.SC_METHOD_NOT_ALLOWED);
        }catch(HttpException e){
            logger.error("Fatal protocol violation: ", e);
        }catch(IOException e){
            logger.error("Fatal transport error", e);
        }catch(Exception e){
            logger.error("unknown exception ", e);
        }finally{
            // Release the connection.
            method.releaseConnection();
        }
    }

    @Test
    public void headSupported() {
        String url = serviceClient.getBaseURL() + "intakes";
        HeadMethod method = new HeadMethod(url);
        try{
            int statusCode = httpClient.executeMethod(method);
            Assert.assertEquals(method.getResponseBody(), null, "expected null");
            if(logger.isDebugEnabled()){
                logger.debug("headSupported url=" + url + " status=" + statusCode);
                for(Header h : method.getResponseHeaders()){
                    logger.debug("headSupported header name=" + h.getName() + " value=" + h.getValue());
                }
            }
            Assert.assertEquals(statusCode, HttpStatus.SC_OK,
                    "expected " + HttpStatus.SC_OK);
        }catch(HttpException e){
            logger.error("Fatal protocol violation: ", e);
        }catch(IOException e){
            logger.error("Fatal transport error", e);
        }catch(Exception e){
            logger.error("unknown exception ", e);
        }finally{
            // Release the connection.
            method.releaseConnection();
        }
    }
}