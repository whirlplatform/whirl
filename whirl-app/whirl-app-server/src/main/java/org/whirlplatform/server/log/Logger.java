package org.whirlplatform.server.log;

public interface Logger {

	void info(Object message);
	
	void info(Object message, Throwable throwable);
	
	void warn(Object message);
	
	void warn(Object message, Throwable throwable);
	
	void error(Object message);
	
	void error(Object message, Throwable throwable);
	
	void debug(Object message);
	
	void debug(Object message, Throwable throwable);
	
}
