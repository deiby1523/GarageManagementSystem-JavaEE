package com.uts.taller2.garage.controller;

import com.uts.taller2.garage.facade.VehiculoFacade;
import com.uts.taller2.garage.model.Vehiculo;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Controlador web para la gestión de vehículos.
 * Recibe peticiones HTTP y las traduce en operaciones CRUD.
 * Debe mostrar mensajes claros en error de negocio.
 */

@WebServlet("/vehiculos")
public class VehiculoServlet extends HttpServlet {

    @EJB
    private VehiculoFacade vehiculoFacade;
    private String error = "";
    private String notification = "";

    /**
     * Maneja solicitudes GET para listar vehículos o mostrar formulario.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            List<Vehiculo> vehiculos = vehiculoFacade.listar();
            req.setAttribute("vehiculos", vehiculos);

            if (!error.trim().isBlank()) {
                req.setAttribute("error", error);
            }
            if (!notification.trim().isBlank()) {
                req.setAttribute("notification", notification);
            }
            error = "";
            notification = "";
            req.getRequestDispatcher("/index.jsp").forward(req, res);
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/index.jsp").forward(req, res);
        }
    }

    /**
     * Maneja solicitudes POST para agregar los vehículos.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        //Obtener acción
        String method = req.getParameter("_method");
        //Validar acción
        if ("DELETE".equalsIgnoreCase(method)) {
            doDelete(req, res);
            return;
        }
        if ("EDIT".equalsIgnoreCase(method)) {
            doPut(req, res);
            return;
        }
        if ("POST".equalsIgnoreCase(method)) {
            //Si no se asume que la acción es guardar en la BD
            String placa = req.getParameter("placa");
            String marca = req.getParameter("marca");
            String modelo = req.getParameter("modelo");
            String color = req.getParameter("color");
            String propietario = req.getParameter("propietario");

            Vehiculo vehiculo = new Vehiculo();
            vehiculo.setPlaca(placa);
            vehiculo.setMarca(marca);
            vehiculo.setModelo(modelo);
            vehiculo.setColor(color);
            vehiculo.setPropietario(propietario);
            try {
                vehiculoFacade.agregar(vehiculo);
                if (("Ferrari").equalsIgnoreCase(marca)) {
                    notification = "Vaya que buen gusto. Haz agregado un Ferrari";
                }
                res.sendRedirect(req.getContextPath() + "/vehiculos");
            } catch (Exception e) {
                error = e.getMessage();
                res.sendRedirect(req.getContextPath() + "/vehiculos");

            }
        }

    }

    /**
     * Maneja las solicitudes PUT para editar vehículos.
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            String placa = req.getParameter("placa");
            String marca = req.getParameter("marca");
            String modelo = req.getParameter("modelo");
            String color = req.getParameter("color");
            String propietario = req.getParameter("propietario");

            Vehiculo vehiculo = new Vehiculo();
            vehiculo.setId(id);
            vehiculo.setPlaca(placa);
            vehiculo.setMarca(marca);
            vehiculo.setModelo(modelo);
            vehiculo.setColor(color);
            vehiculo.setPropietario(propietario);

            vehiculoFacade.actualizar(vehiculo);
            if (("Ferrari").equalsIgnoreCase(marca)) {
                notification = "Vaya que buen gusto. Haz agregado un Ferrari";
            }
            res.sendRedirect(req.getContextPath() + "/vehiculos");
        } catch (NumberFormatException numberFormatException) {
            error = "ID invalido";
            res.sendRedirect(req.getContextPath() + "/vehiculos");
        } catch (SQLException e) {
            error = e.getMessage();
            res.sendRedirect(req.getContextPath() + "/vehiculos");
        }
    }

    /**
     * Maneja solicitudes DELETE para eliminar los vehículos
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            vehiculoFacade.eliminar(id);
            res.sendRedirect(req.getContextPath() + "/vehiculos");
        } catch (NumberFormatException numberFormatException) {
            error = "ID invalido";
            res.sendRedirect(req.getContextPath() + "/vehiculos");
        } catch (SQLException e) {
            error = e.getMessage();
            res.sendRedirect(req.getContextPath() + "/vehiculos");
        }
    }
}
