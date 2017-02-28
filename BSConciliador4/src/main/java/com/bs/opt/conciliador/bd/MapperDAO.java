package com.bs.opt.conciliador.bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;
import com.bs.opt.conciliador.jaxb2.vo.request.ObjectFactory;
import com.bs.opt.conciliador.jaxb2.vo.request.TipoContrapartida;
import com.bs.opt.conciliador.jaxb2.vo.request.TipoDatosOperacion; 
import com.bs.opt.conciliador.utilities.ConciliationExceptionHandler;
import com.bs.opt.conciliador.vo.ConciliationExceptionVO;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MapperDAO {

	private ResourceBundle rb = null;
	
	private final static Logger LOGGER = Logger.getLogger(MapperDAO.class.getName());
	
	public MapperDAO() {
		
		super();
		// TODO Auto-generated constructor stub
		
		rb = ResourceBundle.getBundle("config");
	}
  
	private Connection getConnectionBD() throws ConciliationExceptionVO {
		
		LOGGER.log(Level.INFO, "--> getConexionBD()");
		
		Connection conexion = null;
		
		try {
	        
			Class.forName(rb.getString("bs.opt.bd.driver").trim()); 
			
			String cadenaConexion = rb.getString("bs.opt.bd.url.connection").trim();
			String usuario = rb.getString("bs.opt.bd.user").trim();
			String password = rb.getString("bs.opt.bd.passowrd").trim();
			
			conexion = DriverManager.getConnection(cadenaConexion, usuario, password);
			
			if (conexion != null) {
				LOGGER.log(Level.INFO, "Obtenida conexión.");
	        } else {
	        	LOGGER.log(Level.INFO, "Error al obtener la conexión.");
	        } 
			
		} catch(Exception e) {
			
			ConciliationExceptionHandler.throwConciliationExceptionVO(e);  
		}
		
		LOGGER.log(Level.INFO, "<-- getConexionBD()");
		
		return conexion;
		
	}
	
	public HashMap<String, TipoDatosOperacion> getDataOperationsBD(HashMap<String, TipoDatosOperacion> hmOperacionesXML) throws ConciliationExceptionVO { 
		
		LOGGER.log(Level.INFO, "--> getDatosOperacionesBD()");
		
		//HashMap donde metermos las operaciones. La clave será el número de opoeración.
		HashMap<String, TipoDatosOperacion> hmOperacionesBD = new HashMap<String, TipoDatosOperacion>();
		
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		Connection connection = null; 
		
		try {
			
			//Las operaciones obtenidas de la BD las vamos guardando en objetos.
			ObjectFactory objectFactory = new ObjectFactory();
			
			//obtenemos del HashMap todas las claves (identificación de operación) para montar la where de la query que lanzaremos.
			String identOperacionXML_Where = "";
			Set<String> setIdentOperacion = hmOperacionesXML.keySet();
			Iterator<String> iterIdentOperacion = setIdentOperacion.iterator();
			for (int i=0; i<setIdentOperacion.size(); i++) {
				identOperacionXML_Where+= "?,";
			}		 	
			identOperacionXML_Where = identOperacionXML_Where.substring(0, identOperacionXML_Where.length()-1);
			String query = "select * from BS_OPT_OPERACIONES_DERIVADOS where NUM_OPER in ( " + identOperacionXML_Where + " )"; 
			
			LOGGER.log(Level.INFO, "query: <" + query + ">");
			
			connection = getConnectionBD();
			preparedStatement = connection.prepareStatement(query);
			
			int i=0;
			while (iterIdentOperacion.hasNext()) {
				i++;
				String sIdentOperacion = iterIdentOperacion.next();
				preparedStatement.setString(i, sIdentOperacion); 
			}	
			resultSet = preparedStatement.executeQuery();  
			 
			if (resultSet != null) { 	
				
				LOGGER.log(Level.INFO, "Mapeo de datos: BD -> VO");
				
				while (resultSet.next()) {
					
					//Mapeo de datos de la BD al VO 							
					String numOperacionBD = resultSet.getString("NUM_OPER").trim();
					String contrapartidaBD = resultSet.getString("CONTRAPARTIDA").trim();
					
					TipoDatosOperacion tipoDatosOperacion = objectFactory.createTipoDatosOperacion();
					tipoDatosOperacion.setIdentificadorOperacion(numOperacionBD);
					TipoContrapartida tipoContrapartida = new TipoContrapartida();
					tipoContrapartida.setContrapartidaBIC(contrapartidaBD);
					tipoDatosOperacion.setContrapartida(tipoContrapartida);
					
					//metemos la operación de la base de datos en un hashmap
					hmOperacionesBD.put(numOperacionBD, tipoDatosOperacion); 						
				}		
				
				LOGGER.log(Level.INFO, "Proceso terminado ok. Número de mapeos de operaciones realizadas: <" + hmOperacionesBD.size() + ">");
			} 
								 
		} catch (SQLException e1) {
			
			ConciliationExceptionHandler.throwConciliationExceptionVO(e1);  
			
		} finally {
				
			try { 
					preparedStatement.close();
					resultSet.close();
					connection.close(); 
					
					LOGGER.log(Level.INFO, "Cerrada conexión con éxito.");
					
				} catch (SQLException e2) {

					ConciliationExceptionHandler.throwConciliationExceptionVO(e2);
					
				} catch (Exception e3) {

					ConciliationExceptionHandler.throwConciliationExceptionVO(e3);   		
				}
		}
		
		LOGGER.log(Level.INFO, "<-- getDatosOperacionesBD()");
		
		return hmOperacionesBD; 
		
	}
}
