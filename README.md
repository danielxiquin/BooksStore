
# Bookstore

Este proyecto es una solución de inventario para una librería, desarrollado en **Java** utilizando un **árbol B** como estructura de datos. Los archivos **CSV** se procesan en formato **JSON** para realizar operaciones sobre el inventario, como agregar, actualizar y eliminar libros. El proyecto también permite buscar libros en el árbol y generar un archivo `.txt` con los resultados.

## Funcionalidades

### Clases principales:

- **BTreeNode**: Representa un nodo en el árbol B. Cada nodo puede contener múltiples libros (almacenados como objetos JSON) y referencias a nodos hijos.
  - **Método `insertNonFull(JSONObject book)`**: Inserta un libro en un nodo que aún no está lleno. Si el nodo es una hoja, lo agrega directamente; de lo contrario, recurre a los nodos hijos.
  - **Método `splitChild(int i, BTreeNode y)`**: Divide un nodo cuando se llena, creando un nuevo nodo y redistribuyendo los libros.

- **BTree**: Estructura principal que gestiona los nodos y permite operaciones de inserción, eliminación, y búsqueda de libros.
  - **Método `insert(JSONObject book)`**: Inserta un libro en el árbol, manejando el caso en que la raíz se llene y requiera división.
  - **Método `search(String name)`**: Busca libros por nombre dentro del árbol.

- **Gestión de archivos CSV**: Se utilizan funciones para leer archivos CSV y convertir cada entrada en un objeto JSON. A partir de estas entradas, se realizan operaciones como inserciones o eliminaciones en el árbol B.

### Operaciones soportadas:

- **Insertar libro**: El sistema lee un archivo CSV que contiene la información de los libros en formato JSON y los inserta en el árbol B.
- **Eliminar libro**: Se puede eliminar un libro del árbol utilizando su ISBN.
- **Actualizar libro**: Actualiza los atributos de un libro en función de las instrucciones del archivo CSV.
- **Buscar libros**: Realiza búsquedas de libros por nombre y genera un archivo `.txt` con los resultados de la búsqueda.

## Ejemplo de uso

1. **Archivo de entrada (CSV)**:
   ```csv
   INSERT; {"isbn":"1234567890","name":"Cien Años de Soledad","author":"Gabriel Garcia Marquez","category":"Ficción","price":"20.00","quantity":"10"}
   INSERT; {"isbn":"0987654321","name":"El Principito","author":"Antoine de Saint-Exupéry","category":"Ficción","price":"15.00","quantity":"5"}
   DELETE; {"isbn":"1234567890"}
   SEARCH; {"name":"El Principito"}
   ```

2. **Archivo de salida (TXT)**:
   ```txt
   {"isbn":"0987654321","name":"El Principito","author":"Antoine de Saint-Exupéry","category":"Ficción","price":"15.00","quantity":"5"}
   ```

## Ejecución local

### Pasos:

1. Clona el repositorio:
   ```bash
   git clone https://github.com/danielxiquin/Bookstore.git
   ```

2. Abre el proyecto en **Visual Studio Code** o tu entorno preferido para **Java**.

3. Asegúrate de tener **Java 11** o superior instalado.

4. Compila y ejecuta el código con los siguientes comandos:
   ```bash
   javac Main.java
   java Main
   ```

## Recomendaciones

1. **Mejora en la gestión de nodos llenos**: Actualmente, el método `splitChild` se utiliza cuando un nodo se llena. Sería conveniente implementar un mecanismo que revise el estado del árbol completo periódicamente para evitar que las operaciones de división se acumulen.

2. **Implementar pruebas unitarias**: Es recomendable desarrollar pruebas unitarias que cubran los casos de inserción, eliminación, y búsqueda, para asegurar la robustez del sistema.

3. **Optimización de búsquedas**: Podrías considerar agregar índices adicionales o técnicas de caché para mejorar el rendimiento de las búsquedas en grandes volúmenes de datos.

4. **Manejo de excepciones**: Es recomendable mejorar el manejo de errores y excepciones, especialmente al leer y procesar los archivos CSV, para evitar que errores de formato interrumpan el funcionamiento del sistema.
