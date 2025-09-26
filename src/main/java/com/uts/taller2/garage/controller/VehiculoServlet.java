package com.uts.taller2.garage.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Controlador web para la gestión de vehículos.
 * Recibe peticiones HTTP y las traduce en operaciones CRUD.
 * Debe mostrar mensajes claros en error de negocio.
 */

@WebServlet("/vehiculos")
public class VehiculoServlet extends HttpServlet {
    /**
     * Maneja solicitudes GET para listar vehículos o mostrar formulario.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    /**
     * Maneja solicitudes POST para realizar operaciones CRUD sobre los vehículos.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
