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
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 
package org.collectionspace.services.client.test;

import java.util.Arrays;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ServiceRequestType, identifies types of service requests
 * and the valid HTTP status codes that can be returned from
 * each type of request.
 *
 * Used by client tests of services.
 * 
 * $LastChangedRevision: 566 $
 * $LastChangedDate$
 */
public enum ServiceRequestType {
    
    // Define each of the service request types and their valid HTTP status codes.
    
    CREATE {
        @Override
        public int[] validStatusCodes() { 
              final int[] STATUS_CODES = {
                    Response.Status.CREATED.getStatusCode(),
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    Response.Status.FORBIDDEN.getStatusCode(),
                    Response.Status.CONFLICT.getStatusCode(),
                    Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()
              };
              Arrays.sort(STATUS_CODES);
              return STATUS_CODES;
        }
        @Override
        public boolean isValidStatusCode(int statusCode) {
              if (Arrays.binarySearch(CREATE.validStatusCodes(), statusCode) >= 0) {
                    return true;
              } else {
                    return false;
              }
        }
        @Override
        public String validStatusCodesAsString() {
              return Arrays.toString(CREATE.validStatusCodes());
        }
    },  // Note that commas are required at the end of each enum block, except the last.
    
    
    READ {
        @Override
        public int[] validStatusCodes() { 
              final int[] STATUS_CODES = {
                    Response.Status.OK.getStatusCode(),
                    Response.Status.FORBIDDEN.getStatusCode(),
                    Response.Status.NOT_FOUND.getStatusCode(),
                    Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()
              };
              Arrays.sort(STATUS_CODES);
              return STATUS_CODES;
        }
        @Override
        public boolean isValidStatusCode(int statusCode) {
              if (Arrays.binarySearch(READ.validStatusCodes(), statusCode) >= 0) {
                    return true;
              } else {
                    return false;
              }
        }
        @Override
        public String validStatusCodesAsString() {
              return Arrays.toString(READ.validStatusCodes());
        }
    },
    
    
    READ_MULTIPLE {
        @Override
        public int[] validStatusCodes() { 
              final int[] STATUS_CODES = {
                    Response.Status.OK.getStatusCode(),
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    Response.Status.FORBIDDEN.getStatusCode(),
                    Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()
              };
              Arrays.sort(STATUS_CODES);
              return STATUS_CODES;
        }
        @Override
        public boolean isValidStatusCode(int statusCode) {
              if (Arrays.binarySearch(READ_MULTIPLE.validStatusCodes(), statusCode) >= 0) {
                    return true;
              } else {
                    return false;
              }
        }
        @Override
        public String validStatusCodesAsString() {
              return Arrays.toString(READ_MULTIPLE.validStatusCodes());
        }
    },
    
    
    UPDATE {
        @Override
        public int[] validStatusCodes() { 
              final int[] STATUS_CODES = {
                    Response.Status.OK.getStatusCode(),
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    Response.Status.FORBIDDEN.getStatusCode(),
                    Response.Status.NOT_FOUND.getStatusCode(),
                    Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()
              };
              Arrays.sort(STATUS_CODES);
              return STATUS_CODES;
        }
        @Override
        public boolean isValidStatusCode(int statusCode) {
              if (Arrays.binarySearch(UPDATE.validStatusCodes(), statusCode) >= 0) {
                    return true;
              } else {
                    return false;
              }
        }
        @Override
        public String validStatusCodesAsString() {
              return Arrays.toString(UPDATE.validStatusCodes());
        }
    },
    
    
    DELETE {
        @Override
        public int[] validStatusCodes() { 
              final int[] STATUS_CODES = {
                    Response.Status.OK.getStatusCode(),
                    Response.Status.FORBIDDEN.getStatusCode(),
                    Response.Status.NOT_FOUND.getStatusCode(),
                    Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()
              };
              Arrays.sort(STATUS_CODES);
              return STATUS_CODES;
        }
        @Override
        public boolean isValidStatusCode(int statusCode) {
              if (Arrays.binarySearch(DELETE.validStatusCodes(), statusCode) >= 0) {
                    return true;
              } else {
                    return false;
              }
        }
        @Override
        public String validStatusCodesAsString() {
              return Arrays.toString(DELETE.validStatusCodes());
        }
    },
    
    
    // Used by guard code.
    NON_EXISTENT {
        @Override
        public int[] validStatusCodes() { 
              final int[] STATUS_CODES = { 0 };
              Arrays.sort(STATUS_CODES);
              return STATUS_CODES;
        }
        @Override
        public boolean isValidStatusCode(int statusCode) {
              return false;
        }
        @Override
        public String validStatusCodesAsString() {
              return Arrays.toString(NON_EXISTENT.validStatusCodes());
        }
    };
    
    // Template methods to be implemented by each ServiceRequestType.
    
    public abstract int[] validStatusCodes();
    
    public abstract boolean isValidStatusCode(int statusCode);
    
    public abstract String validStatusCodesAsString();

}