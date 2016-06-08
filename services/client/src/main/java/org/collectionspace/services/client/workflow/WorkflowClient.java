/**	
 * This document is a part of the source code and related artifacts
 * for CollectionSpace, an open source collections management system
 * for museums and related institutions:
 *
 * http://www.collectionspace.org
 * http://wiki.collectionspace.org
 *
 * Copyright (c) 2009 Regents of the University of California
 *
 * Licensed under the Educational Community License (ECL), Version 2.0.
 * You may not use this file except in compliance with this License.
 *
 * You may obtain a copy of the ECL 2.0 License at
 * https://source.collectionspace.org/collection-space/LICENSE.txt
 */
package org.collectionspace.services.client.workflow;

import java.util.HashMap;

import javax.ws.rs.core.Response;

import org.collectionspace.services.client.AbstractCommonListPoxServiceClientImpl;
import org.collectionspace.services.workflow.WorkflowCommon;

/**
 * WorkflowClient.java
 *
 * $LastChangedRevision: 2108 $
 * $LastChangedDate: 2010-05-17 18:25:37 -0700 (Mon, 17 May 2010) $
 *
 */
public class WorkflowClient extends AbstractCommonListPoxServiceClientImpl<WorkflowProxy, WorkflowCommon> {
	
	public static final String SERVICE_NAME = "workflow";
	public static final String SERVICE_PATH_COMPONENT = SERVICE_NAME;	
	public static final String SERVICE_PATH = "/" + SERVICE_PATH_COMPONENT;
	public static final String SERVICE_PAYLOAD_NAME = SERVICE_NAME;
	public static final String SERVICE_COMMONPART_NAME = SERVICE_NAME + PART_LABEL_SEPARATOR + PART_COMMON_LABEL;
	public static final String SERVICE_AUTHZ_SUFFIX = "/*/" + SERVICE_PATH_COMPONENT + "/";
	
	/*
	 * Nuxeu document workflow states
	 */
	
	public static final String WORKFLOWSTATE_XML_ELEMENT_NAME = COLLECTIONSPACE_CORE_WORKFLOWSTATE;
	// common to all Nuxeo document lifecycle policies
	public static final String WORKFLOWSTATE_PROJECT = "project";
	public static final String WORKFLOWSTATE_DELETED = "deleted";
	// part of the "cs_replicating" Nuxeo document lifecycle policy
	public static final String WORKFLOWSTATE_REPLICATED = "replicated";
	public static final String WORKFLOWSTATE_REPLICATED_DELETED = "replicated_deleted";
	// part of the "cs_locking" Nuxeo document lifecycle policy
	public static final String WORKFLOWSTATE_LOCKED = "locked";
	public static final String WORKFLOWSTATE_LOCKED_DELETED = "locked_deleted";
	
	/*
	 * Nuxeo document workflow transition verbs
	 */
	public static final String WORKFLOWTRANSITION_DELETE = "delete";	
	public static final String WORKFLOWTRANSITION_UNDELETE = "undelete";
	public static final String WORKFLOWTRANSITION_LOCK = "lock";
	public static final String WORKFLOWTRANSITION_UNLOCK = "unlock";
	public static final String WORKFLOWTRANSITION_REPLICATE = "replicate";
	public static final String WORKFLOWTRANSITION_UNREPLICATE = "unreplicate";
        
    public static final String WORKFLOWTRANSITION_TO = "to";
	//
	// DocumentHandler workflow-related passed in context properties
	//
	public static final String TRANSITION_ID = "transition_id";
	public static final String TRANSITION_PARAM_JAXRS = "transition";

	//
	// Service Query Params
	//
	public static final String WORKFLOW_QUERY_ONLY_DELETED = "wf_only_deleted";
	public static final String WORKFLOW_QUERY_NONDELETED = "wf_deleted";
	public static final String WORKFLOWSTATE_QUERY = "wf_deleted";
	public static final String TARGET_DOCHANDLER = "wf_dochandler";
	
	//
	// A mapping of state to the transition verbs that will get you there.  For more details,
	// see default-life-cycle-contrib.xml in the Nuxeo server configuration
	//
	private static final HashMap<String, String> statesMappedToTransitions;
    static {
    	statesMappedToTransitions = new HashMap<String, String>();
    	statesMappedToTransitions.put(WORKFLOWSTATE_DELETED, WORKFLOWTRANSITION_DELETE);
    	statesMappedToTransitions.put("c", "d");
    }
    
	public WorkflowClient() throws Exception {
		super();
	}
	
	@Override
	public String getServiceName() {
		return SERVICE_NAME;
	}
	
	@Override
    public String getServicePathComponent() {
        return SERVICE_PATH_COMPONENT;
    }

	@Override
	public Class<WorkflowProxy> getProxyClass() {
		return WorkflowProxy.class;
	}

	/*
	 * Proxied service calls
	 */
	
	@Override
	public Response readList() {
        throw new UnsupportedOperationException();
	}
	
    /* (non-Javadoc)
     * @see org.collectionspace.services.client.AbstractServiceClientImpl#delete(java.lang.String)
     */
    @Override
	public Response delete(String csid) {
        throw new UnsupportedOperationException();
    }
	
}
