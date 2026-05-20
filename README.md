# Plantilla Android Banking - <TEST_YLINO>

Esta es una plantilla Android moderna, escalable y reutilizable orientada a aplicaciones financieras, siguiendo los principios de Clean Architecture y Jetpack Compose.

## Arquitectura

La aplicación sigue una arquitectura de capas (Clean Architecture):

- **Core**: Contiene elementos compartidos como manejo de errores, estados de UI, dispatchers y wrappers de resultados.
- **Domain**: Contiene la lógica de negocio pura (Modelos, Repositorios e Interfases de Casos de Uso). No tiene dependencias de Android.
- **Data**: Implementaciones concretas de los repositorios, Mappers, DTOs y el servicio de API (mockeado en este caso).
- **Presentation**: Capa de UI usando Jetpack Compose, ViewModels con StateFlow para el estado y SharedFlow para eventos one-shot (navegación).

## Flujo de Datos

`Compose UI -> ViewModel -> UseCase -> Repository -> Data Source (Mock API)`

## Tecnologías Utilizadas

- **Kotlin** y Corrutinas.
- **Jetpack Compose** para la UI.
- **Material 3** Design.
- **Navigation Compose** para la navegación entre pantallas.
- **ViewModel** y **Lifecycle Compose**.
- **StateFlow** para el estado de la UI y **SharedFlow** para eventos.
- **JUnit 4** y **Kotlinx-coroutines-test** para pruebas unitarias.

## Cómo correr la app

1. Abrir el proyecto en Android Studio (Ladybug o superior recomendado).
2. Sincronizar Gradle.
3. Ejecutar en un emulador o dispositivo físico (Min SDK 26).

## Cómo correr los tests

- Ejecutar `./gradlew test` desde la terminal o correr los tests directamente desde el IDE en la carpeta `src/test`.

## Decisiones Técnicas

- **DI Manual**: Se implementó una inyección de dependencias manual simple mediante un `GenericViewModelFactory` para mantener el proyecto ligero pero preparado para una fácil migración a Hilt/Koin.
- **Manejo de Errores**: Se utiliza una `sealed class DomainError` para tipar los errores y permitir que la UI reaccione específicamente a cada tipo de fallo.
- **Estados de UI**: Se usa una `sealed interface UiState` genérica para estandarizar los estados de Carga, Éxito y Error.
- **Eventos One-shot**: La navegación se maneja a través de un `SharedFlow` en el ViewModel para evitar problemas de recomposición o eventos duplicados.

## Reemplazo del Mock por API Real

Para conectar una API real:
1. Agregar la dependencia de Retrofit/Ktor en `build.gradle.kts`.
2. Crear la interfaz del servicio en la capa `data/remote`.
3. Actualizar `ProductRepositoryImpl` para recibir el nuevo servicio en lugar de `MockProductApiService`.
4. El resto de las capas (`Domain` y `Presentation`) permanecerán intactas gracias al desacoplamiento.
