package cl.intelidata.amicar.db;

import cl.intelidata.amicar.bd.EntityManagerHelper;
import cl.intelidata.amicar.bd.Proceso;
import cl.intelidata.amicar.bd.ProcesoDAO;

import javax.persistence.Query;

public class ConsultasDB {

	public ConsultasDB() {

	}

	/*******************************************************************************************************/
	/************************************* Metodos de Consulta *********************************************/
	/*******************************************************************************************************/

	@SuppressWarnings("unchecked")
	public Proceso procesoActivo(Integer iProcesoID) {
		Proceso proceso = null;
		try {
			java.util.List<cl.intelidata.amicar.bd.Proceso> procesos = new java.util.ArrayList<cl.intelidata.amicar.bd.Proceso>();
			Query query = EntityManagerHelper.createQuery("SELECT p FROM Proceso p WHERE p.idProceso = :idProceso");
			query.setParameter("idProceso", iProcesoID);
			procesos = query.getResultList();
			for (Proceso p : procesos) {
				proceso = p;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			EntityManagerHelper.closeEntityManager();
		}
		return proceso;
	}

	public void updateProceso(Proceso proceso) {
		try {
			ProcesoDAO procesoDAO = new ProcesoDAO();
			EntityManagerHelper.beginTransaction();
			procesoDAO.update(proceso);
			EntityManagerHelper.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			EntityManagerHelper.closeEntityManager();
		}
	}
}
