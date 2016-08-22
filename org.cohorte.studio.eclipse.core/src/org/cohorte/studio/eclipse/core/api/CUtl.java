package org.cohorte.studio.eclipse.core.api;

import java.util.function.Supplier;

import org.cohorte.studio.eclipse.api.annotations.NonNull;

/**
 * Utilities class.
 * This class conatins static utility methods and data members.
 * 
 * @author Ahmad Shahwan
 *
 */
public class CUtl {
	
	/**
	 * Return an object upon which {@link IEither#or(Object)} can be called.
	 * This is used to define defaults in case of null value.
	 * 
	 * @param aValue a value that can be null.
	 * @return
	 */
	public static <T> IEither<T> either(T aValue) {
		return new CEither<>(aValue);		
	}
	
	/**
	 * An object of this type embed a value that may or may not be null.
	 * 
	 * @author Ahmad Shahwan
	 *
	 * @param <T>
	 */
	public interface IEither <T> {
		
		/**
		 * The embedded value if not null, otherwise passed parameter.
		 * 
		 * @param aDefault a non-null default value.
		 * @return the embedded value if it is not null, otherwise, the value passed as a parameter.
		 */
		public @NonNull T or(@NonNull T aDefault);
		
		/**
		 * The embedded value if not null, otherwise value supplied by passed parameter.
		 * 
		 * @param aSupplier a non-null supplier.
		 * @return the embedded value if it is not null, otherwise, the value supplied by the passed as a parameter.
		 */
		public @NonNull T or(Supplier<@NonNull T> aSupplier);
	}
	
	/**
	 * {@link IEither} implementation.
	 * 
	 * @author Ahmad Shahwan
	 *
	 * @param <T>
	 */
	private static class CEither <T> implements IEither<T> {
		
		private T pValue;
		
		private CEither(T aValue) {
			this.pValue = aValue;
		}

		@Override
		public @NonNull T or(@NonNull T aDefault) {
			if (this.pValue != null) {
				return this.pValue;
			}
			return aDefault;
		}

		@Override
		public @NonNull T or(Supplier<@NonNull T> aSupplier) {
			return aSupplier.get();
		}		
	}
}
