//---------------------------------------------------------------------------------------------------------------------------//

Este repositorio contiene la implementación de un sistemas distribuidos utilizando Java RMI (Remote Method Invocation). 

//---------------------------------------------------------------------------------------------------------------------------//


3. Editor de Texto Compartido Distribuido

El último ejercicio consiste en un editor de texto distribuido en el que varios usuarios pueden modificar el contenido de un documento en línea de manera colaborativa. Las principales características son:

- Interfaz remota definida por TextEditorService, que permite ver, editar y guardar el documento de manera colaborativa.
- El servidor gestiona un documento mediante un StringBuilder que puede ser modificado por los clientes.
- Los clientes pueden insertar, eliminar o actualizar texto en el documento compartido.
- Se han implementado mecanismos de sincronización para evitar conflictos cuando varios clientes intentan modificar el texto simultáneamente.
