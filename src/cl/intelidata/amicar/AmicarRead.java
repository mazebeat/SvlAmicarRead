package cl.intelidata.amicar;

import cl.intelidata.amicar.bd.Proceso;
import cl.intelidata.amicar.db.ConsultasDB;
import cl.intelidata.utils.Texto;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AmicarRead extends HttpServlet {

	private static final long             serialVersionUID = 8462255193613302939L;
	public static        Logger           logger           = LoggerFactory.getLogger(AmicarRead.class);
	public static        SimpleDateFormat sdf              = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	/**
	 * Constructor of the object.
	 */
	public AmicarRead() {
		super();

	}

	public static void main(String args[]) {
		System.out.println("PASO");
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 * <p/>
	 * This method is called when a form has its tag value method equals to get.
	 *
	 * @param request  the request send by the client to the server
	 * @param response the response send by the server to the client
	 *
	 * @throws ServletException if an error occurred
	 * @throws IOException      if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			if ((request.getParameter(Texto.CLIENTE) != null) && (request.getParameter(Texto.COTIZACION) != null)) {
				String cotizacion = request.getParameter(Texto.COTIZACION);
				String cliente = request.getParameter(Texto.CLIENTE);
				Proceso proceso = new Proceso();
				Date fecha = new Date();
				Timestamp time = new Timestamp(fecha.getTime());
				System.out.println("*************************************** " + time);

				if (cliente != null && cotizacion != null) {
					proceso = this.procesoCliente(cliente, cotizacion);
					if (proceso != null) {
						if (proceso.getFechaAperturaMail() == null) {
							proceso.setFechaAperturaMail(time);
							this.actualizarProceso(proceso);
						}
					}
					cl.intelidata.utils.MCrypt mcrypt = new cl.intelidata.utils.MCrypt();
					String cli = new String(mcrypt.decrypt(cliente));
					String cot = new String(mcrypt.decrypt(cotizacion));

					logger.info(String.format("Registrando lectura %s - %s - %s", cli, cot, sdf.format(fecha)));
					LogLecturas.info(cliente + "|" + cotizacion + "|" + sdf.format(fecha));

				}
			} else {
				logger.warn("No se encontraron parametros de entrada");
			}

			this.returnImage(response);
		} catch (Exception e) {
			logger.error("Error al leer imagen lectura", e);
		}

	}

	private Proceso procesoCliente(String strCliente, String strCotizacion) {
		Proceso proceso = null;
		try {
			cl.intelidata.utils.MCrypt mcrypt = new cl.intelidata.utils.MCrypt();
			String cli = new String(mcrypt.decrypt(strCliente)).trim();
			String cot = new String(mcrypt.decrypt(strCotizacion)).trim();

			if (cli != null && cot != null) {
				ConsultasDB consultasDB = new ConsultasDB();
				proceso = consultasDB.procesoActivo(Integer.parseInt(cot));
			}
		} catch (Exception e) {
			logger.error("Error ", e);
		}

		return proceso;
	}

	private void actualizarProceso(Proceso proceso) {
		try {
			ConsultasDB consultasDB = new ConsultasDB();
			consultasDB.updateProceso(proceso);
		} catch (Exception e) {
			logger.error("Problemas al actualizar la base de datos\nCausado por:\n" + e.getMessage());
		}
	}

	private void returnImage(HttpServletResponse response) throws Exception {
		InputStream in = null;
		try {
			response.setContentType("image/png");

			in = this.getClass().getResourceAsStream("blank.png");
			ServletOutputStream bufferSalida = response.getOutputStream();
			IOUtils.copy(in, bufferSalida);
		} catch (Exception e) {
			logger.error("Error al retornar imagen" + e.getMessage(), e);
		} finally {
			in.close();
		}
	}

	/**
	 * The doPost method of the servlet. <br>
	 * <p/>
	 * This method is called when a form has its tag value method equals to
	 * post.
	 *
	 * @param request  the request send by the client to the server
	 * @param response the response send by the server to the client
	 *
	 * @throws ServletException if an error occurred
	 * @throws IOException      if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the POST method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
