package com.bs.opt.conciliador.utilities;

import java.util.logging.Level;
import java.util.logging.Logger;
import com.bs.opt.conciliador.vo.ConciliationExceptionVO;


/**
 * Handlers the application exceptions
 */
public class ConciliationExceptionHandler {

	private final static Logger LOGGER = Logger.getLogger(ConciliationExceptionHandler.class.getName()); 
	
    public static void throwConciliationExceptionVO(Exception exception) throws ConciliationExceptionVO {
        
    	String code 		= exception.getMessage();
        String description  = exception.getMessage();
        String causeDetail  = exception.toString();
        
        LOGGER.log(Level.SEVERE, causeDetail); 
        
        throw new ConciliationExceptionVO(code, description, causeDetail);
    }
}
