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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.collectionspace.services.PersonJAXBSchema;
import org.collectionspace.services.client.CollectionSpaceClient;
import org.collectionspace.services.client.ConservationClient;
import org.collectionspace.services.client.PersonAuthorityClient;
import org.collectionspace.services.client.PersonAuthorityClientUtils;
import org.collectionspace.services.client.PayloadOutputPart;
import org.collectionspace.services.client.PoxPayloadIn;
import org.collectionspace.services.client.PoxPayloadOut;
import org.collectionspace.services.common.authorityref.AuthorityRefList;
import org.collectionspace.services.jaxb.AbstractCommonList;
import org.collectionspace.services.conservation.ConservationCommon;
import org.collectionspace.services.conservation.ConservatorsList;
import org.collectionspace.services.conservation.DestAnalysisGroup;
import org.collectionspace.services.conservation.DestAnalysisGroupList;
import org.collectionspace.services.conservation.ExaminationGroup;
import org.collectionspace.services.conservation.ExaminationGroupList;
import org.collectionspace.services.conservation.OtherPartyGroup;
import org.collectionspace.services.conservation.OtherPartyGroupList;
import org.collectionspace.services.person.PersonTermGroup;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ConservationAuthRefsTest, carries out Authority References tests against a
 * deployed and running Conservation Service.
 *
 * $LastChangedRevision$
 * $LastChangedDate$
 */
public class ConservationAuthRefsTest extends BaseServiceTest<AbstractCommonList> {

    private final String CLASS_NAME = ConservationAuthRefsTest.class.getName();
    private final Logger logger = LoggerFactory.getLogger(CLASS_NAME);
    
    // Instance variables specific to this test.
    final String SERVICE_NAME = "conservation";
    final String SERVICE_PATH_COMPONENT = "conservation";
    final String PERSON_AUTHORITY_NAME = "TestPersonAuthForConservation";
    private String knownResourceId = null;
    private List<String> conservationIdsCreated = new ArrayList<String>();
    private List<String> personIdsCreated = new ArrayList<String>();
    private String personAuthCSID = null;
    private String conservatorRefName = null;
    private String otherPartyRefName = null;
    private String examinationStaffRefName = null;
    private String approvedByRefName = null;
    private String researcherRefName = null;
    private String sampleByRefName = null;
    private final int NUM_AUTH_REFS_EXPECTED = 6;

    /* (non-Javadoc)
     * @see org.collectionspace.services.client.test.BaseServiceTest#getClientInstance()
     */
    @Override
    protected CollectionSpaceClient getClientInstance() {
    	throw new UnsupportedOperationException(); //method not supported (or needed) in this test class
    }

	@Override
	protected CollectionSpaceClient getClientInstance(String clientPropertiesFilename) {
    	throw new UnsupportedOperationException(); //method not supported (or needed) in this test class
	}
	
    /* (non-Javadoc)
     * @see org.collectionspace.services.client.test.BaseServiceTest#getAbstractCommonList(org.jboss.resteasy.client.ClientResponse)
     */
    @Override
	protected AbstractCommonList getCommonList(Response response) {
    	throw new UnsupportedOperationException(); //method not supported (or needed) in this test class
    }

