# Guía Completa para Probar SmartWallet con Postman

Esta guía detalla paso a paso cómo probar cada uno de los endpoints de **SmartWallet** aplicando **Arquitectura Hexagonal**, seguridad **JWT** y la regla de negocio de **Validación de Presupuesto Excedido**.

---

## 1. Pasos Previos y Puesta en Marcha

### Paso 1.1: Base de Datos PostgreSQL
Asegúrate de que el servicio de PostgreSQL esté activo y crea la base de datos `smartwallet_db` (si aún no existe):
```sql
CREATE DATABASE smartwallet_db;
```
*(Opcional)*: Puedes ejecutar el archivo [`database.sql`](database.sql) en tu cliente de base de datos (pgAdmin, DBeaver o psql) para inicializar las tablas y datos de prueba.

### Paso 1.2: Iniciar la Aplicación Spring Boot
Inicia la clase `SmartWalletApplication` desde tu IDE (IntelliJ IDEA) o desde la terminal:
```bash
cd SmartWallet
./mvnw spring-boot:run
```
Verifica en la consola que la aplicación arranca en el puerto `8080`:
```text
Tomcat started on port 8080 (http) with context path '/'
SmartWalletApplication started in ... seconds
```

---

## 2. Configuración en Postman

1. **Variables de entorno (Recomendado):**
   * `baseUrl`: `http://localhost:8080`
   * `token`: (se llena automáticamente con el login)
2. **Encabezados comunes:**
   * `Content-Type`: `application/json`
3. **Peticiones Protegidas:**
   * En la pestaña **Authorization**, selecciona **Type:** `Bearer Token`.
   * En el campo **Token**, coloca `{{token}}` o pega el token JWT obtenido tras el login.

---

## 3. Flujo de Pruebas Paso a Paso

### 1️⃣ Autenticación: Login de Administrador
* **Método:** `POST`
* **URL:** `http://localhost:8080/api/v1/auth/login`
* **Body (raw JSON):**
```json
{
  "username": "admin",
  "password": "admin123"
}
```
* **Script automático para guardar el Token (Pestaña "Scripts -> Post-response" en Postman):**
```javascript
const res = pm.response.json();
if (res.results && res.results.token) {
    pm.environment.set("token", res.results.token);
    console.log("Token guardado con éxito");
}
```
* **Respuesta Exitosa (200 OK):**
```json
{
  "message": "Autenticación exitosa",
  "results": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "tipo": "Bearer",
    "username": "admin",
    "rol": "ADMIN"
  }
}
```

---

### 2️⃣ Categorías

#### A. Listar Categorías Existentes
* **Método:** `GET`
* **URL:** `http://localhost:8080/api/v1/categorias`
* **Authorization:** `Bearer Token` (`{{token}}`)
* **Respuesta (200 OK):** Devuelve la lista de categorías (Alimentación, Transporte, Servicios, Entretenimiento).

#### B. Crear Nueva Categoría
* **Método:** `POST`
* **URL:** `http://localhost:8080/api/v1/categorias`
* **Authorization:** `Bearer Token` (`{{token}}`)
* **Body (raw JSON):**
```json
{
  "nombre": "Educación y Libros",
  "descripcion": "Cursos, certificaciones, libros universitarios y talleres"
}
```

---

### 3️⃣ Presupuestos

#### A. Listar Todos los Presupuestos
* **Método:** `GET`
* **URL:** `http://localhost:8080/api/v1/presupuestos`
* **Authorization:** `Bearer Token` (`{{token}}`)
* **Respuesta (200 OK):**
```json
{
  "message": "Presupuestos recuperados: 3",
  "results": [
    {
      "id": 1,
      "montoLimite": 400.00,
      "montoActual": 120.00,
      "disponible": 280.00,
      "categoria": {
        "id": 1,
        "nombre": "Alimentación"
      },
      "fechaInicio": "2026-11-01",
      "fechaFin": "2026-11-30"
    }
  ]
}
```

#### B. Crear un Presupuesto para Nueva Categoría
* **Método:** `POST`
* **URL:** `http://localhost:8080/api/v1/presupuestos`
* **Authorization:** `Bearer Token` (`{{token}}`)
* **Body (raw JSON):**
```json
{
  "categoriaId": 5,
  "montoLimite": 250.00,
  "fechaInicio": "2026-11-01",
  "fechaFin": "2026-11-30"
}
```

#### C. Evaluar si un Monto Entra en el Presupuesto (Sin Guardar)
* **Método:** `POST`
* **URL:** `http://localhost:8080/api/v1/presupuestos/evaluar?categoriaId=1&monto=100.00`
* **Authorization:** `Bearer Token` (`{{token}}`)
* **Respuesta (200 OK):**
```json
{
  "message": "El monto $100.00 está dentro del presupuesto permitido.",
  "results": "DISPONIBLE"
}
```

