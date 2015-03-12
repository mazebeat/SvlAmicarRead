package cl.intelidata.amicar;

import cl.intelidata.amicar.bd.Clientes;
import cl.intelidata.amicar.db.ConsultasDB;
<<<<<<< HEAD:src/cl/intelidata/amicar/AmicarRead.java
=======
import cl.intelidata.conf.Configuracion;
>>>>>>> f129541cf06bfdd4476a736c0a7e198f7d0754b1:src/cl/intelidata/amicar/AmicarDesinscritos.java
import cl.intelidata.utils.MCrypt;
import cl.intelidata.utils.Texto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AmicarDesinscritos extends HttpServlet {

	private static final long             serialVersionUID = 8462255193613302949L;
	public static        Logger           logger           = LoggerFactory.getLogger(AmicarDesinscritos.class);
	public static        SimpleDateFormat sdf              = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	/**
	 * Constructor of the object.
	 */
	public AmicarDesinscritos() {
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
		String opt = "L";

		try {
<<<<<<< HEAD:src/cl/intelidata/amicar/AmicarRead.java
			String cotizacion = request.getParameter(Texto.COTIZACION);
			String cliente = request.getParameter(Texto.CLIENTE);
			Proceso proceso = new Proceso();
			Date fecha = new Date();
			Timestamp time = new Timestamp(fecha.getTime());
			System.out.println("*************************************** " + time);

			if (cliente != null && cotizacion != null) {

				cl.intelidata.utils.MCrypt mcrypt = new cl.intelidata.utils.MCrypt();
				String cli = new String(mcrypt.decrypt(cliente));
				String cot = new String(mcrypt.decrypt(cotizacion));

				proceso = this.procesoCliente(cliente, cotizacion);

				if (proceso != null) {
					if (proceso.getFechaAperturaMail() == null) {
						proceso.setFechaAperturaMail(time);
						logger.info("Actualizando proceso", proceso);
						this.actualizarProceso(proceso);

						logger.info(String.format("Registrando lectura %s - %s - %s", cli, cot, sdf.format(fecha)));
					} else {
						logger.info("Proceso ya leido", proceso);
					}
				} else {
					logger.info("No se encontro proceso BDD");
				}

				LogLecturas.info(cliente + "|" + cotizacion + "|" + sdf.format(fecha));
=======
			if (request.getParameter(Texto.CLIENTE) != null && request.getParameter(Texto.COTIZACION) != null) {
				Clientes cliente = new Clientes();
				Date fecha = new Date();
				Timestamp time = new Timestamp(fecha.getTime());
				System.out.println("*************************************** " + time);
				logger.info("Procesando solicitud");
				cliente = this.procesoDescarteCliente(request.getParameter(Texto.CLIENTE), request.getParameter(Texto.COTIZACION));

				MCrypt mcrypt = new cl.intelidata.utils.MCrypt();
				String cli = new String(mcrypt.decrypt(request.getParameter(Texto.CLIENTE)));
				String cot = new String(mcrypt.decrypt(request.getParameter(Texto.COTIZACION)));

				if (cliente != null) {
					if (!cliente.getDesinscrito()) {
						cliente.setDesinscrito(true);
						logger.info("Actualizando cliente", cliente);

						this.actualizarCliente(cliente);

						logger.info(String.format("Registrando lectura %s - %s - %s", cli, cot, sdf.format(fecha)));
					} else {
						logger.info("Cliente ya desinscrito", cliente);
					}
				} else {
					logger.info("No se ha encontrado cliente");
				}

				logger.info(request.getParameter(Texto.CLIENTE) + "|" + request.getParameter(Texto.COTIZACION) + "|" + sdf.format(fecha));

>>>>>>> f129541cf06bfdd4476a736c0a7e198f7d0754b1:src/cl/intelidata/amicar/AmicarDesinscritos.java
			} else {
				logger.warn("No se encontraron parametros de entrada");
				opt = "A";
			}

		} catch (Exception e) {
			logger.error("Error al procesar cliente inscrito", e);
			opt = "A";
		}

		this.redirect(request, response, opt);
	}

	public void redirect(HttpServletRequest request, HttpServletResponse response, String opt) {
		response.setContentType("text/html");
		String site = "http://www.amicar.cl";

		if (opt.equalsIgnoreCase("L")) {
			try {
				logger.info("Obteniendo URL Landing desde archivo properties");
				site = Configuracion.getInstance().getInitParameter("dominioLanding");

				if (!site.trim().endsWith("?")) {
					site = site.trim().concat("?");
				}

				if (request.getQueryString() != null) {
					logger.info("Redireccionando hacia Landing");

					MCrypt mcrypt = new MCrypt();
					String des = MCrypt.bytesToHex(mcrypt.encrypt("desinscrito"));
					site = site.concat(request.getQueryString()).concat("&action=").concat(des);
				} else {
					logger.info("Redireccionando hacia Amicar");
					logger.warn("Parametros nulos cambiando URL hacia Amicar");
					site = "http://www.amicar.cl";
				}
			} catch (Exception e) {
				logger.error("Error al redireccionar.", e);
			}
		} else {
			logger.info("Redireccionando hacia Amicar");
		}

		logger.info("Redireccionando a " + site);
		response.setStatus(response.SC_MOVED_PERMANENTLY); // SC_MOVED_TEMPORARILY | SC_MOVED_PERMANENTLY
		response.setHeader("Location", site);
	}

	private Clientes procesoDescarteCliente(String strCliente, String strCotizacion) {
		Clientes cliente = null;
		try {
			MCrypt mcrypt = new MCrypt();
			String cli = new String(mcrypt.decrypt(strCliente)).trim();
			String cot = new String(mcrypt.decrypt(strCotizacion)).trim();

			if (cli != null && cot != null) {
				ConsultasDB consultasDB = new ConsultasDB();
				cliente = consultasDB.clienteActivo(Integer.parseInt(cli));
			}
		} catch (Exception e) {
			logger.error("Problemas al procesar el cliente\nCausado por:\n" + e.getMessage());
		}

		return cliente;
	}

	private void actualizarCliente(Clientes cliente) {
		try {
			ConsultasDB consultasDB = new ConsultasDB();
			consultasDB.updateCliente(cliente);
		} catch (Exception e) {
			logger.error("Problemas al actualizar la base de datos\nCausado por:\n" + e.getMessage());
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
