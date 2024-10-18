# Descripción general del microservicio
 
Este Microservicio es una API RESTful encargado de gestionar los usuarios y construida con **Spring Boot** que implementa seguridad mediante **JWT (JSON Web Tokens)**.
Proporciona operaciones CRUD para gestionar recursos y servicios protegidos.


## Pre requisitos

- [IntelliJ IDEA 2024.2.3](https://www.jetbrains.com/idea/download).
- [Gradle 8.10.2](https://gradle.org/).
- [Java 11.0.23 2024-04-16 LTS](https://www.oracle.com/co/java/technologies/javase/jdk11-archive-downloads.html).
- [H2 Database](http://www.h2database.com/html/download.html).

## API Reference

| Endpoint                | Method | Auth? | Description                                                    |
|-------------------------|--------|-------|----------------------------------------------------------------|
| `/api/user`             | POST   | NO    | Creacion del usuario.                                          |
| `/api/user{id}`         | GET    | Yes   | Consulta por id y devuelve la informacion del usario.          |
| `/api/user`             | GET    | Yes   | Consulta por objeto User y devuelve la informacion del usario. |
| `/api/user{id}`         | PUT    | Yes   | Actualiza la información del usuario por id.                   |
| `/api/user/disable{id}` | DELETE | Yes   | Deshabilita el estado(isActive) del usuario con su id.         |
| `/api/user/enable{id}`  | DELETE | Yes   | Habilita el estado(isActive) del usuario con su id.            |
| `/api/auth/login`       | POST   | No    | Genera un nuevo token.                                         |


## Modelo de datos

A continuación mostramos las tablas utilizadas en el servicio.

| Table    | Description                                       |
|----------|---------------------------------------------------|
| `user`   | Tabla para almacenar los usuarios.                |
| `phones` | Tabla para almacenar los telefonos del usuario.   |

## Configuración y despliegue local

**Clonar el repositorio**:
1. Abrir la consola y ejecutar:
    - git clone https://github.com/pruebanisum/user.git
    - cd user

**Para desplegar localmente este servicio se debe abrir la consola y ejececutar los siguientes comados:**

1. ./gradlew clean
2. ./gradlew openApiGenerate
3. ./gradlew build
4. Desplegar el proyecto, dando clic derecho en el main UserApplication y seleccionar RUN o DEBUG.

La API estará disponible en: http://localhost:8080

## Despliegue Interfaz Base de Datos
Para abrir la interfaz de la base de datos ir  a la ruta y fijar los datos siguiendo los siguientes pasos:
1. http://localhost:8080/h2-console 
2. Saved Settings: Generic H2(Embedded)
3. Settings Name: Generic H2(Embedded)
4. Driver Class: org.h2.Driver
5. JDBC URL: jdbc:h2:mem:testdb
6. User Name: sa
7. Password:(dejar vacío)
8. Connect

## Probar los Servicios
Para probar los servicios realiza lo siguiente:
1. Puedes utilizar la herramienta Postman. Descargar e instalar.
   - [Postman Version 7.36.7](https://www.postman.com/downloads/).
2. Cargar en Postmam la colecciones.
   - Colección de Usuario (resources/postman/User Nisum.postman_collection.json)
   - Colección de Login (resources/postman/Login User Nisum.postman_collection.json)
3. Para tener mas información de los endpoints ir a:
   - Archivo yaml swagger (resources/swagger/api.yaml)

## Seguridad con JWT
El sistema de autenticación utiliza JSON Web Tokens (JWT). Para acceder a los endpoints protegidos, primero deberás autenticarte(email,password) para obtener un token, luego debes incluir ese token en los encabezados de tus solicitudes.

## Contribución y Licencia
   - Autor: Henry Ruiz