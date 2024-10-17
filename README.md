//---------------------------------------------------------------------------------------------------------------------------------------------------------/////
Este repositorio contiene la implementación de tres sistemas distribuidos utilizando Java RMI (Remote Method Invocation). Cada uno de los ejercicios propone un reto relacionado con la creación de servicios distribuidos, en los que varios clientes interactúan con un servidor central para compartir recursos o realizar operaciones concurrentes. A continuación, se ofrece una descripción general de cada uno de los ejercicios implementados:

1. Chat Distribuido usando RMI
Este ejercicio consiste en la implementación de un chat simple distribuido que permite a múltiples usuarios enviar y recibir mensajes en tiempo real. Las principales características incluyen:

Interfaz remota definida por ChatService, que permite enviar y recibir mensajes entre los usuarios conectados.
El servidor almacena los mensajes en una lista y los distribuye entre los clientes.
Los clientes, una vez conectados, pueden enviar mensajes y recibir actualizaciones del chat en tiempo real.
Se han implementado mecanismos básicos para gestionar las entradas y salidas de usuarios, así como la finalización de la sesión del cliente.
2. Sistema Distribuido de Voto Online
Este ejercicio plantea la creación de un sistema de votación en línea donde los usuarios pueden votar por diferentes candidatos de manera distribuida. El sistema está diseñado para gestionar múltiples clientes de forma segura, evitando problemas de sincronización. Las funcionalidades clave incluyen:

Interfaz remota definida por VotingService, que permite votar, consultar resultados y gestionar candidatos y sus votos.
Implementación de un sistema de conteo de votos basado en un diccionario (Map<String, Integer>).
Funcionalidades para añadir votos, consultar los resultados actuales, eliminar votos y candidatos, y obtener información detallada sobre la votación en curso.
3. Editor de Texto Compartido Distribuido
El último ejercicio consiste en un editor de texto distribuido en el que varios usuarios pueden modificar el contenido de un documento en línea de manera colaborativa. Las principales características son:

Interfaz remota definida por TextEditorService, que permite ver, editar y guardar el documento de manera colaborativa.
El servidor gestiona un documento mediante un StringBuilder que puede ser modificado por los clientes.
Los clientes pueden insertar, eliminar o actualizar texto en el documento compartido.
Se han implementado mecanismos de sincronización para evitar conflictos cuando varios clientes intentan modificar el texto simultáneamente.
