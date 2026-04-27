### 10. Decisiones de Diseño

#### Records, clase abstracta y interfaz

Utilizo recods para entidades inmutables como Libro, Ebook, Préstamo, Sanción y Transacción. ¿Por qué? Por ejmplo: el libro no cambiará de autor o de título, y el préstamo no cambiará de fecha.

Utilizo clase abstracta Socio para Estudiante y Docente, ya que comparten atributos e implementación de métodos, variando únicamente el límite de préstamos. Haberlas hecho clases independientes hubiera implicado duplicar toda esa lógica común.

Utilizo interfaz Recurso para Libro y Ebook, porque solo comparten atributos, no métodos.

#### Optionals

Utilizado por ejemplo en fechaDevoluciónReal en Préstamo, porque será null mientras el libro no haya sido devuelto. El Optional obliga a manejar explícitamente el caso null, eliminando NullPointerException en tiempo de ejecución.

#### JSON

Al cargar la aplicación, cada repositorio lee su archivo y mantiene los datos en un Map en memoria. Ante cualquier modificación, reescribe el archivo entero. Por el alcance del proyecto y el tamaño que pueden llegar a tomar los archivos, evito la implementación de busqueda de posición y modificación parcial del archivo. El costo de reescritura es despresciable en estos niveles. Gson singleton para centralizar la configuración y no crear instancias redundantes.

#### Sanciones

Sistema de 3 strikes, las primeras tres veces que se devuelve con demora, si el socio se demora n días, por n días tras la devolución quedará sancionando, bloqueando posibles preśtamos. Ya a la 4ta demora, dehabilitación por un mes.