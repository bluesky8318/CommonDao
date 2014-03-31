/**
 * 
 */
package cn.org.zeronote.orm.jta;

import javax.naming.NamingException;
import javax.transaction.SystemException;

import org.objectweb.jotm.Current;
import org.objectweb.jotm.Jotm;
import org.objectweb.jotm.TimerManager;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;

/**
 * 从spring2.5中copy过来并修改
 * Spring提供的实现里没有停止Jotm的两个守护线程
 * @author <a href="mailto:lizheng8318@gmail.com">lizheng</a>
 */
public class JotmFactoryBeanEx implements FactoryBean<Current>, DisposableBean {

	private Current jotmCurrent;

	private Jotm jotm;


	public JotmFactoryBeanEx() throws NamingException {
		// Check for already active JOTM instance.
		this.jotmCurrent = Current.getCurrent();

		// If none found, create new local JOTM instance.
		if (this.jotmCurrent == null) {
			// Only for use within the current Spring context:
			// local, not bound to registry.
			this.jotm = new Jotm(true, false);
			this.jotmCurrent = Current.getCurrent();
		}
	}

	/**
	 * Set the default transaction timeout for the JOTM instance.
	 * <p>Should only be called for a local JOTM instance,
	 * not when accessing an existing (shared) JOTM instance.
	 * @param defaultTimeout default timeout
	 */
	public void setDefaultTimeout(int defaultTimeout) {
		this.jotmCurrent.setDefaultTimeout(defaultTimeout);
		// The following is a JOTM oddity: should be used for demarcation transaction only,
		// but is required here in order to actually get rid of JOTM's default (60 seconds).
		try {
			this.jotmCurrent.setTransactionTimeout(defaultTimeout);
		}
		catch (SystemException ex) {
			// should never happen
		}
	}


	/**
	 * Return the JOTM instance created by this factory bean, if any.
	 * Will be <code>null</code> if an already active JOTM instance is used.
	 * <p>Application code should never need to access this.
	 * @return Jotm
	 */
	public Jotm getJotm() {
		return this.jotm;
	}

	public Current getObject() {
		return this.jotmCurrent;
	}

	public Class<?> getObjectType() {
		return this.jotmCurrent.getClass();
	}

	public boolean isSingleton() {
		return true;
	}


	/**
	 * Stop the local JOTM instance, if created by this FactoryBean.
	 */
	public void destroy() {
		if (this.jotm != null) {
			this.jotm.stop();
		}
		TimerManager.stop();
	}

}
