package org.cohorte.studio.eclipse.core.registry;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;

/**
 * Component registration component.
 * Register and unregister service components. 
 * 
 * @author Ahmad Shahwan
 *
 */
public class CRegistrar {
	
	private List<Object> pObjects = new ArrayList<>();
	private IEclipseContext pContext;
	
	/**
	 * Constructor.
	 * 
	 * @param context
	 */
	@Inject
	public CRegistrar(IEclipseContext context) {
		this.pContext = context;
	}

	/**
	 * Create registration record using component factory.
	 * This instantiate a component and prepare it for registration.
	 * 
	 * @param factory		registered factory
	 * @param service		registered service
	 */
	public <T> Record<T> register(Class<T> factory) {
		T wRef = ContextInjectionFactory.make(factory, this.pContext);
		synchronized (this.pObjects) {
			this.pObjects.add(wRef);
		}
		return new Record<>(wRef);
	}
	
	/**
	 * Unregister record.
	 * 
	 * @param object
	 */
	public void unregister(Record<?> record) {
		ContextInjectionFactory.uninject(record.getReference(), this.pContext);
	}
	
	/**
	 * Unregister component.
	 * 
	 * @param object
	 */
	public void unregister(Object object) {
		ContextInjectionFactory.uninject(object, this.pContext);
	}
	
	/**
	 * Unregister all components.
	 * 
	 */
	public void clear() {
		synchronized (this.pObjects) {
			for (Object wRef : pObjects) {
				unregister(wRef);	
			}
		}
	}
	
	/**
	 * Registration record.
	 * 
	 * @author Ahmad Shahwan
	 *
	 * @param <T> type of the registered component factory.
	 */
	public class Record<T> {
		private T pRef;
		
		/**
		 * Constructor.
		 * 
		 * @param ref
		 */
		public Record(T ref) {
			this.pRef = ref;
		}
		
		/**
		 * Finalize registration as a service.
		 * 
		 * @param service
		 * @return
		 */
		public T as(Class<? super T> service) {
			CRegistrar.this.pContext.set(service, this.pRef);
			return this.pRef;
		}
		
		/**
		 * Return component.
		 * 
		 * @return
		 */
		public T getReference() {
			return this.pRef;
		}
	}
}
