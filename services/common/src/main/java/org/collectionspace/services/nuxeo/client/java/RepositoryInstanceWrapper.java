package org.collectionspace.services.nuxeo.client.java;

import java.security.Principal;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.Filter;
import org.nuxeo.ecm.core.api.IterableQueryResult;
import org.nuxeo.ecm.core.api.NoRollbackOnException;
import org.nuxeo.ecm.core.api.event.DocumentEventTypes;
import org.nuxeo.ecm.core.api.repository.RepositoryInstance;

public class RepositoryInstanceWrapper implements RepositoryInstanceInterface {

	private RepositoryInstance repoSession;
	
	public RepositoryInstanceWrapper(RepositoryInstance repoSession) {
		this.repoSession = repoSession;
	}

	@Override
	public 	RepositoryInstance getRepositoryInstance() {
		return repoSession;
	}
	
	@Override
    public String getSessionId() {
    	return repoSession.getSessionId();
    }
    
    @Override
    public void close() throws Exception {
    	repoSession.close();
    }

    /**
     * Gets the root document of this repository.
     *
     * @return the root document. cannot be null
     * @throws ClientException
     * @throws SecurityException
     */
	@Override
    public DocumentModel getRootDocument() throws ClientException {
    	return repoSession.getRootDocument();
    }
    
    /**
     * Returns the repository name against which this core session is bound.
     *
     * @return the repository name used currently used as an identifier
     */
	@Override
    public String getRepositoryName() {
		return repoSession.getRepositoryName();
	}
    
    /**
     * Gets the principal that created the client session.
     *
     * @return the principal
     */
	@Override
	public Principal getPrincipal() {
		return repoSession.getPrincipal();
	}

	@Override
	public IterableQueryResult queryAndFetch(String query, String queryType,
            Object... params) throws ClientException {
		return repoSession.queryAndFetch(query, queryType, params);
	}

	@Override
	public DocumentModelList query(String query, Filter filter, long limit,
            long offset, boolean countTotal) throws ClientException {
		return repoSession.query(query, filter, limit, offset, countTotal);
	}

	@Override
    public DocumentModelList query(String query, int max) throws ClientException {
    	return repoSession.query(query, max);
    }
    
	@Override
	public DocumentModelList query(String query) throws ClientException {
		return repoSession.query(query);
	}

    /**
     * Gets a document model given its reference.
     * <p>
     * The default schemas are used to populate the returned document model.
     * Default schemas are configured via the document type manager.
     * <p>
     * Any other data model not part of the default schemas will be lazily
     * loaded as needed.
     *
     * @param docRef the document reference
     * @return the document
     * @throws ClientException
     * @throws SecurityException
     */
    @NoRollbackOnException
    @Override
    public DocumentModel getDocument(DocumentRef docRef) throws ClientException {
	    return repoSession.getDocument(docRef);
    }

    @Override
    public DocumentModel saveDocument(DocumentModel docModel) throws ClientException {
    	return repoSession.saveDocument(docModel);
    }

    @Override
    public void save() throws ClientException {
    	repoSession.save();
    }

    /**
     * Bulk document saving.
     *
     * @param docModels the document models that needs to be saved
     * @throws ClientException
     */
    @Override
    public void saveDocuments(DocumentModel[] docModels) throws ClientException {
    	repoSession.saveDocuments(docModels);
    }

    /**
     * Removes this document and all its children, if any.
     *
     * @param docRef the reference to the document to remove
     * @throws ClientException
     */
    @Override
    public void removeDocument(DocumentRef docRef) throws ClientException {
    	repoSession.removeDocument(docRef);
    }

    /**
     * Creates a document model using required information.
     * <p>
     * Used to fetch initial datamodels from the type definition.
     * <p>
     * DocumentModel creation notifies a
     * {@link DocumentEventTypes.EMPTY_DOCUMENTMODEL_CREATED} so that core event
     * listener can initialize its content with computed properties.
     *
     * @param parentPath
     * @param id
     * @param typeName
     * @return the initial document model
     * @throws ClientException
     */
    @Override
    public DocumentModel createDocumentModel(String parentPath, String id,
            String typeName) throws ClientException {
    	return repoSession.createDocumentModel(parentPath, id, typeName);
    }
    
    /**
     * Creates a document using given document model for initialization.
     * <p>
     * The model contains path of the new document, its type and optionally the
     * initial data models of the document.
     * <p>
     *
     * @param model the document model to use for initialization
     * @return the created document
     * @throws ClientException
     */
    @Override
    public DocumentModel createDocument(DocumentModel model) throws ClientException {
    	return repoSession.createDocument(model);
    }
    
    /**
     * Gets the children of the given parent.
     *
     * @param parent the parent reference
     * @return the children if any, an empty list if no children or null if the
     *         specified parent document is not a folder
     * @throws ClientException
     */
    @NoRollbackOnException
    @Override
    public DocumentModelList getChildren(DocumentRef parent) throws ClientException {
    	return repoSession.getChildren(parent);
    }

    
	
}
