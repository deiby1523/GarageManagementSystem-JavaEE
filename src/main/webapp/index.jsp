<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Gestión de Vehículos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="container mt-4">

<!--
    /**
     * Página JSP para la gestión de vehículos.
     * - Muestra los vehículos en una tabla usando JSTL.
     * - Contiene un formulario para añadir nuevos vehículos.
     * - Recibe atributos desde el servlet: "vehiculos", "error", "mensaje".
     * - Usa Bootstrap 5 para diseño responsivo y estilo.
     */
-->

<h1 class="mb-4 text-center">Gestión de Vehículos</h1>

<!-- Mostrar error si existe -->
<c:if test="${not empty error}">
    <div class="alert alert-danger" role="alert">
            ${error}
    </div>
</c:if>

<!-- Mostrar notificación de marca ferrari -->
<c:if test="${not empty notification}">
    <div class="alert alert-success" role="alert">
            ${notification}
    </div>
</c:if>

<!-- Mostrar mensaje de éxito -->
<c:if test="${not empty mensaje}">
    <div class="alert alert-success" role="alert">
            ${mensaje}
    </div>
</c:if>

<div class="row">
    <!-- Tabla de vehículos -->
    <div class="col-md-8">
        <h2>Listado</h2>

        <c:if test="${not empty vehiculos}">
            <table class="table table-striped table-bordered">
                <thead class="table-dark">
                <tr>
                    <th>ID</th>
                    <th>Placa</th>
                    <th>Marca</th>
                    <th>Modelo</th>
                    <th>Color</th>
                    <th>Propietario</th>
                    <th>Opciones</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="vehiculo" items="${vehiculos}">
                    <tr>
                        <td>${vehiculo.id}</td>
                        <td>${vehiculo.placa}</td>
                        <td>${vehiculo.marca}</td>
                        <td>${vehiculo.modelo}</td>
                        <td>${vehiculo.color}</td>
                        <td>${vehiculo.propietario}</td>
                        <td>
                            <form action="${pageContext.request.contextPath}/vehiculos?id=${vehiculo.id}" method="post"
                                  style="display:inline;">
                                <input type="hidden" name="_method" value="DELETE">
                                <button type="submit" class="btn btn-danger"
                                        onclick="return confirm('¿Eliminar este vehículo?');">Eliminar
                                </button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:if>

        <c:if test="${empty vehiculos}">
            <div class="alert alert-warning" role="alert">
                No hay vehículos registrados.
            </div>
        </c:if>
    </div>


    <div class="col-md-4">
        <h2>Registrar Vehículo</h2>
        <form action="vehiculos" method="post" class="border rounded p-3 shadow-sm bg-light">
            <!-- acción -->
            <input type="hidden" name="_method" id="_method" value="POST">

            <!-- Formulario para añadir vehículo -->
            <div class="mb-3">
                <label for="id" class="form-label">ID (SOLO PARA EDITAR)</label>
                <input type="text" class="form-control" id="id" name="id">
            </div>
            <div class="mb-3">
                <label for="placa" class="form-label">Placa</label>
                <input type="text" class="form-control" id="placa" name="placa">
            </div>
            <div class="mb-3">
                <label for="marca" class="form-label">Marca</label>
                <input type="text" class="form-control" id="marca" name="marca">
            </div>
            <div class="mb-3">
                <label for="modelo" class="form-label">Modelo</label>
                <input type="text" class="form-control" id="modelo" name="modelo">
            </div>
            <div class="mb-3">
                <label for="color" class="form-label">Color</label>
                <input type="text" class="form-control" id="color" name="color">
            </div>
            <div class="mb-3">
                <label for="propietario" class="form-label">Propietario</label>
                <input type="text" class="form-control" id="propietario" name="propietario">
            </div>
            <button type="submit" class="btn btn-primary w-100" onclick="setMethod('POST')">Añadir Vehículo</button>
            <button type="submit" class="btn btn-warning w-100 mt-2" onclick="setMethod('EDIT')">Editar Vehículo
            </button>
        </form>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function setMethod(method) {
        document.getElementById('_method').value = method;
    }
</script>
</body>
</html>
