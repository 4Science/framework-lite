package it.cilea.core.spring.dao.hibernate;

import it.cilea.core.model.Identifiable;
import it.cilea.core.spring.dao.GenericDao;
import it.cilea.core.spring.model.LastModifiableObject;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.ObjectRetrievalFailureException;

/**
 * This class serves as the Base class for all other DAOs - namely to hold
 * common CRUD methods that they might all use. You should only need to extend
 * this class when your require custom CRUD logic.
 * <p/>
 * <p>
 * To register this class in your Spring context file, use the following XML.
 * 
 * <pre>
 *      &lt;bean id="fooDao" class="org.appfuse.dao.hibernate.GenericDaoHibernate"&gt;
 *          &lt;constructor-arg value="org.appfuse.model.Foo"/&gt;
 *      &lt;/bean&gt;
 * </pre>
 * 
 * @author <a href="mailto:bwnoll@gmail.com">Bryan Noll</a>
 * @param <T>
 *            a type variable
 * @param <PK>
 *            the primary key for that type
 */
public class GenericDaoHibernate<T extends Identifiable<PK>, PK extends Serializable> implements GenericDao<T, PK> {
	/**
	 * Log variable for all child classes. Uses LogFactory.getLog(getClass())
	 * from Commons Logging
	 */
	protected final Logger log = LoggerFactory.getLogger(getClass());

	private Class<T> persistentClass;

	private SessionFactory sessionFactory;

	/**
	 * Constructor that takes in a class to see which type of entity to persist.
	 * Use this constructor when subclassing.
	 * 
	 * @param persistentClass
	 *            the class type you'd like to persist
	 */
	public GenericDaoHibernate(final Class<T> persistentClass) {
		this.persistentClass = persistentClass;
	}

	/**
	 * Constructor that takes in a class and sessionFactory for easy creation of
	 * DAO.
	 * 
	 * @param persistentClass
	 *            the class type you'd like to persist
	 * @param sessionFactory
	 *            the pre-configured Hibernate SessionFactory
	 */
	public GenericDaoHibernate(final Class<T> persistentClass, SessionFactory sessionFactory) {
		this(persistentClass);
		this.sessionFactory = sessionFactory;

	}

	public SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}

    @Qualifier("sessionFactorySur")
	@Autowired
	@Required
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;

	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public T get(PK id) {
		T entity = (T) sessionFactory.getCurrentSession().get(this.persistentClass, id);
		if (entity == null) {
			log.warn("Uh oh, '" + this.persistentClass + "' object with id '" + id + "' not found...");
			throw new ObjectRetrievalFailureException(this.persistentClass, id);
		}
		return entity;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public boolean exists(PK id) {
		T entity = (T) sessionFactory.getCurrentSession().get(this.persistentClass, id);
		return entity != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public T save(T object) {
		if (log.isDebugEnabled()) {
			log.debug("object's id: " + object.getId());
		}
		if (object instanceof LastModifiableObject)
			((LastModifiableObject) object).setLastModified(new Date());

		if (object.getId() == null) {
			sessionFactory.getCurrentSession().save(object);
			return (T) object;
		} else
			return (T) sessionFactory.getCurrentSession().merge(object);
	}

	/**
	 * {@inheritDoc}
	 */

	public void remove(PK id) {
		sessionFactory.getCurrentSession().delete(this.get(id));
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public List<T> findByNamedQueryAndNamedParam(String queryName, Map<String, Object> queryParams) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(queryName);
		for (String s : queryParams.keySet()) {
			query.setParameter(s, queryParams.get(s));
		}
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<T> findByNamedQuery(String queryName, Object... values) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(queryName);
		int i = 0;
		for (Object s : values) {
			query.setParameter(i, s);
			i++;
		}
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<T> findByNamedQuery(String queryName) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(queryName);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<T> findByNamedQueryAndNamedParam(String queryName, String key, Object value) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(queryName);
		query.setParameter(key, value);
		return query.list();
	}

	@Override
	public Class<T> getPersistentClass() {
		return this.persistentClass;
	}

	@SuppressWarnings("unchecked")
	public PK findIdByPropertyIdentifier(String propertyName, Object value) {
		log.error("Unsupported operation. This method must be implemented in specific DAO class");
		return null;
	}
}