    // ---------------------------------------------------------------
    // CRUD tests : CREATE tests
    // ---------------------------------------------------------------
    // Success outcomes
    @Test(dataProvider="testName", dataProviderClass=AbstractServiceTestImpl.class)
    public void createWithAuthRefs(String testName) throws Exception {
        testSetup(STATUS_CREATED, ServiceRequestType.CREATE);

        // Submit the request to the service and store the response.
        String identifier = createIdentifier();
        
        // Create all the person refs and entities
        createPersonRefs();

        // Create a new Conservation resource.
        //
        // One or more fields in this resource will be PersonAuthority
        // references, and will refer to Person resources by their refNames.
        ConservationClient conservationClient = new ConservationClient();
        PoxPayloadOut multipart = createConservationInstance(
                "conservationNumber-" + identifier,
                conservatorRefName,
                otherPartyRefName,
                examinationStaffRefName,
                approvedByRefName,
                researcherRefName,
                sampleByRefName);
        Response response = conservationClient.create(multipart);
        int statusCode = response.getStatus();
        try {
            // Check the status code of the response: does it match
            // the expected response(s)?
            //
            // Specifically:
            // Does it fall within the set of valid status codes?
            // Does it exactly match the expected status code?
            if(logger.isDebugEnabled()){
                logger.debug(testName + ": status = " + statusCode);
            }
            Assert.assertTrue(testRequestType.isValidStatusCode(statusCode),
                    invalidStatusCodeMessage(testRequestType, statusCode));
            Assert.assertEquals(statusCode, testExpectedStatusCode);

            // Store the ID returned from the first resource created
            // for additional tests below.
            if (knownResourceId == null){
                knownResourceId = extractId(response);
                if (logger.isDebugEnabled()) {
                    logger.debug(testName + ": knownResourceId=" + knownResourceId);
                }
            }
        
            // Store the IDs from every resource created by tests,
            // so they can be deleted after tests have been run.
            conservationIdsCreated.add(extractId(response));
        } finally {
        	response.close();
        }
    }
    
    protected void createPersonRefs() throws Exception{
        PersonAuthorityClient personAuthClient = new PersonAuthorityClient();
        // Create a temporary PersonAuthority resource, and its corresponding
        // refName by which it can be identified.
    	PoxPayloadOut multipart = PersonAuthorityClientUtils.createPersonAuthorityInstance(
    	    PERSON_AUTHORITY_NAME, PERSON_AUTHORITY_NAME, personAuthClient.getCommonPartName());
        Response res = personAuthClient.create(multipart);
        try {
               int statusCode = res.getStatus();
               Assert.assertTrue(testRequestType.isValidStatusCode(statusCode),
                   invalidStatusCodeMessage(testRequestType, statusCode));
               Assert.assertEquals(statusCode, STATUS_CREATED);
               personAuthCSID = extractId(res);
        } finally {
               res.close();
        }

        String authRefName = PersonAuthorityClientUtils.getAuthorityRefName(personAuthCSID, null);
        // Create temporary Person resources, and their corresponding refNames
        // by which they can be identified.
       	String csid = createPerson("Connie", "Conservator", "connieConservator", authRefName);
        personIdsCreated.add(csid);
        conservatorRefName = PersonAuthorityClientUtils.getPersonRefName(personAuthCSID, csid, null);

       	csid = createPerson("Oliver", "Otherparty", "oliverOtherparty", authRefName);
        personIdsCreated.add(csid);
        otherPartyRefName = PersonAuthorityClientUtils.getPersonRefName(personAuthCSID, csid, null);

        csid = createPerson("Steven", "Staff", "stevenStaff", authRefName);
        personIdsCreated.add(csid);
        examinationStaffRefName = PersonAuthorityClientUtils.getPersonRefName(personAuthCSID, csid, null);
        
        csid = createPerson("Allison", "Approver", "allisonApprover", authRefName);
        personIdsCreated.add(csid);
        approvedByRefName = PersonAuthorityClientUtils.getPersonRefName(personAuthCSID, csid, null);

        csid = createPerson("Rachel", "Researcher", "rachelResearcher", authRefName);
        personIdsCreated.add(csid);
        researcherRefName = PersonAuthorityClientUtils.getPersonRefName(personAuthCSID, csid, null);

        csid = createPerson("Sabrina", "Sampler", "sabrinaSampler", authRefName);
        personIdsCreated.add(csid);
        sampleByRefName = PersonAuthorityClientUtils.getPersonRefName(personAuthCSID, csid, null);
    }
    
