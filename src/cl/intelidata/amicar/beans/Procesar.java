package cl.intelidata.amicar.beans;

import cl.intelidata.amicar.bd.Proceso;
import cl.intelidata.conf.Configuracion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Procesar {
	/*******************************************************************************************************/
	/********************************************** Atributos **********************************************/
	/*******************************************************************************************************/

	protected Proceso proceso;

	private static Logger logger = LoggerFactory.getLogger(Procesar.class);

	/*******************************************************************************************************/
	/******************************************** Constructores ********************************************/
	/*******************************************************************************************************/

	public Procesar() {

	}

	public Procesar(Proceso proceso) {
		this.proceso = proceso;
	}

	/*******************************************************************************************************/
	/*********************************************** Metodos ***********************************************/
	/*******************************************************************************************************/

	public void guardarCotizacion() {
		try {
			cl.intelidata.amicar.db.ConsultasDB consultasDB = new cl.intelidata.amicar.db.ConsultasDB();
			java.util.Date fecha = new java.util.Date();
			java.sql.Timestamp time = new java.sql.Timestamp(fecha.getTime());
			this.proceso.setFechaClickLink(time);
			consultasDB.updateProceso(this.proceso);
			this.mailEjecutivo(this.proceso);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void mailEjecutivo(Proceso proceso) {
		try {
			Archivo archivo = new Archivo(Configuracion.getInstance().getInitParameter("SalidaEmmesaging"), Configuracion.getInstance().getInitParameter("SalidaEmmesaging"));
			java.util.List<String> mail = new java.util.ArrayList<String>();
			mail.add(
					cl.intelidata.amicar.referencias.Texto.LLAVE_INICIO + "|" + proceso.getEjecutivos().getCorreoEjecutivo() + "|" + proceso.getClientes().getRutCliente() + "|"
					+ proceso.getEjecutivos().getLocales().getNombreLocal() + "|" + proceso.getClientes().getEmailCliente() + "|" + proceso.getVendedores().getNombreVendedor() + "|"
					+ proceso.getFechaClickLink() + "|");
			archivo.guardarLista(mail, "Ejecutivos", "txt");

			logger.info("Mail id: " + proceso.getIdProceso() + " cliente id: " + proceso.getClientes().getRutCliente() + " ejecutivo id: " + proceso.getEjecutivos().getIdEjecutivo()
					+ "  vendedor id: " + proceso.getVendedores().getIdVendedor() + " fecha envio: " + proceso.getFechaEnvio() + " fecha click: " + proceso.getFechaClickLink());
		} catch (Exception e) {
			logger.error("Error al generar la data para email. \nCausado por:\n " + e.getMessage());
		}

	}
}
