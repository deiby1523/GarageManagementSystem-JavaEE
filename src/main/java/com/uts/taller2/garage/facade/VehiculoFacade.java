package com.uts.taller2.garage.facade;


import com.uts.taller2.garage.model.Vehiculo;
import com.uts.taller2.garage.persistence.VehiculoDAO;
import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Fachada para operaciones sobre vehículos.
 * Deben agregarse reglas de negocio antes de llamar al DAO.
 */
@Stateless
public class VehiculoFacade {

    private final String[] allowedColors = {"Rojo", "Azul", "Verde", "Gris"};
    @Resource(lookup = "jdbc/garageDB")
    private DataSource ds;

    /**
     * Lista todos los vehículos.
     * Debe documentar excepciones si se agregan reglas.
     */
    public List<Vehiculo> listar() throws SQLException {
        try (Connection con = ds.getConnection()) {
            VehiculoDAO dao = new VehiculoDAO(con);
            return dao.listar();
        }
    }

    /**
     * Busca vehículo por ID. Manejar errores en llamada.
     */
    public Vehiculo buscarPorId(int id) throws SQLException {
        try (Connection con = ds.getConnection()) {
            VehiculoDAO dao = new VehiculoDAO(con);
            return dao.buscarPorId(id);
        }
    }

    /**
     * Agrega vehículo. Debe validar con reglas de negocio antes de agregar.
     * Por ejemplo, no agregar si la placa ya existe, si propietario está
     * vacío, etc.
     */
    public void agregar(Vehiculo v) throws SQLException {
        try (Connection con = ds.getConnection()) {
            VehiculoDAO dao = new VehiculoDAO(con);
            // Validaciones de negocio
            validatePlaca(v.getPlaca(), dao);
            validateBrand(v.getMarca());
            validateModel(v.getModelo());
            validateColor(v.getColor());
            validateOwner(v.getPropietario());

            v.setPlaca(v.getPlaca().toUpperCase());
            v.setMarca(v.getMarca().toUpperCase());
            v.setModelo(v.getModelo().toUpperCase());
            v.setColor(v.getColor().toUpperCase());
            v.setPropietario(v.getPropietario().toUpperCase());

            // Sí pasa todas las validaciones, guardar en BD
            dao.agregar(v);
        }
    }

    /**
     * Actualiza vehículo; incluir reglas de negocio.
     */
    public void actualizar(Vehiculo v) throws SQLException {
        try (Connection con = ds.getConnection()) {
            VehiculoDAO dao = new VehiculoDAO(con);

            Vehiculo vehiculoActual = buscarPorId(v.getId());

            if (vehiculoActual != null) {
                // Validaciones de negocio
                if (v.getPlaca() != null && !v.getPlaca().trim().isEmpty()) {
                    validatePlaca(v.getPlaca(), dao);
                    vehiculoActual.setPlaca(v.getPlaca().toUpperCase());
                }
                if (v.getMarca() != null && !v.getMarca().trim().isEmpty()) {
                    validateBrand(v.getMarca());
                    vehiculoActual.setMarca(v.getMarca().toUpperCase());
                }
                if (v.getModelo() != null && !v.getModelo().trim().isEmpty()) {
                    validateModel(v.getModelo());
                    vehiculoActual.setModelo(v.getModelo().toUpperCase());
                }
                if (v.getColor() != null && !v.getColor().trim().isEmpty()) {
                    validateColor(v.getColor());
                    vehiculoActual.setColor(v.getColor().toUpperCase());
                }
                if (v.getPropietario() != null && !v.getPropietario().trim().isEmpty()) {
                    validateOwner(v.getPropietario());
                    vehiculoActual.setPropietario(v.getPropietario().toUpperCase());
                }

                // Guardar cambios en BD
                dao.actualizar(vehiculoActual);
            } else {
                throw new SQLException("Vehiculo no encontrado");
            }
        }
    }

    /**
     * Elimina vehículo por ID
     */
    public void eliminar(int id) throws SQLException {
        try (Connection con = ds.getConnection()) {
            VehiculoDAO dao = new VehiculoDAO(con);
            //Buscar vehiculo en BD
            Vehiculo v = dao.buscarPorId(id);
            //Validar si el vehículo es del administrador
            if ("Administrador".equalsIgnoreCase(v.getPropietario())) {
                throw new SQLException("El vehiculo del administrador no se puede eliminar");
            }
            //Si pasa la validación eliminar
            dao.eliminar(id);
        }
    }

