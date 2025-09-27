/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.uts.taller2.garage.facade;

/**
 * Fachada que expone los métodos básicos. Sólo incluye el paso directo (sin
 * reglas), para que los estudiantes implementen las reglas de negocio en esta
 * clase.
 */
import com.uts.taller2.garage.model.Vehiculo;
import com.uts.taller2.garage.model.VehiculoDAO;
import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Fachada para operaciones sobre vehículos. Deben agregarse reglas de negocio
 * antes de llamar al DAO.
 */
@Stateless
public class VehiculoFacade {

    @Resource(lookup = "jdbc/myPool")
    private DataSource ds;

    /**
     * Lista todos los vehículos. Debe documentar excepciones si se agregan
     * reglas.
     */
    public List<Vehiculo> listar() throws SQLException {
        try (Connection con = ds.getConnection()) {
            VehiculoDAO dao = new VehiculoDAO(con);
            return dao.listar();
        }
    }

    /**
     * Busca vehículo por id. Manejar errores en llamada.
     */
    public Vehiculo buscarPorId(int id) throws SQLException {
        try (Connection con = ds.getConnection()) {
            VehiculoDAO dao = new VehiculoDAO(con);
            return dao.buscarPorId(id);
        }
    }

    /**
     * Agrega vehículo. Debe validar con reglas de negocio antes de agregar. Por
     * ejemplo, no agregar si la placa ya existe, si propietario está vacío,
     * etc.
     */
    public void agregar(Vehiculo v) throws SQLException {
        try (Connection con = ds.getConnection()) {
            VehiculoDAO dao = new VehiculoDAO(con);
            dao.agregar(v);
        }
    }

    /**
     * Actualiza vehículo; incluir reglas de negocio.
     */
    public void actualizar(Vehiculo v) throws SQLException {
        try (Connection con = ds.getConnection()) {
            VehiculoDAO dao = new VehiculoDAO(con);
            dao.actualizar(v);
        }
    }

    /**
     * Elimina vehículo por id.
     */
    public void eliminar(int id) throws SQLException {
        try (Connection con = ds.getConnection()) {
            VehiculoDAO dao = new VehiculoDAO(con);
            dao.eliminar(id);
        }
    }
}