    protected String createPerson(String firstName, String surName, String shortId, String authRefName ) throws Exception {
        String result = null;
       
        PersonAuthorityClient personAuthClient = new PersonAuthorityClient();
        Map<String, String> personInfo = new HashMap<String,String>();
        personInfo.put(PersonJAXBSchema.FORE_NAME, firstName);
        personInfo.put(PersonJAXBSchema.SUR_NAME, surName);
        personInfo.put(PersonJAXBSchema.SHORT_IDENTIFIER, shortId);
        List<PersonTermGroup> personTerms = new ArrayList<PersonTermGroup>();
        PersonTermGroup term = new PersonTermGroup();
        String termName = firstName + " " + surName;
        term.setTermDisplayName(termName);
        term.setTermName(termName);
        personTerms.add(term);
        PoxPayloadOut multipart = PersonAuthorityClientUtils.createPersonInstance(personAuthCSID, 
    				authRefName, personInfo, personTerms, personAuthClient.getItemCommonPartName());
      
        Response res = personAuthClient.createItem(personAuthCSID, multipart);
        try {
            int statusCode = res.getStatus();
            Assert.assertTrue(testRequestType.isValidStatusCode(statusCode),
                invalidStatusCodeMessage(testRequestType, statusCode));
            Assert.assertEquals(statusCode, STATUS_CREATED);
            result = extractId(res);
        } finally {
            res.close();
        }

        return result;
    }

    // Success outcomes
    @Test(dataProvider="testName", dataProviderClass=AbstractServiceTestImpl.class,
        dependsOnMethods = {"createWithAuthRefs"})
    public void readAndCheckAuthRefs(String testName) throws Exception {
        // Perform setup.
        testSetup(STATUS_OK, ServiceRequestType.READ);

        // Submit the request to the service and store the response.
        ConservationClient conservationClient = new ConservationClient();
        Response res = conservationClient.read(knownResourceId);
        ConservationCommon conservationCommon = null;
        try {
	        assertStatusCode(res, testName);
	        // Extract the common part from the response.
	        PoxPayloadIn input = new PoxPayloadIn(res.readEntity(String.class));
	        conservationCommon = (ConservationCommon) extractPart(input,
	            conservationClient.getCommonPartName(), ConservationCommon.class);
	        Assert.assertNotNull(conservationCommon);
	        if(logger.isDebugEnabled()){
	            logger.debug(objectAsXmlString(conservationCommon, ConservationCommon.class));
	        }
        } finally {
        	if (res != null) {
                res.close();
            }
        }
        //
        // Check a couple of fields
        //
        Assert.assertEquals(conservationCommon.getApprovedBy(), approvedByRefName);
        Assert.assertEquals(conservationCommon.getResearcher(), researcherRefName);
        
        // Get the auth refs and check them
        Response res2 = conservationClient.getAuthorityRefs(knownResourceId);
        AuthorityRefList list = null;
        try {
	        assertStatusCode(res2, testName);
	        list = res2.readEntity(AuthorityRefList.class);
	        Assert.assertNotNull(list);
        } finally {
        	if (res2 != null) {
        		res2.close();
            }
        }
        
        List<AuthorityRefList.AuthorityRefItem> items = list.getAuthorityRefItem();
        int numAuthRefsFound = items.size();
        if(logger.isDebugEnabled()){
            logger.debug("Expected " + NUM_AUTH_REFS_EXPECTED +
                " authority references, found " + numAuthRefsFound);
        }

        // Optionally output additional data about list members for debugging.
        boolean iterateThroughList = true;
        if(iterateThroughList && logger.isDebugEnabled()){
            int i = 0;
            for(AuthorityRefList.AuthorityRefItem item : items){
                logger.debug(testName + ": list-item[" + i + "] Field:" +
                		item.getSourceField() + "= " +
                        item.getAuthDisplayName() +
                        item.getItemDisplayName());
                logger.debug(testName + ": list-item[" + i + "] refName=" +
                        item.getRefName());
                logger.debug(testName + ": list-item[" + i + "] URI=" +
                        item.getUri());
                i++;
            }
        }

        Assert.assertEquals(numAuthRefsFound, NUM_AUTH_REFS_EXPECTED,
            "Did not find all expected authority references! " +
            "Expected " + NUM_AUTH_REFS_EXPECTED + ", found " + numAuthRefsFound);

    }


    // ---------------------------------------------------------------
    // Cleanup of resources created during testing
    // ---------------------------------------------------------------