#### D. Actualizar Límite de Presupuesto
* **Método:** `PUT`
* **URL:** `http://localhost:8080/api/v1/presupuestos/1/limite?nuevoLimite=500.00`
* **Authorization:** `Bearer Token` (`{{token}}`)

---

### 4️⃣ Gastos (Prueba Clave de la Lógica de Negocio)

#### CASO 1: Registrar Gasto Válido (Dentro de Presupuesto)
La categoría 1 (`Alimentación`) tiene límite `$400.00` y gastado `$120.00` (disponible `$280.00`).  
Registramos un gasto de `$50.00`:
* **Método:** `POST`
* **URL:** `http://localhost:8080/api/v1/gastos`
* **Authorization:** `Bearer Token` (`{{token}}`)
* **Body (raw JSON):**
```json
{
  "descripcion": "Compra semanal de frutas y verduras",
  "monto": 50.00,
  "moneda": "USD",
  "fecha": "2026-11-08T10:00:00",
  "categoriaId": 1
}
```
* **Respuesta (201 Created):**
```json
{
  "message": "Gasto registrado exitosamente",
  "results": {
    "id": 5,
    "descripcion": "Compra semanal de frutas y verduras",
    "monto": 50.00,
    "moneda": "USD",
    "fecha": "2026-11-08T10:00:00",
    "categoria": {
      "id": 1,
      "nombre": "Alimentación"
    }
  }
}
```
*(El presupuesto ahora tiene gastado `$170.00` y disponible `$230.00`).*

---

#### CASO 2: 🔥 PRUEBA DE REGLA DEL DOMINIO -> `PresupuestoExcedidoException`
Ahora intentamos registrar un gasto de `$800.00` en la categoría 1 (disponible: `$230.00`):
* **Método:** `POST`
* **URL:** `http://localhost:8080/api/v1/gastos`
* **Authorization:** `Bearer Token` (`{{token}}`)
* **Body (raw JSON):**
```json
{
  "descripcion": "Banquete gourmet exclusivo",
  "monto": 800.00,
  "moneda": "USD",
  "fecha": "2026-11-08T10:30:00",
  "categoriaId": 1
}
```
* **Respuesta Esperada (400 Bad Request):**
```json
{
  "message": "Operación rechazada: Presupuesto excedido para la categoría 'Alimentación'. Límite: 400.00, Gastado actual: 170.00, Nuevo gasto: 800.00. Excedente proyectado: 570.00",
  "results": null
}
```
* **Consola de la aplicación:**
Observarás la traza del `LoggingAspect` auditando el intento y `NotificacionEmailAdapter` emitiendo la alerta crítica por email.

---

#### CASO 3: Gasto Multimoneda (Consulta API Externa en Vivo)
Registramos un gasto en Euros (`EUR`). El sistema usa `TipoCambioHttpAdapter` para consultar la tasa en vivo y convertir a la moneda base:
* **Método:** `POST`
* **URL:** `http://localhost:8080/api/v1/gastos`
* **Authorization:** `Bearer Token` (`{{token}}`)
* **Body (raw JSON):**
```json
{
  "descripcion": "Software internacional en Europa",
  "monto": 15.00,
  "moneda": "EUR",
  "fecha": "2026-11-08T11:00:00",
  "categoriaId": 3
}
```

---

#### CASO 4: Listar Gastos
* **Listar todos:** `GET http://localhost:8080/api/v1/gastos`
* **Filtrar por categoría:** `GET http://localhost:8080/api/v1/gastos?categoriaId=1`
* **Consultar gasto por ID:** `GET http://localhost:8080/api/v1/gastos/1`

---

### 5️⃣ Gestión de Usuarios (Rol ADMIN)

* **Listar usuarios:**
  `GET http://localhost:8080/api/v1/usuario`
* **Crear nuevo usuario con rol USER:**
  * **Método:** `POST`
  * **URL:** `http://localhost:8080/api/v1/usuario`
  * **Authorization:** `Bearer Token` (`{{token}}`)
  * **Body (raw JSON):**
  ```json
  {
    "username": "dario_user",
    "password": "user1234",
    "rol": "USER"
  }
  ```

---

## 4. Swagger / OpenAPI UI
También puedes probar e interactuar con todos los endpoints y schemas de la API visualmente desde el navegador:
* **URL:** `http://localhost:8080/swagger-ui/index.html`
* Haz clic en el botón verde **Authorize** arriba a la derecha y pega el token JWT (`eyJ...`) para probar cualquier endpoint directamente.

