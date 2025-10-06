# 🚗 GarageManagementSystem – Java EE

**Proyecto:** Gestión de Vehículos — Aplicación CRUD con **Jakarta EE (Servlets + EJB Stateless + JDBC)** y vistas **JSP**.  
**Repositorio:** GitHub (trabajo en equipo, 3 integrantes).

---

## 📚 Índice

1. [Resumen](#1-resumen)  
2. [Arquitectura y componentes](#2-arquitectura-y-componentes)  
3. [Convenciones de nombres](#3-convenciones-de-nombres)  
4. [Reglas de negocio](#4-reglas-de-negocio)  
5. [Pre-requisitos](#5-pre-requisitos)  
6. [Configuración DB y DataSource](#6-configuración-db-y-datasource)  
7. [Cómo compilar y desplegar (NetBeans + GlassFish)](#7-cómo-compilar-y-desplegar-netbeans--glassfish)  
8. [Flujo Git recomendado (branches y MR)](#8-flujo-git-recomendado-branches-y-mr)  
9. [Autores](#9-autores)

---

## 1️⃣ Resumen

Aplicación web **Java EE** para la **gestión de vehículos** en un taller.  
Implementa una **arquitectura en capas** con entidades, DAO, fachadas y servlets para gestionar la persistencia, las reglas de negocio y los controladores.  

Admite operaciones **CRUD**, aplica **validaciones**, y proporciona **gestión estructurada de excepciones** con mensajes claros al usuario.

---

## 2️⃣ Arquitectura y componentes

**📊 Diagrama lógico (resumen):**

[Browser] ⇄ [Servlet (VehiculoServlet) / JSP]
⇄ [VehiculoFacade (EJB Stateless)]
⇄ [VehiculoDAO (JDBC)]
⇄ [MySQL]


### 🔹 Descripción de capas

| Capa | Componentes | Descripción |
|------|--------------|-------------|
| **Presentación (Web)** | `VehiculoServlet`, `index.jsp` | Controlador y vista. Recibe parámetros, redirige y muestra mensajes. |
| **Lógica de negocio (Facade)** | `VehiculoFacade` | Valida reglas de negocio antes del DAO. Lanza excepciones de negocio. |
| **Persistencia (DAO)** | `VehiculoDAO` | CRUD vía JDBC con `PreparedStatement`. |
| **Modelo** | `Vehiculo` (POJO) | Representa la entidad con getters/setters y JavaDoc. |
| **REST (opcional)** | `JakartaRestConfiguration` | Configuración base si se amplía el proyecto con endpoints REST. |

---

## 3️⃣ Convenciones de nombres

### 🧩 Paquetes
com.uts.taller2.garage.controller → Servlets y controladores
com.uts.taller2.garage.facade → EJB / lógica de negocio
com.uts.taller2.garage.persistence → DAOs
com.uts.taller2.garage.model → Entidades / POJOs


### 🧱 Clases
- `PascalCase`: `VehiculoServlet`, `VehiculoFacade`, `VehiculoDAO`, `Vehiculo`

### ⚙️ Métodos y variables
- `camelCase`: `listar()`, `buscarPorId()`, `agregar()`
- Constantes: `MAYUSCULAS_SEPARADAS_POR_GUION_BAJO`

### 🗄️ Base de datos
- Tablas y columnas: `snake_case`  
  Ejemplo: `vehicles`, columna `license_plate`

---

## 4️⃣ Reglas de negocio

Implementadas en `VehiculoFacade`:

1. 🚫 No permitir agregar vehículo con **placa duplicada**.  
2. 👤 **Propietario obligatorio** y mínimo **5 caracteres**.  
3. 🏷️ Marca, modelo y placa con **mínimo 3 caracteres**.  
4. 🎨 Color solo acepta: `Rojo`, `Blanco`, `Negro`, `Azul`, `Gris`.  
5. 📅 Modelo (año) **no mayor al actual** ni con más de **20 años de antigüedad**.  
6. 🔒 **Placas únicas** en toda la base (validadas en BD y DAO).  
7. ⛔ No eliminar si el propietario es `"Administrador"`.  
8. 🛠️ Solo actualizar si el vehículo **existe**.  
9. 🧩 Validar entradas para evitar **SQL Injection (simulado)**.  
10. 🏁 Si la marca es `"Ferrari"`, enviar **notificación simulada** en el servlet.

> Todas las reglas se documentan como comentarios dentro de `VehiculoFacade`.

---

## 5️⃣ Pre-requisitos

- ☕ **Java JDK 11+**
- 🧠 **NetBeans** (o IDE con soporte Jakarta EE)
- 🌐 **GlassFish 6+** o compatible con Jakarta EE 9/10  
- 🐬 **MySQL / MariaDB**
- 📦 **JSTL / Jakarta Taglibs** en `WEB-INF/lib` si el servidor no las incluye

---

## 6️⃣ Configuración DB y DataSource

### 6.1 🧾 Esquema mínimo (ejemplo)
```sql
CREATE DATABASE garage;
USE garage;

CREATE TABLE vehicles (
  id INT AUTO_INCREMENT PRIMARY KEY,
  brand VARCHAR(100) NOT NULL,
  model VARCHAR(100) NOT NULL,
  owner VARCHAR(100) NOT NULL,
  color VARCHAR(20),
  license_plate VARCHAR(20) NOT NULL UNIQUE
);
```

### 6.2 ⚙️ DataSource (GlassFish)

Crear JDBC Connection Pool (driver: com.mysql.cj.jdbc.Driver)

Crear JDBC Resource con JNDI: jdbc/garageDB

### Configurar credenciales y URL:

jdbc:mysql://localhost:3306/garage


### En GlassFish:
Resources → JDBC → JDBC Connection Pools → New...

---

##  7️⃣ Cómo compilar y desplegar (NetBeans + GlassFish)

Importar el proyecto en NetBeans.

Configurar GlassFish y verificar que esté en ejecución.

Crear el DataSource jdbc/garageDB como se explicó.

Ejecutar Clean and Build del proyecto.

Ejecutar o Deploy en GlassFish.

Abrir en el navegador:

http://localhost:8080/GarageManagementSystem-JavaEE/vehiculos

---

##  8️⃣ Flujo Git recomendado (branches y MR)
# 🌲 Ramas
| Tipo                | Convención              | Ejemplo                       |
| ------------------- | ----------------------- | ----------------------------- |
| Principal           | `main`                  | Rama estable                  |
| Nueva funcionalidad | `feature/<descripcion>` | `feature/validaciones-facade` |
| Corrección          | `fix/<descripcion>`     | `fix/error-eliminar-vehiculo` |

---

## 🔄 Flujo de trabajo
### Crear rama de trabajo
git checkout -b feature/validaciones-facade

### Agregar cambios
git add .

### Commit claro
git commit -m "feat: validaciones de placa y color en VehiculoFacade"

### Subir cambios
git push origin feature/validaciones-facade


### Luego, en GitHub o GitLab:

Crear Merge Request / Pull Request hacia main

Incluir descripción clara, pruebas realizadas y checklist de revisión.

 ---
 
## 👥 9️⃣ Autores
| Nombre                          | Correo                                                        |
| ------------------------------- | ------------------------------------------------------------- |
| **Jair Fabián Duarte**          | [jfabianduarte@uts.edu.co](mailto:jfabianduarte@uts.edu.co)   |
| **Deiby Fabián Prada**          | [dfabianprada@uts.edu.co](mailto:dfabianprada@uts.edu.co)     |
| **Armando Rafael Trespalacios** | [artrespalacios@uts.edu.co](mailto:artrespalacios@uts.edu.co) |
