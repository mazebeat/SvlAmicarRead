package cl.intelidata.amicar.db;

import cl.intelidata.amicar.bd.*;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class ConsultasDB {

	public ConsultasDB() {

	}

	/*******************************************************************************************************/
	/************************************* Metodos de Consulta *********************************************/
	/**
	 * ***************************************************************************************************
	 */

	@SuppressWarnings("unchecked")
	public Clientes clienteActivo(Integer idCliente) {
		Clientes cliente = null;
		try {
			List<Clientes> clientes = new ArrayList<Clientes>();
			Query query = EntityManagerHelper.createQuery("SELECT c FROM Clientes c WHERE c.idCliente = :idCliente");
			query.setParameter("idCliente", idCliente);
			clientes = query.getResultList();
			for (Clientes c : clientes) {
				cliente = c;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			EntityManagerHelper.closeEntityManager();
		}
		return cliente;
	}

	public void updateCliente(Clientes cliente) {
		try {
			ClientesDAO clientesDAO = new ClientesDAO();
			EntityManagerHelper.beginTransaction();
			clientesDAO.update(cliente);
			EntityManagerHelper.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			EntityManagerHelper.closeEntityManager();
		}
	}
}
