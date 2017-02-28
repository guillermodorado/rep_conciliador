package com.bs.opt.conciliador.main;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;
import com.bs.opt.conciliador.bd.MapperDAO;
import com.bs.opt.conciliador.jaxb2.vo.request.TipoDatosOperacion;
import com.bs.opt.conciliador.process.CreateXLS;
import com.bs.opt.conciliador.process.ProcessOperations;
import com.bs.opt.conciliador.utilities.ConciliationExceptionHandler;
import com.bs.opt.conciliador.vo.ConciliationExceptionVO;
import com.bs.opt.conciliador.vo.ResultConciliationVO;
import com.bs.opt.conciliador.xml.MapperXML; 
import java.util.logging.Level;
import java.util.logging.LogManager;

public class Main2 {

	private final static Logger LOGGER = Logger.getLogger("com.bs.opt.conciliador");
	
	private static void loadPropertiesLogger() throws ConciliationExceptionVO {
	
		try {
		
			LogManager.getLogManager().readConfiguration(new FileInputStream("src/main/resources/log.properties"));
			
		} catch (IOException e) {
			
			LOGGER.log(Level.SEVERE, "NO ha podido crear el FileHandler para enviar las trazas a un fichero .log");
			
			ConciliationExceptionHandler.throwConciliationExceptionVO(e);  
						
		}
	}
	
	public static void main(String argv[]) {
		
		try {
		
			//creamos un log para volcar las trazas en un fichero.
			loadPropertiesLogger(); 
			
			LOGGER.log(Level.INFO, "--> INICIO");  
			
			MapperXML mapperXML = new MapperXML();
			
			HashMap<String, TipoDatosOperacion> hmOperacionesXML = mapperXML.getDataOperationsXML();
			
			if (hmOperacionesXML != null) {  
				
				MapperDAO mapperDAO = new MapperDAO();
				
				HashMap<String, TipoDatosOperacion> hmOperacionesBD = mapperDAO.getDataOperationsBD(hmOperacionesXML);  
				
				ProcessOperations processOperations = new ProcessOperations(); 
				
				ResultConciliationVO resultConciliation = processOperations.conciliation(hmOperacionesXML, hmOperacionesBD);
				
				CreateXLS createXLS = new CreateXLS();
				
				createXLS.toXLS(resultConciliation);
			}
			
		} catch (ConciliationExceptionVO conciliationExceptionVO) {
			
			String code 		= conciliationExceptionVO.getCode();
			String description  = conciliationExceptionVO.getDescription();
			String causeDetail  = conciliationExceptionVO.getCauseDetail();
			
			LOGGER.log(Level.SEVERE, causeDetail);
		}
				
		LOGGER.log(Level.INFO, "<-- FIN"); 
	}
}