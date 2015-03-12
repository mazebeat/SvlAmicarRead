package cl.intelidata.amicar;

import cl.intelidata.amicar.beans.Procesar;
import cl.intelidata.amicar.beans.Validador;
import cl.intelidata.amicar.referencias.Texto;
import cl.intelidata.conf.Configuracion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

public class AmicarCotizacion extends HttpServlet {

	private static final long             serialVersionUID = 8462255193613302949L;
	public static        Logger           logger           = LoggerFactory.getLogger(AmicarCotizacion.class);
	public static        SimpleDateFormat sdf              = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	/**
	 * Constructor of the object.
	 */
	public AmicarCotizacion() {
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
			if ((request.getParameter(Texto.COTIZACION) != null)) {
				Validador validador = new Validador(request.getParameter(Texto.COTIZACION));

				if (validador.isProcesoValido() && !validador.isCotizacionLeida()) {
					System.out.println(Texto.MENSAJE_CLIENTE);
					logger.info(Texto.MENSAJE_CLIENTE);

					Procesar procesar = new Procesar(validador.getProcesoPrincipal());
					procesar.guardarCotizacion();
				} else if (validador.isProcesoValido() && validador.isCotizacionLeida()) {
					System.out.println(Texto.MENSAJE_PROCESO_OK);
					logger.info(Texto.MENSAJE_PROCESO_OK, 10);
				} else {
					System.out.println(Texto.MENSAJE_PROCESO_INVALIDO);
					logger.info(Texto.MENSAJE_PROCESO_INVALIDO, 10);
					opt = "A";
				}
			} else {
				logger.error("No se encontraron parametros de entrada");
				opt = "A";
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			logger.error("Error al procesar Cotizacion", e);
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
					site = site.concat(request.getQueryString());
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
		response.setStatus(response.SC_MOVED_PERMANENTLY); // SC_MOVED_TEMPORARILY //SC_MOVED_PERMANENTLY
		response.setHeader("Location", site);
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
