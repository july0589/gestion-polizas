
# API de Gestión de Pólizas

Proyecto desarrollado con Spring Boot para administrar pólizas y sus riesgos.

## Tecnologías

- Java 21
- Spring Boot
- Maven
- Spring Data JPA
- Base de datos H2

## Requisitos

- JDK 21
- Git

## Configuración

La aplicación utiliza la variable de entorno `API_KEY` para proteger los endpoints mediante la cabecera `x-api-key`.

En PowerShell, antes de iniciar la aplicación:

```powershell
$env:API_KEY = "123456"
```

## Ejecutar la aplicación

Desde la carpeta raíz del proyecto:

```powershell
.\mvnw.cmd spring-boot:run
```

La API estará disponible en `http://localhost:8080`.

## Endpoints implementados

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/polizas` | Listar las pólizas por tipo y estado |
| GET | `/polizas/{id}/riesgos` | Consultar riesgos de una póliza |
| POST | `/polizas/{id}/renovar` | Renovar una póliza |
| POST | `/polizas/{id}/cancelar` | Cancelar una póliza |
| POST | `/polizas/{id}/riesgos` | Agregar un riesgo a una póliza colectiva |
| POST | `/riesgos/{id}/cancelar` | Cancelar un riesgo |
| POST | `/core-mock/evento` | Registrar en logs un intento de envío al CORE |

## Seguridad

Las peticiones deben incluir la cabecera:

```http
x-api-key: 123456
```

La clave se configura mediante la variable de entorno `API_KEY`.

## Ejemplo: consultar pólizas

```powershell
$headers = @{ "x-api-key" = "123456" }

Invoke-RestMethod `
  -Method Get `
  -Uri "http://localhost:8080/polizas" `
  -Headers $headers
```

## Ejemplo: enviar un evento al CORE simulado

```powershell
$headers = @{ "x-api-key" = "123456" }

$body = @{
    evento = "ACTUALIZACION"
    polizaId = 555
} | ConvertTo-Json

Invoke-RestMethod `
  -Method Post `
  -Uri "http://localhost:8080/core-mock/evento" `
  -Headers $headers `
  -ContentType "application/json" `
  -Body $body
```
