package org.cohorte.studio.eclipse.core.log;

import java.util.IllegalFormatException;

import javax.inject.Inject;

import org.cohorte.studio.eclipse.api.managers.ILogger;
import org.eclipse.e4.core.services.log.Logger;

/**
 * Format save logger.
 * If illegal format is passed as an argument to any of the log message, a different error message will be logger.
 * The original message will still be logged (with the original severity) at its pattern, ignoring all the other
 * arguments.
 * 
 * @author Ahmad Shahwan
 *
 */
public class CLogger implements ILogger {
	
	private Logger pE4Logger;
	
	@Inject
	public CLogger(Logger logger) {
		this.pE4Logger = logger;
	}

	@Override
	public void debug(String aPattern, Object... aArgs) {
		this.pE4Logger.debug(format(aPattern, aArgs));
	}

	@Override
	public void info(String aPattern, Object... aArgs) {
		this.pE4Logger.info(String.format(aPattern, aArgs));
	}

	@Override
	public void warning(String aPattern, Object... aArgs) {
		this.pE4Logger.warn(format(aPattern, aArgs));

	}

	@Override
	public void warning(Throwable e, String aPattern, Object... aArgs) {
		this.pE4Logger.warn(e, format(aPattern, aArgs));

	}

	@Override
	public void error(String aPattern, Object... aArgs) {
		this.pE4Logger.error(format(aPattern, aArgs));
	}

	@Override
	public void error(Throwable e, String aPattern, Object... aArgs) {
		this.pE4Logger.error(e, format(aPattern, aArgs));
	}
	
	/**
	 * Safely format a log message.
	 * 
	 * @param aPattern
	 * @param aArgs
	 * @return
	 */
	private String format(String aPattern, Object... aArgs) {
		try {
			return String.format(aPattern, aArgs);
		} catch (IllegalFormatException e) {
			this.pE4Logger.error(e, "Format error!");
			return aPattern;
		}		
	}

}
