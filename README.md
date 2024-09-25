
# Documentación de Laboratorio 1: Arboles B, B*, B+

---

## Descripción del Problema

La librería "Libros y Más" enfrenta un problema con la gestión de su inventario. Con miles de artículos, el sistema actual no es eficiente para realizar búsquedas, actualizaciones, o inserciones, lo que afecta la experiencia del cliente, especialmente en temporadas de alta demanda.

El objetivo de este laboratorio es implementar una estructura de datos eficiente que permita optimizar las operaciones de búsqueda, inserción, actualización y eliminación de registros de inventario, utilizando árboles B, B*, o B+.

---

## Requerimientos

Se requiere implementar las siguientes funcionalidades en la estructura de datos:

- **Insertar registro de artículo:** Ingresar un nuevo artículo en el inventario.
- **Eliminar registro de artículo:** Eliminar un artículo utilizando el ISBN como llave primaria.
- **Actualizar un registro:** Modificar atributos de un artículo utilizando el ISBN.
- **Buscar registros por nombre:** Permitir la búsqueda de todos los registros que coincidan con el nombre de un artículo.

---

## Implementación

### Árbol B

Se utilizó un Árbol B para representar la estructura de datos del inventario debido a su balanceo automático y su eficiencia en operaciones de búsqueda, inserción y eliminación. Los árboles B son adecuados para manejar grandes cantidades de datos, ya que minimizan el número de accesos al disco durante las operaciones.

- **Nodos internos y hojas:** Los nodos pueden contener múltiples llaves y apuntadores a otros nodos, lo que permite almacenar y organizar los datos de manera eficiente.
- **División de nodos:** Implementamos la lógica de división de nodos cuando un nodo alcanza su capacidad máxima, garantizando que el árbol se mantenga balanceado.
- **Búsqueda:** La búsqueda se realiza de manera eficiente dividiendo los datos en cada nivel del árbol hasta encontrar el artículo o determinar que no está presente.

### Operaciones

- **Insertar:** La operación de inserción se realiza buscando la posición correcta en el árbol y, si es necesario, dividiendo nodos para mantener el balance.
- **Eliminar:** La eliminación de un registro sigue una estrategia de fusión o redistribución de nodos si un nodo cae por debajo de su capacidad mínima.
- **Actualizar:** Para actualizar un artículo, primero se realiza una búsqueda del ISBN y luego se modifican los atributos especificados.
- **Buscar por nombre:** La búsqueda por nombre recorre los nodos para encontrar todos los registros que coincidan con el nombre del artículo.

---

## Ejemplo de Entrada

```csv
INSERT; {"isbn":"1234567890","name":"Cien Años de Soledad","author":"Gabriel Garcia Marquez","category":"Ficción","price":"20.00","quantity":"10"}
INSERT; {"isbn":"0987654321","name":"El Principito","author":"Antoine de Saint-Exupéry","category":"Ficción","price":"15.00","quantity":"5"}
PATCH; {"isbn":"0987654321","author":"Antoine de Saint-Exupéry","price":"18.00"}
DELETE; {"isbn":"1234567890"}
SEARCH; {"name":"Cien Años de Soledad"}
```

## Ejemplo de Salida

```json
{
  "isbn": "1234567890",
  "name": "Cien Años de Soledad",
  "author": "Gabriel Garcia Marquez",
  "category": "Ficción",
  "price": "20.00",
  "quantity": "10"
}
{
  "isbn": "0987654321",
  "name": "El Principito",
  "author": "Antoine de Saint-Exupéry",
  "category": "Ficción",
  "price": "18.00",
  "quantity": "5"
}
```

---

## Recomendaciones de Mejora

1. **Uso de índices adicionales:** Para mejorar aún más las búsquedas, se recomienda implementar un índice secundario para búsquedas por nombre, ya que actualmente las búsquedas por nombre recorren todo el árbol.
2. **Optimización de actualizaciones:** Implementar un sistema de caché podría acelerar las actualizaciones y reducir la carga sobre el sistema de inventario en picos de demanda.
3. **Pruebas unitarias:** Asegurarse de que cada funcionalidad esté cubierta por pruebas unitarias exhaustivas, incluyendo casos límite como la inserción en nodos llenos y la eliminación de nodos con solo una llave.
4. **Optimización del tamaño de nodo:** Dependiendo del entorno de producción, ajustar el tamaño de los nodos del árbol puede optimizar el rendimiento del árbol B en sistemas de disco.

---

## Archivo README.md

```md
# Inventario Libros y Más

Este proyecto es una implementación de un sistema de gestión de inventario optimizado utilizando Árboles B, B*, o B+. Permite realizar operaciones de búsqueda, inserción, eliminación y actualización de artículos en el inventario de una librería.

## Funcionalidades

- Insertar un nuevo artículo en el inventario.
- Eliminar artículos utilizando el ISBN.
- Actualizar atributos de un artículo.
- Buscar artículos por nombre.

## Requisitos

- Java 11 o superior.
- Archivo de entrada en formato `.csv` con las operaciones de inserción, actualización, eliminación y búsqueda.

## Uso

1. Ejecutar el programa con el archivo de entrada:

   ```bash
   java Main input.csv
   ```

2. El archivo de salida se generará con los resultados de las búsquedas.

## Recomendaciones

- Optimizar las búsquedas por nombre implementando un índice secundario.
- Considerar la implementación de caché para reducir la carga en picos de demanda.
- Ajustar el tamaño de los nodos del árbol según el entorno de producción para mejorar el rendimiento.
```

