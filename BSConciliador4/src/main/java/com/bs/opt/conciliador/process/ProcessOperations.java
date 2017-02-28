package com.bs.opt.conciliador.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import com.bs.opt.conciliador.jaxb2.vo.request.TipoDatosOperacion;
import com.bs.opt.conciliador.utilities.ConciliationExceptionHandler;
import com.bs.opt.conciliador.vo.ConciliationExceptionVO;
import com.bs.opt.conciliador.vo.ResultConciliationVO;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProcessOperations {
	
	private final static Logger LOGGER = Logger.getLogger(ProcessOperations.class.getName());
	
	public ProcessOperations() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ResultConciliationVO conciliation(HashMap<String, TipoDatosOperacion> hmOperacionesXML, HashMap<String, TipoDatosOperacion> hmOperacionesBD) throws ConciliationExceptionVO {
		
		LOGGER.log(Level.INFO, "--> conciliation()");
		
		ArrayList<TipoDatosOperacion> arrayListOK = new ArrayList<TipoDatosOperacion>();
		ArrayList<TipoDatosOperacion> arrayListKO = new ArrayList<TipoDatosOperacion>();
		
		ResultConciliationVO resultConciliationVO = new ResultConciliationVO();
		
		try {
			
			Set<String> setOperacionesXML = hmOperacionesXML.keySet();
			Iterator<String> iterOperacionesXML = setOperacionesXML.iterator();
			while (iterOperacionesXML.hasNext()) {
				
				//N�mero de Identifcaci�n que viene en la operaci�n del xml
				String claveOperacionXML = iterOperacionesXML.next();
				
				TipoDatosOperacion operacionXML = hmOperacionesXML.get(claveOperacionXML);
				TipoDatosOperacion operacionBD = hmOperacionesBD.get(claveOperacionXML);
				
				//lo buscamos en el HashMap de las operaciones obtenidas de la base de datos
				if (hmOperacionesBD.containsKey(claveOperacionXML)) {
					
					//comparamos ambas operaciones: XML y BD para saber si se mantiene la conciliaci�n
					boolean operationOK = compareOperations (operacionXML, operacionBD);
					if (operationOK) {
						
						arrayListOK.add(operacionXML);
						LOGGER.log(Level.INFO, "La operaci�n <" + claveOperacionXML + "> est� conciliada respecto al xml y la bd: la a�adimos a la lista de operaciones OK.");
						
					} else {
						
						arrayListKO.add(operacionXML);
						LOGGER.log(Level.INFO, "La operaci�n <" + claveOperacionXML + "> NO est� conciliada respecto al xml y la bd: la a�adimos a la lista de operaciones KO.");
					}	
					
				} else {
					
					//esa operaci�n est� en el xml pero NO en base de datos: la a�adimos a la lista de operaciones KO				
					LOGGER.log(Level.INFO, "La operaci�n <" + claveOperacionXML + "> est� en el xml pero NO en base de datos: la a�adimos a la lista de operaciones KO.");
					
					arrayListKO.add(operacionXML);
				}	
			}
					
			resultConciliationVO.setArrayListKO(arrayListKO);
			
			resultConciliationVO.setArrayListOK(arrayListOK);
			
		} catch (Exception e) {
			
			ConciliationExceptionHandler.throwConciliationExceptionVO(e); 
		}

		LOGGER.log(Level.INFO, "<-- conciliation()");
		
		return resultConciliationVO;
		
	}
	
	private boolean compareOperations (TipoDatosOperacion operacionXML, TipoDatosOperacion operacionBD) throws ConciliationExceptionVO {
		
		boolean retorno = false;
		
		try {

			//campos-valores a comparar
			String identOperacionXML = operacionXML.getIdentificadorOperacion();
			String contrapartidaBICXML = operacionXML.getContrapartida().getContrapartidaBIC();
			
			String identOperacionBD = operacionBD.getIdentificadorOperacion();
			String contrapartidaBICBD = operacionBD.getContrapartida().getContrapartidaBIC();
			
			//comparamos todos los campos
			if (identOperacionXML.equalsIgnoreCase(identOperacionBD) &&
					contrapartidaBICXML.equalsIgnoreCase(contrapartidaBICBD) ) { 
				
				retorno = true;
			}

		} catch (Exception e) {
			
			ConciliationExceptionHandler.throwConciliationExceptionVO(e); 			
		}
		
		return retorno;
	}
	
}
