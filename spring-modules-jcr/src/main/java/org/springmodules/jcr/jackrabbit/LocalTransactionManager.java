package org.springmodules.jcr.jackrabbit;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.transaction.xa.XAResource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.InvalidIsolationLevelException;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.SmartTransactionObject;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springmodules.jcr.SessionFactory;
import org.springmodules.jcr.SessionFactoryUtils;
import org.springmodules.jcr.SessionHolder;
import org.springmodules.jcr.jackrabbit.support.UserTxSessionHolder;

/**
 * PlatformTransactionManager implementation for a single JCR SessionFactory.
 * 
 * Binds a Jcr session from the specified SessionFactory to the thread,
 * potentially allowing for one thread session per session factory.
 * 
 * <p>
 * This local strategy is an alternative to executing JCR operations within JTA
 * transactions. Its advantage is that it is able to work in any environment,
 * for example a standalone application or a test suite. It is <i>not</i> able
 * to provide XA transactions, for example to share transactions with data
 * access.
 * 
 * <p>
 * JcrTemplate will auto-detect such thread-bound connection/session pairs and
 * automatically participate in them. There is currently no support for letting
 * plain JCR code participate in such transactions.
 * 
 * <p>
 * This transaction strategy will typically be used in combination with a single
 * JCR Repository for all JCR access to save resources, typically in a
 * standalone application. 

 * 
 * @see javax.jcr.RepositoryException
 * @see org.springframework.transaction.support.AbstractPlatformTransactionManager
 * 
 * @author Costin Leau
 * @author Guillaume Bort <guillaume.bort@zenexity.fr>
 * 
 */
public class LocalTransactionManager extends AbstractPlatformTransactionManager implements InitializingBean {
    private SessionFactory sessionFactory;

    /**
     * Create a new JcrTransactionManager instance.
     * 
     */
    public LocalTransactionManager() {
    }

