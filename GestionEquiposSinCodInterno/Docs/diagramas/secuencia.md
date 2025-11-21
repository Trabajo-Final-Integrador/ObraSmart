# Diagrama de Secuencia – Alta de Equipo

Propuesta de escenario para el diagrama de secuencia:

1. El **Usuario** interactúa con el **Frontend Angular** y envía el formulario de alta de equipo.  
2. El frontend realiza una llamada HTTP al **API Gateway**.  
3. El API Gateway enruta la petición al microservicio **GestionEquipos**.  
4. El **EquipoController** recibe la solicitud y delega en el **EquipoService**.  
5. El **EquipoService** valida datos y utiliza el **EquipoRepository** para persistir la entidad.  
6. La respuesta viaja de vuelta al frontend con el resultado de la operación.

Se recomienda modelar este flujo en Draw.io y exportar como `secuencia-alta-equipo.png`.
