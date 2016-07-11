package org.cohorte.studio.eclipse.utils.i18n;

/**
 * Internationalization mix-in.
 * 
 * @author Ahmad Shahwan
 *
 */
public interface IInternationalizable {
	
	/**
	 * Return an internationalization of the message ID according to current locale if one is available.
	 * Otherwise returns the message ID.
	 * @param aMsgId message ID
	 * @return
	 */
	default String i(String aMsgId) {
		return CInternationalizationManager.getInstance().getText(aMsgId);
	}
}