    /**
     * Deletes all resources created by tests, after all tests have been run.
     *
     * This cleanup method will always be run, even if one or more tests fail.
     * For this reason, it attempts to remove all resources created
     * at any point during testing, even if some of those resources
     * may be expected to be deleted by certain tests.
     * @throws Exception 
     */
    @AfterClass(alwaysRun=true)
    public void cleanUp() throws Exception {
        String noTest = System.getProperty("noTestCleanup");
    	if(Boolean.TRUE.toString().equalsIgnoreCase(noTest)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Skipping Cleanup phase ...");
            }
            return;
    	}
        if (logger.isDebugEnabled()) {
            logger.debug("Cleaning up temporary resources created for testing ...");
        }
        PersonAuthorityClient personAuthClient = new PersonAuthorityClient();
        // Delete Person resource(s) (before PersonAuthority resources).
        
        for (String resourceId : personIdsCreated) {
            // Note: Any non-success responses are ignored and not reported.
            personAuthClient.deleteItem(personAuthCSID, resourceId).close();
        }
        
        // Delete PersonAuthority resource(s).
        // Note: Any non-success response is ignored and not reported.
        if (personAuthCSID != null) {
        	personAuthClient.delete(personAuthCSID);
	        // Delete Conservation resource(s).
        	ConservationClient conservationClient = new ConservationClient();
	        for (String resourceId : conservationIdsCreated) {
	            // Note: Any non-success responses are ignored and not reported.
                conservationClient.delete(resourceId).close(); 
	        }
        }
    }

    // ---------------------------------------------------------------
    // Utility methods used by tests above
    // ---------------------------------------------------------------
    public String getServiceName() {
        return SERVICE_NAME;
    }

    @Override
    public String getServicePathComponent() {
        return SERVICE_PATH_COMPONENT;
    }

    private PoxPayloadOut createConservationInstance(String conservationNumber,
            String conservatorRefName,
            String otherPartyRefName,
            String examinationStaffRefName,
            String approvedByRefName,
            String researcherRefName,
            String sampleByRefName) throws Exception {
        ConservationCommon conservationCommon = new ConservationCommon();
        conservationCommon.setConservationNumber(conservationNumber);

        ConservatorsList conservatorsList =  new ConservatorsList();
        conservatorsList.getConservator().add(conservatorRefName);

        OtherPartyGroupList otherPartyGroupList = new OtherPartyGroupList();
        OtherPartyGroup otherPartyGroup = new OtherPartyGroup();
        otherPartyGroup.setOtherParty(otherPartyRefName);
        otherPartyGroupList.getOtherPartyGroup().add(otherPartyGroup);

        ExaminationGroupList examinationGroupList = new ExaminationGroupList();
        ExaminationGroup examinationGroup = new ExaminationGroup();
        examinationGroup.setExaminationStaff(examinationStaffRefName);
        examinationGroupList.getExaminationGroup().add(examinationGroup);
        
        DestAnalysisGroupList destAnalysisGroupList = new DestAnalysisGroupList();
        DestAnalysisGroup destAnalysisGroup = new DestAnalysisGroup();
        destAnalysisGroup.setSampleBy(sampleByRefName);
        destAnalysisGroupList.getDestAnalysisGroup().add(destAnalysisGroup);
        
        conservationCommon.setConservators(conservatorsList);
        conservationCommon.setOtherPartyGroupList(otherPartyGroupList);
        conservationCommon.setExaminationGroupList(examinationGroupList);
        conservationCommon.setApprovedBy(approvedByRefName);
        conservationCommon.setResearcher(researcherRefName);
        conservationCommon.setDestAnalysisGroupList(destAnalysisGroupList);
        
        PoxPayloadOut multipart = new PoxPayloadOut(this.getServicePathComponent());
        PayloadOutputPart commonPart =
            multipart.addPart(new ConservationClient().getCommonPartName(), conservationCommon);
        
        if(logger.isDebugEnabled()){
            logger.debug("to be created, conservation common");
            logger.debug(objectAsXmlString(conservationCommon, ConservationCommon.class));
        }

        return multipart;
    }

    @Override
    protected Class<AbstractCommonList> getCommonListType() {
        return AbstractCommonList.class;
    }

}
