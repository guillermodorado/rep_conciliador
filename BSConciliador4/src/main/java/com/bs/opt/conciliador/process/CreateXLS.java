package com.bs.opt.conciliador.process;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import com.bs.opt.conciliador.jaxb2.vo.request.TipoDatosOperacion;
import com.bs.opt.conciliador.utilities.ConciliationExceptionHandler;
import com.bs.opt.conciliador.vo.ConciliationExceptionVO;
import com.bs.opt.conciliador.vo.ResultConciliationVO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by veronica.valenzuela on 13/01/2017.
 */
public class CreateXLS {

    private Workbook wb = null;
    
    private CreationHelper helper = null;
    
    private ResourceBundle rb = null;

    private final static Logger LOGGER = Logger.getLogger(CreateXLS.class.getName());
    
    public CreateXLS() {
		super();
		// TODO Auto-generated constructor stub
		
	    wb = new HSSFWorkbook();
	    
	    helper = wb.getCreationHelper();
	    
	    rb = ResourceBundle.getBundle("config");
	}

	public void toXLS(ResultConciliationVO resultConciliation) throws ConciliationExceptionVO {

		LOGGER.log(Level.INFO, "--> toXLS()");
		
		try {
			
	        Sheet sheetOK = wb.createSheet("OK");
	        Sheet sheetKO = wb.createSheet("KO");

	        crearSheet(resultConciliation.getArrayListOK(), sheetOK);
	        crearSheet(resultConciliation.getArrayListKO(), sheetKO);

	        //Volcamos la salida a un fichero excel.	        
	        String volcadoFicheroExcel = rb.getString("bs.opt.xls.fichero.ruta").trim() + rb.getString("bs.opt.xls.fichero.nombre").trim();
	        LOGGER.log(Level.INFO, "Volcamos la salida en un fichero excel: <" + volcadoFicheroExcel + ">");
	        FileOutputStream fileOut = new FileOutputStream(new File(volcadoFicheroExcel));
	        wb.write(fileOut);
	        fileOut.close(); 
	        
	        LOGGER.log(Level.INFO, "<-- toXLS()");
	        
		} catch (FileNotFoundException e) {

			ConciliationExceptionHandler.throwConciliationExceptionVO(e);  
			
		} catch (IOException e) {
			
			ConciliationExceptionHandler.throwConciliationExceptionVO(e);  			
		}
    }

    private void crearSheet(ArrayList<TipoDatosOperacion> arrayListDatos, Sheet hoja) throws ConciliationExceptionVO {
    	
    	try {
    		
            crearCabecera(hoja);
            crearBody(arrayListDatos, hoja);

            LOGGER.log(Level.INFO, "Creada la hoja para las operaciones: <" + hoja.getSheetName() + ">");
            
    	} catch (Exception e) {
    		
    		ConciliationExceptionHandler.throwConciliationExceptionVO(e);  
    	}
    }

    private void crearCabecera(Sheet hoja) throws ConciliationExceptionVO {
    	
    	try {
    	
    		String nomCampoIdentOper     = rb.getString("bs.opt.xls.campo.mostrar.idenOper").trim();
    		String nomCampoContrapartida = rb.getString("bs.opt.xls.campo.mostrar.contrapartida").trim();
    		
            Row row;
            int i = 0;
            row = hoja.createRow(0); 
            
            row.createCell(i).setCellValue( helper.createRichTextString(nomCampoIdentOper));i++;
            row.createCell(i).setCellValue( helper.createRichTextString(nomCampoContrapartida));i++;
            
    	} catch (Exception e) {
    		
    		ConciliationExceptionHandler.throwConciliationExceptionVO(e);  
    	}
    }

    private void crearBody(ArrayList<TipoDatosOperacion> arrayListDatos, Sheet hoja) throws ConciliationExceptionVO {
        
    	try {
    		
            for (int i=0; i<arrayListDatos.size(); i++) {
           	 
            	TipoDatosOperacion operacionVO = arrayListDatos.get(i);
            	String valorCampoNumOperacion     = operacionVO.getIdentificadorOperacion();
            	String valorCampoContrapartidaBIC = operacionVO.getContrapartida().getContrapartidaBIC();
            	
                Row row = hoja.createRow(i+1);
                row.createCell(0).setCellValue(helper.createRichTextString(valorCampoNumOperacion));
                row.createCell(1).setCellValue(helper.createRichTextString(valorCampoContrapartidaBIC));
            }
    		
    	} catch (Exception e) {
    		
    		ConciliationExceptionHandler.throwConciliationExceptionVO(e);  
    	}
    }
}