    /**
     * Create a new JcrTransactionManager instance.
     * 
     * @param sessionFactory
     *            Repository to manage transactions for
     */
    public LocalTransactionManager(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * @return Returns the sessionFactory.
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * @param sessionFactory
     *            The sessionFactory to set.
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void afterPropertiesSet() throws Exception {
        if (getSessionFactory() == null)
            throw new IllegalArgumentException("repository is required");
    }

    protected Object doGetTransaction() throws TransactionException {
        JcrTransactionObject txObject = new JcrTransactionObject();

        if (TransactionSynchronizationManager.hasResource(getSessionFactory())) {
            UserTxSessionHolder sessionHolder = (UserTxSessionHolder) TransactionSynchronizationManager.getResource(getSessionFactory());
            if (logger.isDebugEnabled()) {
                logger.debug("Found thread-bound session [" + sessionHolder.getSession() + "] for JCR transaction");
            }
            txObject.setSessionHolder(sessionHolder, false);
        }

        return txObject;
    }

    protected boolean isExistingTransaction(Object transaction) throws TransactionException {
        return ((JcrTransactionObject) transaction).hasTransaction();
    }

    protected void doBegin(Object transaction, TransactionDefinition transactionDefinition) throws TransactionException {
        if (transactionDefinition.getIsolationLevel() != TransactionDefinition.ISOLATION_DEFAULT) {
            throw new InvalidIsolationLevelException("JCR does not support an isolation level concept");
        }

        Session session = null;

        try {
            JcrTransactionObject txObject = (JcrTransactionObject) transaction;
            if (txObject.getSessionHolder() == null) {
                // get the new session
                Session newSession = sessionFactory.getSession();

                // make sure we have an XAResource
                if (!(newSession instanceof XAResource))
                    throw new IllegalArgumentException("transactions are not supported by your Jcr Repository");

                if (logger.isDebugEnabled()) {
                    logger.debug("Opened new session [" + newSession + "] for JCR transaction");
                }
                txObject.setSessionHolder(new UserTxSessionHolder(newSession), true);
            }

            UserTxSessionHolder sessionHolder = txObject.getSessionHolder();

            sessionHolder.setSynchronizedWithTransaction(true);
            session = sessionHolder.getSession();

            /*
             * We have no notion of flushing inside a JCR session
             * 
            if (transactionDefinition.isReadOnly() && txObject.isNewSessionHolder()) {
                sessionHolder.setReadOnly(true);
            }

            if (!transactionDefinition.isReadOnly() && !txObject.isNewSessionHolder()) {
                if (sessionHolder.isReadOnly()) {
                    sessionHolder.setReadOnly(false);
                }
            }
            */

            // start the transaction
            sessionHolder.getTransaction().begin();

            // Register transaction timeout.
            if (transactionDefinition.getTimeout() != TransactionDefinition.TIMEOUT_DEFAULT) {
                txObject.getSessionHolder().setTimeoutInSeconds(transactionDefinition.getTimeout());
            }

            // Bind the session holder to the thread.
            if (txObject.isNewSessionHolder()) {
                TransactionSynchronizationManager.bindResource(getSessionFactory(), sessionHolder);
            }
        }

        catch (Exception ex) {
            SessionFactoryUtils.releaseSession(session, getSessionFactory());
            throw new CannotCreateTransactionException("Could not open JCR session for transaction", ex);
        }
    }

    protected Object doSuspend(Object transaction) {
        JcrTransactionObject txObject = (JcrTransactionObject) transaction;
        txObject.setSessionHolder(null, false);
        SessionHolder sessionHolder = (UserTxSessionHolder) TransactionSynchronizationManager.unbindResource(getSessionFactory());
        return new SuspendedResourcesHolder(sessionHolder);
    }

    protected void doResume(Object transaction, Object suspendedResources) {
        SuspendedResourcesHolder resourcesHolder = (SuspendedResourcesHolder) suspendedResources;
        if (TransactionSynchronizationManager.hasResource(getSessionFactory())) {
            // From non-transactional code running in active transaction
            // synchronization
            // -> can be safely removed, will be closed on transaction
            // completion.
            TransactionSynchronizationManager.unbindResource(getSessionFactory());
        }
        TransactionSynchronizationManager.bindResource(getSessionFactory(), resourcesHolder.getSessionHolder());
    }

    protected void doCommit(DefaultTransactionStatus status) {
        JcrTransactionObject txObject = (JcrTransactionObject) status.getTransaction();
        if (status.isDebug()) {
            logger.debug("Committing JCR transaction on session [" + txObject.getSessionHolder().getSession() + "]");
        }
        try {
            txObject.getSessionHolder().getTransaction().commit();
        } catch (Exception ex) {
            // assumably from commit call to the underlying JCR repository
            throw new TransactionSystemException("Could not commit JCR transaction", ex);
        }
    }

    protected void doRollback(DefaultTransactionStatus status) {
        JcrTransactionObject txObject = (JcrTransactionObject) status.getTransaction();
        if (status.isDebug()) {
            logger.debug("Rolling back JCR transaction on session [" + txObject.getSessionHolder().getSession() + "]");
        }
        try {
            txObject.getSessionHolder().getTransaction().rollback();
        } catch (Exception ex) {
            throw new TransactionSystemException("Could not roll back JCR transaction", ex);
        } finally {
            if (!txObject.isNewSessionHolder()) {
                // Clear all pending inserts/updates/deletes in the Session.
                // Necessary for pre-bound Sessions, to avoid inconsistent
                // state.
                try {
                    txObject.getSessionHolder().getSession().refresh(false);
                } catch (RepositoryException e) {
                    // we already throw an exception (hold back this one).
                }
            }
        }
    }

    protected void doSetRollbackOnly(DefaultTransactionStatus status) {
        JcrTransactionObject txObject = (JcrTransactionObject) status.getTransaction();
        if (status.isDebug()) {
            logger.debug("Setting JCR transaction on session [" + txObject.getSessionHolder().getSession() + "] rollback-only");
        }
        txObject.setRollbackOnly();
    }

    protected void doCleanupAfterCompletion(Object transaction) {
        JcrTransactionObject txObject = (JcrTransactionObject) transaction;

        // Remove the session holder from the thread.
        if (txObject.isNewSessionHolder()) {
            TransactionSynchronizationManager.unbindResource(getSessionFactory());
        }

        Session session = txObject.getSessionHolder().getSession();
        if (txObject.isNewSessionHolder()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Closing JCR session [" + session + "] after transaction");
            }
            SessionFactoryUtils.releaseSession(session, sessionFactory);
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Not closing pre-bound JCR session [" + session + "] after transaction");
            }
        }
        txObject.getSessionHolder().clear();
    }

    /**
     * Internal transaction object.
     * 
     * @see org.springframework.transaction.support.SmartTransactionObject
     * 
     */
    private static class JcrTransactionObject implements SmartTransactionObject {
        private UserTxSessionHolder sessionHolder;
        private boolean newSessionHolder;

        public void setSessionHolder(UserTxSessionHolder sessionHolder, boolean newSessionHolder) {
            this.sessionHolder = sessionHolder;
            this.newSessionHolder = newSessionHolder;
        }

        public UserTxSessionHolder getSessionHolder() {
            return sessionHolder;
        }

        public boolean isNewSessionHolder() {
            return newSessionHolder;
        }

        public boolean hasTransaction() {
            return (this.sessionHolder != null && this.sessionHolder.getTransaction() != null);
        }

        public void setRollbackOnly() {
            getSessionHolder().setRollbackOnly();
        }

        public boolean isRollbackOnly() {
            return getSessionHolder().isRollbackOnly();
        }

        public void flush() {
        }
    }

    /**
     * Holder for suspended resources. Used internally by doSuspend and
     * doResume.
     */
    private static class SuspendedResourcesHolder {

        private final SessionHolder sessionHolder;

        private SuspendedResourcesHolder(SessionHolder sessionHolder) {
            this.sessionHolder = sessionHolder;
        }

        private SessionHolder getSessionHolder() {
            return sessionHolder;
        }
    }
}
