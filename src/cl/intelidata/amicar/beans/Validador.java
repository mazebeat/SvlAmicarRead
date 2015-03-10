package cl.intelidata.amicar.beans;

import cl.intelidata.amicar.bd.Proceso;

public class Validador {
	/*******************************************************************************************************/
	/********************************************** Atributos **********************************************/
	/**
	 * ***************************************************************************************************
	 */

	private boolean procesoValido    = false;
	private boolean cotizacionLeida  = false;
	private Proceso procesoPrincipal = new Proceso();

	/*******************************************************************************************************/
	/******************************************** Constructores ********************************************/
	/**
	 * ***************************************************************************************************
	 */
	public Validador(String iProcesoID) {
		try {
			MCrypt mcrypt = new MCrypt();
			String id = new String(mcrypt.decrypt(iProcesoID)).trim();
			if (id != null) {
				this.asignarProceso(Integer.parseInt(id));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*******************************************************************************************************/
	/*********************************************** Metodos ***********************************************/
	/**
	 * ***************************************************************************************************
	 */

	private void asignarProceso(int iProcesoID) {
		cl.intelidata.amicar.db.ConsultasDB consultasDB = new cl.intelidata.amicar.db.ConsultasDB();
		Proceso proceso = consultasDB.procesoActivo(iProcesoID);
		this.setProcesoPrincipal(proceso);

		if ((this.getProcesoPrincipal() != null)) {
			this.setProcesoValido(true);
			if (proceso.getFechaClickLink() != null) {
				this.setCotizacionLeida(true);
			}
		}
	}

	/*******************************************************************************************************/
	/****************************************** Getters y Setters ******************************************/
	/**
	 * ***************************************************************************************************
	 */

	public void setProcesoValido(boolean procesoValido) {
		this.procesoValido = procesoValido;
	}

	public boolean isProcesoValido() {
		return procesoValido;
	}

	public boolean isCotizacionLeida() {
		return cotizacionLeida;
	}

	public void setCotizacionLeida(boolean cotizacionLeida) {
		this.cotizacionLeida = cotizacionLeida;
	}

	public Proceso getProcesoPrincipal() {
		return procesoPrincipal;
	}

	public void setProcesoPrincipal(Proceso procesoPrincipal) {
		this.procesoPrincipal = procesoPrincipal;
	}

}
