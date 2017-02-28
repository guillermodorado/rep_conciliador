package com.bs.opt.conciliador.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javax.xml.datatype.XMLGregorianCalendar;

public class ConciliationValidator {

	public ConciliationValidator() {
		// TODO Auto-generated constructor stub
	}

	public static boolean fechaCreacionXML(XMLGregorianCalendar fechaCreacionXML) {
		
		boolean fechasIguales = false;
		
		ResourceBundle rb = ResourceBundle.getBundle("config");
		String formatoFechaXML = rb.getString("bs.opt.xml.formato.fecha.creacion").trim();
		
		SimpleDateFormat formateador = new SimpleDateFormat(formatoFechaXML);
		
		//obtenemos la fecha actual: Date -> String
		Date dateFechaActual = new Date();
		String strFechaActual = formateador.format(dateFechaActual);
		
		//obtenemos la fecha del xml: XMLGregorianCalendar -> Date -> String
		
		Date dateFechaCreacion = fechaCreacionXML.toGregorianCalendar().getTime();
		String strFechaCreacion = formateador.format(dateFechaCreacion);
		
		//las comparamos para ver si son iguales
		if (strFechaCreacion != null && strFechaActual != null) {
			
			if (strFechaCreacion.equalsIgnoreCase(strFechaActual)) { 
				
				fechasIguales = true;
			}
		}
		
		//return fechasIguales;
		
		return true;
	}
}