    private void validatePlaca(String placa, VehiculoDAO dao) throws SQLException {
        // Validar: no nulo ni vacío
        if (placa == null || placa.trim().isEmpty()) {
            throw new SQLException("La placa no puede estar vacía");
        }

        // Validar: inyección SQL
        validateNoSQLInjection(placa);

        // Validar: mínimo 3 caracteres
        if (placa.trim().length() < 3) {
            throw new SQLException("La placa debe tener al menos 3 caracteres");
        }

        // Validar: placa no duplicada en BD
        if (dao.existePlaca(placa)) {
            throw new SQLException("La placa " + placa + " ya está registrada");
        }
    }

    private void validateColor(String colorVehiculo) throws SQLException {
        // Validar: no nulo ni vacío
        if (colorVehiculo == null || colorVehiculo.trim().isEmpty()) {
            throw new SQLException("El color no puede estar vacío");
        }

        // Validar: inyección SQL
        validateNoSQLInjection(colorVehiculo);

        // Validar: color debe estar dentro de la lista permitida
        boolean colorValido = false;
        for (String color : allowedColors) {
            if (color.equalsIgnoreCase(colorVehiculo)) {
                colorValido = true;
                break;
            }
        }

        // Lanzar excepción si el color no es válido
        if (!colorValido) {
            throw new SQLException("Color no permitido. Colores permitidos: Rojo, Azul, Verde, Gris");
        }
    }

    private void validateOwner(String propietario) throws SQLException {
        // Validar: no nulo ni vacío
        if (propietario == null || propietario.trim().isEmpty()) {
            throw new SQLException("El propietario no puede estar vacío");
        }

        // Validar: inyección SQL
        validateNoSQLInjection(propietario);

        // Validar: mínimo 5 caracteres
        if (propietario.trim().length() < 5) {
            throw new SQLException("El propietario debe tener al menos 5 caracteres");
        }
    }

    private void validateModel(String modelo) throws SQLException {
        // Validar: no nulo ni vacío
        if (modelo == null || modelo.trim().isEmpty()) {
            throw new SQLException("El modelo no puede estar vacío");
        }

        // Validar: inyección SQL
        validateNoSQLInjection(modelo);

        // Validar: mínimo 3 caracteres
        if (modelo.trim().length() < 3) {
            throw new SQLException("El modelo debe tener al menos 3 caracteres");
        }

        try {
            int modeloInt = Integer.parseInt(modelo);
            int currentYear = LocalDate.now().getYear();

            // Validar: no mayor al año actual
            if (modeloInt > currentYear) {
                throw new SQLException("El modelo no puede ser mayor al año actual");
            }

            // Validar: máximo 20 años de antigüedad
            if (currentYear - modeloInt > 20) {
                throw new SQLException("El vehículo es muy antiguo, no se permiten modelos con más de 20 años");
            }

        } catch (NumberFormatException e) {
            throw new SQLException("El modelo debe ser un año válido. Ejemplo: 2014");
        }
    }

    private void validateBrand(String marca) throws SQLException {
        // Validar: no nulo ni vacío
        if (marca == null || marca.trim().isEmpty()) {
            throw new SQLException("La marca no puede estar vacía");
        }

        // Validar: inyección SQL
        validateNoSQLInjection(marca);

        // Validar: mínimo 3 caracteres
        if (marca.trim().length() < 3) {
            throw new SQLException("La marca debe tener al menos 3 caracteres");
        }
    }

    /**
     * Válida una entrada de usuario para detectar patrones típicos de SQL Injection.
     *
     * <p>Esta función busca palabras clave y caracteres que suelen usarse en ataques
     * de inyección SQL (SELECT, DROP, UNION, comentarios '--', punto y coma ';',  etc.).
     * Si detecta un patrón sospechoso lanza SQLException.</p>
     *
     * @param input la cadena de entrada a validar (puede ser null)
     * @throws SQLException si detecta un patrón sospechoso
     */
    private void validateNoSQLInjection(String input) throws SQLException {
        if (input == null) return;

        /*
         * Regex explicada:
         * (?i)             -> ignorar mayúsculas/minúsculas
         * \\b(...|...)\\b  -> detecta palabra completa (SELECT, INSERT, UPDATE, DELETE, DROP, UNION, OR, AND)
         * |(--|;|'|\\*|=)  -> o símbolos comunes en inyección: comentario SQL, punto y coma, asterisco, igual
         */
        String regex = "(?i)(\\b(SELECT|INSERT|UPDATE|DELETE|DROP|UNION|OR|AND)\\b|--|;|\\*|=)";

        if (input.matches(".*" + regex + ".*")) {
            throw new SQLException("Entrada inválida: posible intento de SQL Injection");
        }
    }


}
