package com.bs.opt.conciliador.vo;

import java.util.ArrayList;
import com.bs.opt.conciliador.jaxb2.vo.request.TipoDatosOperacion;

public class ResultConciliationVO {

	public ResultConciliationVO() {
		// TODO Auto-generated constructor stub
	}

	ArrayList<TipoDatosOperacion> arrayListOK = new ArrayList<TipoDatosOperacion>();
	
	ArrayList<TipoDatosOperacion> arrayListKO = new ArrayList<TipoDatosOperacion>();

	public ArrayList<TipoDatosOperacion> getArrayListOK() {
		return arrayListOK;
	}

	public void setArrayListOK(ArrayList<TipoDatosOperacion> arrayListOK) {
		this.arrayListOK = arrayListOK;
	}

	public ArrayList<TipoDatosOperacion> getArrayListKO() {
		return arrayListKO;
	}

	public void setArrayListKO(ArrayList<TipoDatosOperacion> arrayListKO) {
		this.arrayListKO = arrayListKO;
	}
	
	 
	
}
