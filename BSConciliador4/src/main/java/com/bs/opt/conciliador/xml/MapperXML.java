package com.bs.opt.conciliador.xml;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;
import com.bs.opt.conciliador.jaxb2.vo.request.TipoDatosOperacion;
import com.bs.opt.conciliador.jaxb2.vo.request.TipoOperaciones;
import com.bs.opt.conciliador.jaxb2.vo.request.TipoTramiteCDTDual;
import com.bs.opt.conciliador.jaxb2.vo.request.TipoTramiteCDTESP;
import com.bs.opt.conciliador.jaxb2.vo.request.TramiteCNMV;
import com.bs.opt.conciliador.jaxb2.vo.response.TipoErrorTransaccion;
import com.bs.opt.conciliador.jaxb2.vo.response.TipoErroresTransaccion;
import com.bs.opt.conciliador.jaxb2.vo.response.TipoInfoErrores;
import com.bs.opt.conciliador.jaxb2.vo.response.TipoTramiteCDTRESESP;
import com.bs.opt.conciliador.utilities.ConciliationExceptionHandler;
import com.bs.opt.conciliador.utilities.ConciliationValidator;
import com.bs.opt.conciliador.vo.ConciliationExceptionVO;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MapperXML {

	private ResourceBundle rb = null;
	
	private final static Logger LOGGER = Logger.getLogger(MapperXML.class.getName()); 
	
	public MapperXML() {
		// TODO Auto-generated constructor stub
		super();
		
		rb = ResourceBundle.getBundle("config");
	}
	
	public HashMap<String, TipoDatosOperacion> getDataOperationsXML() throws ConciliationExceptionVO {
		
		LOGGER.log(Level.INFO, "--> getDatosOperacionesXML()");
		
		//HashMap donde metermos las operaciones. La clave será el número de opoeración.
		HashMap<String, TipoDatosOperacion> hmOperacionesRequest = new HashMap<String, TipoDatosOperacion>();
		
		//HashMap donde metermos los errores si los hubiera. La clave será el número de opoeración.
		HashMap<String, TipoErrorTransaccion> hmOperacionesResponse = new HashMap<String, TipoErrorTransaccion>();
		
		try {
			
			//Cargamos el xml en memoria mediante jaxb a través de las clases generadas dinámicamente a partir de éste.
			String ficheroOperacionesXml = rb.getString("bs.opt.xml.fichero.ruta.request").trim() + rb.getString("bs.opt.xml.fichero.nombre.request").trim();
			JAXBContext jaxbContextRequest = JAXBContext.newInstance(rb.getString("bs.opt.jaxb2.paquete.request").trim());
	        Unmarshaller unmarshallerRequest = jaxbContextRequest.createUnmarshaller();
	        LOGGER.log(Level.INFO, "Cargamos el fichero xml <" + ficheroOperacionesXml + "> en memoria mediante jaxb a través de las clases generadas dinámicamente a partir de éste.");
	        TramiteCNMV reqTramiteCNMV = (TramiteCNMV) unmarshallerRequest.unmarshal(new FileInputStream(ficheroOperacionesXml));
	        TipoTramiteCDTDual reqTramiteCDT = reqTramiteCNMV.getTramiteCDT();
	        TipoTramiteCDTESP reqTramiteCDTESP = reqTramiteCDT.getTramiteCDTESP();
	        
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			//lo primero: validar que la fecha de creación del XML es una fecha correcta. De momento la comparamos con la fecha actual.
	        XMLGregorianCalendar fechaCreacionXML = reqTramiteCDTESP.getFechaCreacion();
			boolean fechaOK = ConciliationValidator.fechaCreacionXML(fechaCreacionXML);
			
	        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
		        
			if (fechaOK) {
	
				LOGGER.log(Level.INFO, "La fecha de creación que viene en el xml es correcta.");
				
		        TipoOperaciones reqOperaciones = reqTramiteCDTESP.getOperaciones();
		        java.util.List<Object> listReqOperaciones = reqOperaciones.getDatosOperacionOrDatosCancelacion();
		        for (int i=0; i<listReqOperaciones.size(); i++) { 
		        	TipoDatosOperacion reqTipoDatosOperacion = (TipoDatosOperacion)listReqOperaciones.get(i);
		        	String reqIdentOperacion = reqTipoDatosOperacion.getIdentificadorOperacion();
		        	hmOperacionesRequest.put(reqIdentOperacion, reqTipoDatosOperacion); //Guardamos cada operación en el HashMap.
		        }
		        
		        LOGGER.log(Level.INFO, "Hemos volcado las operaciones del xml a objetos java en memoria.");
		        
		        //Cargamos el xml en memoria mediante jaxb a través de las clases generadas dinámicamente a partir de éste.
		        JAXBContext jaxbContextReponse = JAXBContext.newInstance(rb.getString("bs.opt.jaxb2.paquete.response").trim());
		        Unmarshaller unmarshallerResponse = jaxbContextReponse.createUnmarshaller();
		        com.bs.opt.conciliador.jaxb2.vo.response.TramiteCNMV resTramiteCNMV = (com.bs.opt.conciliador.jaxb2.vo.response.TramiteCNMV) unmarshallerResponse.unmarshal(new FileInputStream(rb.getString("bs.opt.xml.fichero.ruta.response").trim() + rb.getString("bs.opt.xml.fichero.nombre.response").trim()));
		        com.bs.opt.conciliador.jaxb2.vo.response.TipoTramiteCDTDual resTramiteCDT = resTramiteCNMV.getTramiteCDT();
		        TipoTramiteCDTRESESP resTramiteCDTESP = resTramiteCDT.getTramiteCDTRESESP();
		        String sinErrores = resTramiteCDTESP.getSinErrores();
		        if (sinErrores != null && sinErrores.equalsIgnoreCase("OK")) {        	
		        	//System.out.println("FICHERO XML DE RESPUESTA (response): OK");        	
		        } else {        	
		        	TipoInfoErrores resTipoInfoErrores = resTramiteCDTESP.getInfoErrores();
		        	TipoErroresTransaccion resTipoErroresTransaccion = resTipoInfoErrores.getErroresOperacion();
		        	List<TipoErrorTransaccion> listResTipoErrorTransaccion = resTipoErroresTransaccion.getErrorTransaccion();
		        	for (int i=0; i<listResTipoErrorTransaccion.size(); i++) {
		        		TipoErrorTransaccion resTipoErrorTransaccion = listResTipoErrorTransaccion.get(i);
		        		String resIdentOperacion = resTipoErrorTransaccion.getIdentificadorOperacion();
		        		hmOperacionesResponse.put(resIdentOperacion, resTipoErrorTransaccion); 
		        	}
		        }		    
		        
			} else {
				
				//La fecha no es correcta. No hacemos nada y nos salimos...
				hmOperacionesRequest = null;
				
				LOGGER.log(Level.INFO, "La fecha de creación que viene en el xml NO es correcta; no haces nada y salimos de la operativa...");  
			}
		
		} catch (Exception e) {

			ConciliationExceptionHandler.throwConciliationExceptionVO(e); 
		}
		
		LOGGER.log(Level.INFO, "<-- getDatosOperacionesXML()"); 
	
		return hmOperacionesRequest;
	}
	
}
