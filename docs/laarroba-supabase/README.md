# Plan de migración de La Arroba a Supabase

Estado del documento: 10 de agosto de 2026.

Este documento es el punto de continuidad para la migración de autenticación
y datos de `laarroba-kmp` desde Firebase a Supabase, apoyándose en
`jchucomponents-supabase`. Debe permitir que otro agente retome el trabajo sin
depender del historial de la conversación.

No contiene secretos, claves de Supabase, credenciales SMTP ni datos personales
exportados de Firebase. Esos valores nunca deben incorporarse al repositorio.

## Repositorios implicados

| Repositorio | Responsabilidad |
| --- | --- |
| `jchucomponents` | Fachada KMP reutilizable de Supabase, persistencia configurable, Auth, PostgREST, RPC, Realtime y Edge Functions. |
| `laarroba-kmp` | Modelos, repositorios, ViewModels y reglas de dominio compartidas; UI Compose y SwiftUI; recepción de deep links. |
| Supascale | Proyecto Supabase, Auth, PostgreSQL, RLS, Storage, funciones y configuración de correo. Todos los pasos de consola deben adaptarse a Supascale. |

Rutas locales observadas al crear este documento:

```text
/Users/jesusmariacalderon/Documents/Repositorios/jchucomponents
/Users/jesusmariacalderon/Documents/Repositorios/laarroba-kmp
```

## Objetivo funcional

Mantener las capacidades actuales de La Arroba y hacerlas robustas y
multiplataforma:

1. Inicio de sesión con correo y contraseña.
2. Registro de usuario.
3. Registro orientado a comercio, con solicitud de una tienda existente o de
   una tienda nueva.
4. Confirmación obligatoria de correo mediante enlace y deep link.
5. Reenvío del correo de confirmación.
6. Recuperación y cambio de contraseña.
7. Restauración segura de sesión al arrancar Android e iOS.
8. Cierre de sesión y tratamiento de sesiones caducadas.
9. Migración de los perfiles y saldos de Firestore sin conservar el antiguo
   booleano `gdpr`.
10. Aceptación versionada de documentos legales desde cero.
11. Libro de movimientos de Elipis, no solo un saldo mutable.
12. Administración, restricciones por funcionalidad y posterior gestión de
    tiendas, planes y fidelización.

Firebase no se retirará del flujo productivo hasta completar la reconciliación
de datos y los criterios de salida descritos más abajo.

## Estado actual confirmado

### Supascale

- [x] El registro de usuarios está habilitado.
- [x] El proveedor de correo y contraseña está habilitado.
- [x] La confirmación de correo es obligatoria.
- [x] El redirect/deep link de autenticación se ha añadido en Supascale.
- [x] Los usuarios de Firebase Auth ya se han migrado a Supabase Auth, según
  el estado comunicado por el responsable del proyecto.
- [ ] Auditar que cada usuario migrado conserva una correspondencia inequívoca
  entre el UID de Firebase y el UUID de `auth.users`.
- [ ] Confirmar la política de contraseñas, rate limits, CAPTCHA y proveedores
  que realmente se utilizarán.
- [ ] Configurar SMTP y plantillas definitivas. Esta tarea se ha aplazado de
  forma explícita y no bloquea el diseño inicial de base de datos.

La configuración de Supascale, y no las rutas del panel de Supabase Cloud, es
la referencia para futuras instrucciones de consola.

### `jchucomponents`

La versión publicada `3.0.0-alpha10` sirve para una prueba básica, pero no es
el objetivo final para sustituir Firebase Auth. El árbol de trabajo contiene
el desarrollo de `3.0.0-alpha11` con:

- [x] Usuario de Auth tipado.
- [x] Estado observable de inicialización, sesión restaurada, autenticación,
  cierre y fallo de refresco.
- [x] Login y registro con resultados tipados.
- [x] Indicación de registro pendiente de confirmación de correo.
- [x] Reenvío de confirmación.
- [x] Solicitud y actualización de contraseña.
- [x] Procesamiento de callbacks PKCE/implicit y validación de scheme/host.
- [x] Refresco, restauración, limpieza local y distintos alcances de logout.
- [x] Errores de Auth tipados.
- [x] Inyección configurable de `SessionManager` y `CodeVerifierCache`.
- [x] Helpers de PostgREST RPC.
- [x] Opciones de idempotencia y correlación para Edge Functions.
- [x] Pruebas unitarias nuevas para modelos, errores, configuración y RPC.
- [x] Ejecutar la validación completa proporcional del módulo.
- [x] Revisar la API pública y resolver cualquier fallo detectado.
- [ ] Publicar `3.0.0-alpha11`.
- [ ] Validar `alpha11` desde un consumidor KMP externo Android/iOS.

Los cambios de `alpha11` están sin publicar y conviven con otros cambios del
usuario en el working tree. No se deben descartar, resetear ni sobrescribir.

Validación local completada el 10 de agosto de 2026:

```text
:jchucomponents-supabase:ktlintCheck
:jchucomponents-supabase:allTests
:jchucomponents-supabase:apiCheck
:jchucomponents-supabase:publishToMavenLocal
:app:compileDebugKotlin
```

Los targets Android, iOS y macOS compilan; los tests Android, iOS Simulator y
macOS ARM64 pasan. Los targets x64 se compilan y sus tests se omiten en el host
ARM64, como corresponde. Para evitar las dos instalaciones Kotlin/Native que
existen en el entorno local, la validación Apple se ejecutó con:

```text
KONAN_DATA_DIR=/Users/jesusmariacalderon/.konan
```

Además, la validación de callbacks ahora comprueba scheme/host sin sensibilidad
a mayúsculas, decodifica errores del fragmento y tiene pruebas comunes. Los
flujos de Auth vuelven a propagar la cancelación y clasifican fallos de red como
reintentables.

### `laarroba-kmp`

- [x] Existe una prueba Android `debug` de Supabase que no sustituye la sesión
  productiva de Firebase.
- [x] Android tiene cambios iniciales de manifiesto/navegación para deep link.
- [x] iOS tiene una entrada inicial en `Info.plist` para deep link.
- [x] Existe `docs/supabase-auth-migration.md` con la auditoría inicial de
  `alpha10`.
- [ ] La implementación productiva de Auth sigue duplicada en `androidMain` e
  `iosMain` y todavía utiliza Firebase.
- [ ] Los modelos, repositorio y ViewModel finales de Supabase deben trasladarse
  a `shared/commonMain`.
- [ ] Compose y SwiftUI deben consumir la misma lógica compartida.
- [ ] Las plataformas solo deben aportar recepción de URL y almacenamiento
  seguro cuando sea inevitable.

## Decisiones de arquitectura ya tomadas

### KMP

Los modelos, repositorios, casos de uso, ViewModels y estados de Auth vivirán
una sola vez en `shared/commonMain`. Android Compose y SwiftUI consumirán esa
misma lógica.

Solo quedará en código de plataforma:

- recepción del `Intent`/URL de autenticación;
- integración con Android Keystore o Apple Keychain si requiere adaptadores
  nativos;
- navegación y presentación Compose/SwiftUI.

No se creará otra implementación completa del repositorio en `androidMain` y
otra en `iosMain`.

### Identidad y autorización

- `auth.users.id` es el identificador UUID canónico del usuario.
- `public.profiles.id` utilizará exactamente ese mismo UUID como PK y FK.
- El UID de Firebase solo existirá como correspondencia de migración y no como
  identidad nueva.
- `user_metadata` puede transportar datos de presentación durante el registro,
  pero nunca roles, propiedad de tiendas ni autorización.
- Los roles administrativos, membresías, restricciones y permisos viven en
  tablas controladas por RLS y funciones del servidor.
- Una cuenta de comercio comienza siendo una cuenta Auth normal. No recibe
  permisos sobre una tienda hasta que su solicitud sea aprobada.

### Esquemas PostgreSQL

El nombre del esquema no decide por sí solo quién puede leer los datos:

- `auth` pertenece al sistema de autenticación. El cliente no consulta
  `auth.users` directamente; se administra desde Supascale o SQL con permisos.
- `public` es un esquema expuesto por la API, pero no significa acceso anónimo.
  `GRANT` y RLS deben autorizar cada operación.
- `private` no debe exponerse a PostgREST. Sus datos solo se usan mediante
  funciones restringidas o procesos administrativos.

Toda tabla expuesta al cliente tendrá RLS habilitado. Nunca se incluirá una
clave `service_role`, `secret` o equivalente dentro de Android, iOS o el código
KMP distribuido.

### Perfiles

`profiles.status` expresa el estado global dentro de La Arroba, no el estado de
confirmación de Auth. Estados previstos:

```text
active
suspended
pending_deletion
deleted
```

Los bloqueos parciales no se acumularán dentro de `profiles.status`. Se
modelarán como restricciones con un `scope`:

```text
all
elipis
polls
loyalty
shop_management
```

Por ejemplo, una persona puede quedar suspendida de votar sin perder el acceso
a su cuenta. `scope` identifica exactamente la funcionalidad bloqueada.

### Documentos legales

- No se migrará `gdpr: true` desde Firestore.
- No se creará una aceptación heredada ficticia.
- Usuarios nuevos y migrados comienzan la aceptación legal desde cero.
- La nueva versión de la app mostrará un modal bloqueante cuando falte una
  versión obligatoria.
- Cada versión aceptada genera una fila nueva. No se actualiza la fila anterior,
  porque el historial debe demostrar qué texto se aceptó y cuándo.
- El formulario de registro puede exigir la casilla, pero la aceptación
  canónica se guardará cuando el correo esté confirmado y exista una sesión
  autenticada.

### Elipis

El saldo y el historial estarán separados:

- `elipi_accounts` contiene el saldo actual y totales derivados.
- `elipi_transactions` es el libro inmutable que explica el saldo.

Una transacción no se edita ni se elimina. Un error se corrige mediante un
movimiento compensatorio. La app podrá mostrar cantidad, comercio/lugar, fecha,
tipo y descripción de cada movimiento.

Las operaciones de entrega, canje y ajuste no escribirán directamente el saldo
desde el cliente. Pasarán por una función transaccional e idempotente.

### Tiendas

El catálogo actual de tiendas continúa en JSON durante la primera fase de
usuarios y Elipis. No se creará todavía un modelo definitivo de tiendas sin
revisar todos esos JSON en la fase correspondiente.

La revisión preliminar encontró 72 tiendas, 7 categorías e IDs slug sin
duplicados. Esos IDs ya se utilizan en navegación, favoritos, caché y URLs y se
deben conservar durante una futura migración.

Una tienda podrá existir sin propietario. Un `super_admin` podrá registrarla y
publicarla sin adquirir ninguna membresía. Más tarde, un propietario podrá
reclamarla.

## Modelo inicial de base de datos

Este es el alcance de la primera migración de datos. Los tipos, constraints,
retención y nombres definitivos se cerrarán antes de escribir el primer SQL.

### `public.profiles`

```text
id              uuid PK -> auth.users.id
member_code     text UNIQUE NOT NULL
name            text NOT NULL
surnames        text NULL
status          text NOT NULL DEFAULT 'active'
created_at      timestamptz NOT NULL
updated_at      timestamptz NOT NULL
```

Notas:

- No se duplica el correo; pertenece a `auth.users`.
- No se incluye avatar porque no forma parte del producto actual.
- No se incluye `gdpr`.
- No se incluye el saldo de puntos.
- `member_code` se genera una sola vez, es fijo e inmutable.
- Los códigos nuevos no deben depender del UUID ni llevar necesariamente el
  prefijo histórico `ELIPI_`.

Formato recomendado pendiente de aprobación final:

```text
member_code: identificador aleatorio con entropía suficiente
QR: laarroba://member/v1/{member_code}
```

Durante la transición, los QR antiguos `ELIPI_<firebase_uid>` se resolverán
mediante una tabla privada de alias. El QR identifica al destinatario, pero no
autoriza una entrega o canje de puntos.

### `private.identity_mappings`

```text
user_id             uuid PK -> auth.users.id
firebase_uid        text UNIQUE NOT NULL
legacy_member_code  text UNIQUE NULL
migrated_at         timestamptz NOT NULL
migration_run_id    uuid NOT NULL
```

Solo existe para migración, compatibilidad de QR antiguo y auditoría. El
cliente no puede leerla ni modificarla. Una futura retirada de aliases exige
medir primero que ya no se utilizan QR antiguos.

### `public.legal_documents`

```text
document_type   text
version         text
title           text NOT NULL
content_url     text NOT NULL
content_hash    text NOT NULL
required        boolean NOT NULL
active          boolean NOT NULL
published_at    timestamptz NOT NULL
PK (document_type, version)
```

El hash permite relacionar una aceptación con el contenido exacto publicado.
Los documentos activos necesarios para registrarse podrán leerse sin sesión;
la escritura será exclusivamente administrativa.

### `public.legal_acceptances`

```text
user_id          uuid -> auth.users.id
document_type    text
document_version text
accepted_at      timestamptz NOT NULL
locale           text NOT NULL
app_version      text NULL
PK (user_id, document_type, document_version)
FK (document_type, document_version) -> legal_documents
```

Cada usuario solo puede leer sus aceptaciones e insertar una aceptación para
sí mismo mediante la operación aprobada. No se permite `update` ni `delete`
desde la app.

### `private.platform_admins`

```text
user_id      uuid PK -> auth.users.id
role         text NOT NULL
active       boolean NOT NULL
created_by   uuid NULL -> auth.users.id
created_at   timestamptz NOT NULL
updated_at   timestamptz NOT NULL
```

Roles iniciales previstos:

```text
super_admin
admin
reviewer
```

El primer `super_admin` ya tiene cuenta en `auth.users`, pero no se insertará
hasta que existan tablas, funciones base y correspondencias de usuarios.
`created_by` será `NULL` para ese bootstrap. Los administradores posteriores
tendrán autor y se gestionarán mediante RPC.

### `private.account_restrictions`

```text
id          uuid PK
user_id     uuid -> auth.users.id
scope       text NOT NULL
reason      text NOT NULL
starts_at   timestamptz NOT NULL
ends_at     timestamptz NULL
active      boolean NOT NULL
created_by  uuid -> auth.users.id
created_at  timestamptz NOT NULL
revoked_by  uuid NULL -> auth.users.id
revoked_at  timestamptz NULL
```

La causa interna no tiene por qué exponerse completa al usuario. Una RPC o
vista segura puede devolver solo sus restricciones efectivas.

### `public.elipi_accounts`

```text
user_id          uuid PK -> auth.users.id
balance          bigint NOT NULL DEFAULT 0
lifetime_earned  bigint NOT NULL DEFAULT 0
lifetime_spent   bigint NOT NULL DEFAULT 0
version          bigint NOT NULL DEFAULT 0
updated_at       timestamptz NOT NULL
```

El cliente puede consultar su saldo, pero no actualizarlo directamente.
`version` permite detectar conflictos si se decide aplicar control optimista.

### `public.elipi_transactions`

```text
id                        uuid PK
user_id                   uuid -> auth.users.id
amount                    bigint NOT NULL
transaction_type          text NOT NULL
shop_id                   text NULL
shop_name_snapshot        text NULL
description               text NULL
balance_after             bigint NOT NULL
occurred_at               timestamptz NOT NULL
created_at                timestamptz NOT NULL
created_by_user_id        uuid NULL -> auth.users.id
reference_transaction_id  uuid NULL
idempotency_key           text UNIQUE NOT NULL
```

Tipos iniciales previstos:

```text
migration_opening_balance
registration_bonus
award
redemption
adjustment
reversal
```

`amount` será positivo para entradas y negativo para consumos. Guardar el
nombre del comercio como snapshot permite mostrar un historial comprensible
aunque el catálogo cambie de nombre en el futuro.

## Funciones RPC previstas

Una RPC es una función PostgreSQL expuesta de forma controlada mediante
PostgREST. La app la invoca como una operación remota, pero la autorización y la
transacción se vuelven a comprobar dentro de PostgreSQL.

Primera fase:

```text
get_my_bootstrap()
accept_legal_document(document_type, version, locale, app_version)
get_my_effective_restrictions()
```

Elipis y compatibilidad:

```text
resolve_member_code(member_code)
award_elipis(operation_id, member_code, shop_id, amount, description)
redeem_elipis(operation_id, member_code, shop_id, amount, description)
adjust_elipis(operation_id, user_id, amount, reason)
```

Administración:

```text
grant_platform_role(user_id, role)
revoke_platform_role(user_id)
set_account_restriction(user_id, scope, reason, ends_at)
revoke_account_restriction(restriction_id)
```

Las funciones privilegiadas deberán:

1. Comprobar `auth.uid()` y el rol activo del llamante.
2. Utilizar `SECURITY DEFINER` solo cuando sea necesario.
3. Fijar un `search_path` seguro.
4. Revocar ejecución de `public`/`anon` cuando no proceda.
5. Validar argumentos y restricciones activas.
6. Ser idempotentes para operaciones económicas.
7. Registrar autor y fecha de las acciones administrativas.

## Políticas RLS mínimas

La matriz definitiva se guardará junto a las migraciones SQL. Como punto de
partida:

| Recurso | `anon` | Usuario autenticado | Administración |
| --- | --- | --- | --- |
| `profiles` | Sin acceso | Leer el propio; actualizar solo `name`/`surnames` mediante operación segura | Mediante RPC restringida |
| `legal_documents` | Leer activos requeridos | Leer activos | Publicar mediante RPC/SQL |
| `legal_acceptances` | Sin acceso | Leer e insertar las propias; sin actualizar/borrar | Auditoría restringida |
| `platform_admins` | Sin acceso | Sin acceso directo | RPC con comprobación de rol |
| `account_restrictions` | Sin acceso | Sin acceso directo; consultar resumen propio | RPC con comprobación de rol |
| `elipi_accounts` | Sin acceso | Leer el propio | Mutar solo mediante RPC transaccional |
| `elipi_transactions` | Sin acceso | Leer las propias, paginadas | Insertar compensaciones mediante RPC |
| `identity_mappings` | Sin acceso | Sin acceso | Proceso de migración/RPC restringida |

Además:

- Forzar RLS en todas las tablas expuestas cuando sea compatible con las
  operaciones administrativas.
- Indexar todas las columnas utilizadas por políticas y joins.
- No permitir que un usuario elija `status`, `role`, saldo o permisos.
- No resolver códigos de miembros desde consultas abiertas. Solo un comercio
  autenticado y autorizado podrá utilizarlos en el flujo necesario.
- Probar cada política con `anon`, usuario A, usuario B, comercio y admin.

## Correspondencia de Firestore a PostgreSQL

### Usuarios

| Firestore | Destino | Tratamiento |
| --- | --- | --- |
| `createAccount` | `profiles.created_at` | Conservar la fecha original cuando sea válida. |
| `email` | `auth.users.email` | No duplicar en `profiles`; usar para reconciliación solo si falta una correspondencia mejor. |
| `gdpr` | Ninguno | Ignorar; las aceptaciones empiezan desde cero. |
| `id = ELIPI_<uid>` | `identity_mappings.legacy_member_code` | Mantener como alias transitorio, no como identidad canónica nueva. |
| UID del documento | `identity_mappings.firebase_uid` | Asociar al UUID de Supabase. |
| `points` | `elipi_accounts` + transacción inicial | Crear saldo y un único `migration_opening_balance`. |
| `user` | `profiles.name` | Conservar; `surnames` comienza en `NULL` si no existe. |

La importación debe ser idempotente. Repetirla no puede duplicar el saldo ni
los movimientos iniciales. Cada lote tendrá un identificador de ejecución y un
informe de filas creadas, omitidas y fallidas.

### Comercios antiguos de Firestore

No se importarán directamente como perfiles mezclando usuario, empresa y
tienda. Primero se conservará el export y se diseñará la fase de tiendas tras
revisar los JSON y las relaciones reales.

Los campos antiguos como CIF, teléfono, premios, saldo y subcolecciones
`log/points` se clasificarán en:

- identidad personal de quien inicia sesión;
- datos legales privados;
- catálogo público de la tienda;
- permisos/membresías;
- configuración de fidelización;
- transacciones de Elipis.

No se asumirá que `log` y `points` contienen conceptos diferentes hasta
comparar los datos exportados y detectar duplicados.

## Flujo objetivo de autenticación

### Arranque de la app

```text
iniciar cliente singleton
-> esperar inicialización de Auth
-> restaurar/refrescar sesión
-> sin sesión: mostrar acceso
-> con sesión: cargar bootstrap de perfil
-> comprobar correo confirmado
-> comprobar documentos legales pendientes
-> comprobar estado/restricciones
-> entrar en la aplicación
```

La navegación no debe decidir entre login y home antes de que Auth termine la
restauración. Así se evita mostrar brevemente login o expulsar a una persona con
sesión válida.

### Login

1. Validar correo/contraseña en UI y dominio.
2. Iniciar sesión con `JchuSupabaseAuth`.
3. Distinguir credenciales incorrectas, correo sin confirmar, cuenta bloqueada,
   rate limit, red, timeout y servidor.
4. Cargar perfil y estado legal.
5. Navegar solo cuando el bootstrap sea válido.

### Registro de usuario

1. Solicitar nombre, apellidos opcionales, correo, contraseña y consentimiento
   visible de los documentos actuales.
2. Crear la cuenta Auth con redirect URL.
3. Crear perfil y cuenta de Elipis mediante trigger/función idempotente.
4. Mostrar pantalla "revisa tu correo" si no existe sesión.
5. Permitir reenviar el mensaje con cooldown.
6. Procesar el deep link PKCE.
7. Con sesión confirmada, registrar las aceptaciones legales canónicas.
8. Entrar en la app.

### Registro orientado a tienda

1. Crear primero una cuenta Auth normal.
2. Confirmar el correo.
3. Mostrar:

```text
Reclamar una tienda existente
Solicitar una tienda nueva
```

4. Crear una solicitud pendiente.
5. Mantener al usuario sin permisos comerciales mientras se revisa.
6. Al aprobar, crear la membresía `owner` de la tienda.

El valor `requested_account_type = shop` puede ayudar a reanudar la UX tras el
correo, pero nunca concede autorización.

### Recuperación de contraseña

1. Solicitar el correo sin revelar si existe una cuenta.
2. Enviar enlace con redirect configurado en Supascale.
3. Procesar el deep link.
4. Solicitar y validar la contraseña nueva.
5. Actualizarla y ofrecer cierre global de otras sesiones si corresponde.

## Persistencia de sesión

`autoLoadFromStorage`, `autoSaveToStorage` y auto-refresh deben estar activos,
pero el cliente Supabase debe ser singleton. El almacenamiento predeterminado
de `supabase-kt` persiste la sesión, aunque no proporciona por sí mismo una
garantía explícita de cifrado de credenciales.

Antes de producción hay que completar una de estas opciones y documentarla:

1. Inyectar desde `laarroba-kmp` un `SessionManager` y `CodeVerifierCache`
   respaldados por Android Keystore/almacenamiento cifrado y Apple Keychain.
2. Incorporar adaptadores seguros y reutilizables en una versión posterior de
   `jchucomponents-supabase`.

No usar `jchucomponents-prefs`/DataStore sin una capa criptográfica para guardar
tokens. Probar restauración, refresh, token revocado, cambio de contraseña,
logout local/global y callback con verifier caducado.

## Arquitectura objetivo en `laarroba-kmp`

Estructura orientativa:

```text
shared/src/commonMain/.../features/auth/
|-- models/
|-- repository/
|-- usecases/
|-- viewmodel/
`-- state/

shared/src/commonMain/.../features/profile/
shared/src/commonMain/.../features/legal/
shared/src/commonMain/.../features/elipis/
shared/src/commonMain/.../features/admin/
shared/src/commonMain/.../features/shops/
`-- features/subscriptions/
```

Responsabilidades:

- `AuthRepository`: Auth, callback, recuperación y logout.
- `ProfileRepository`: perfil propio y bootstrap.
- `LegalRepository`: documentos activos y aceptaciones.
- `ElipiRepository`: saldo e historial paginado.
- `AdminRepository`: RPC administrativas.
- `ShopRepository`: catálogo, solicitudes y membresías cuando llegue su fase.
- ViewModels compartidos: estado estable consumible por Compose y SwiftUI.

Evitar que las vistas dependan directamente del SDK de Supabase o de tipos de
Firebase. Mapear errores técnicos a mensajes/estados de dominio.

## Panel administrativo futuro

Puede implementarse dentro de `laarroba-kmp`, con UI nativa en cada plataforma
y repositorios/ViewModels compartidos. Ocultar una pantalla no aporta
seguridad; todas las operaciones se comprobarán otra vez en PostgreSQL.

Capacidades previstas:

- listar, nombrar, cambiar y desactivar administradores;
- revisar solicitudes de tiendas;
- crear/publicar una tienda sin vincularla al administrador;
- aprobar una reclamación y crear la membresía del propietario;
- aplicar o revocar restricciones globales o por funcionalidad;
- realizar ajustes compensatorios de Elipis;
- consultar la auditoría de cada cambio.

## Fase posterior de tiendas

Antes de crear estas tablas hay que revisar los JSON completos y el modelo
`ShopDetailResponse`, confirmando opcionales, horarios que cruzan medianoche,
servicios, documentos, galería y el significado de `elipacheck` frente a
`loyalty_points`.

Modelo candidato, no definitivo:

```text
shops
shop_categories
shop_category_memberships
shop_media
shop_opening_intervals
shop_features
shop_services
shop_documents
shop_claims
shop_memberships
private.shop_legal_details
```

`shop_memberships` responde quién puede administrar una tienda y con qué rol;
no contiene los datos del comercio. Permitá varias tiendas por usuario y
varios propietarios/empleados por tienda.

`shop_claims` admite dos casos:

- reclamar un slug existente de los JSON;
- proponer una tienda nueva.

Las pruebas de propiedad, si se solicitan, se guardarán en un bucket privado de
Storage. Son documentos o imágenes que acreditan la relación con el comercio;
solo solicitante y revisores autorizados podrán acceder mediante URL firmada.
No son obligatorias hasta que se defina el procedimiento de validación.

## Suscripciones y RevenueCat

Membresía y suscripción son conceptos distintos:

- membresía: quién pertenece o administra una tienda;
- entitlement: qué capacidades tiene contratadas esa tienda.

Modelo candidato:

```text
shop_plans
plan_features
shop_subscriptions
shop_entitlements
revenuecat_webhook_events
```

Se podrán definir varios planes y activar características diferentes por plan.
La autorización efectiva será:

```text
membresía activa
+ entitlement necesario activo
+ usuario sin restricciones
= operación permitida
```

Supabase mantendrá una copia verificada desde webhooks/backend de RevenueCat.
RLS no confiará en un booleano enviado por la app. Los webhooks se procesarán
de forma verificada e idempotente. El App User ID de RevenueCat será el UUID de
Supabase, no el código público del miembro.

## Encuestas y otras funcionalidades

Las restricciones `polls` deben aplicarse en servidor. El sistema actual admite
un identificador anónimo generado en el dispositivo, lo que no permite suspender
de forma fiable ni impedir votos repetidos entre instalaciones. Antes de migrar
encuestas hay que decidir si votar requerirá sesión autenticada.

La misma comprobación central de restricciones se reutilizará para Elipis,
fidelización y gestión de tiendas.

## Fases ejecutables y criterios de salida

### Fase 0: conservar contexto

- [x] Crear este README de continuidad.
- [ ] Mantener actualizado su estado al terminar cada fase.
- [ ] Registrar decisiones nuevas y evitar que queden solo en una conversación.

Criterio de salida: cualquier agente identifica estado, próxima tarea y
decisiones pendientes leyendo este archivo y los `git status`.

### Fase 1: finalizar `jchucomponents-supabase` alpha11

- [x] Revisar los cambios locales sin eliminar trabajo ajeno.
- [x] Ejecutar pruebas comunes y compilación Android/KMP del módulo.
- [x] Compilar los targets Apple y ejecutar tests en iOS Simulator.
- [x] Corregir documentación y changelog de la API preparada.
- [ ] Publicar `3.0.0-alpha11` siguiendo `RELEASING.md`.
- [ ] Probarla desde un consumidor limpio.

Criterio de salida: `laarroba-kmp` puede iniciar, registrar, confirmar,
recuperar, restaurar y cerrar sesión sin acceder directamente a `supabase-kt`.

### Fase 2: cerrar el diseño SQL inicial

- [ ] Aprobar formato de `member_code` y compatibilidad del QR antiguo.
- [ ] Aprobar tipos/constraints y política de retención/borrado.
- [ ] Crear migraciones versionadas para esquemas, tablas, índices y triggers.
- [ ] Crear RPC iniciales.
- [ ] Crear grants y RLS por rol.
- [ ] Crear pruebas SQL negativas y positivas.
- [ ] Ejecutar primero en un entorno Supascale no productivo o con backup.

Criterio de salida: el esquema puede recrearse de forma repetible y ningún
cliente puede elevar privilegios o modificar saldos directamente.

### Fase 3: migrar perfiles y Elipis de Firestore

- [ ] Realizar backup/export antes de escribir en PostgreSQL.
- [ ] Verificar la correspondencia Firebase UID -> Supabase UUID.
- [ ] Ejecutar importación dry-run con informe de conflictos.
- [ ] Importar perfiles sin `gdpr`, correo duplicado ni puntos embebidos.
- [ ] Crear cuentas y movimientos `migration_opening_balance` idempotentes.
- [ ] Comparar recuentos, saldos agregados y muestras por usuario.
- [ ] Insertar el primer `super_admin` después de crear las tablas.
- [ ] Guardar informe y rollback plan.

Criterio de salida: todos los usuarios migrados tienen perfil, mapping y saldo
reconciliado sin duplicados.

### Fase 4: Auth productivo compartido en `laarroba-kmp`

- [ ] Actualizar dependencia a `jchucomponents-supabase:3.0.0-alpha11`.
- [ ] Crear cliente singleton y DI compartida.
- [ ] Sustituir repositorios Firebase duplicados por implementación commonMain.
- [ ] Implementar startup gate y bootstrap.
- [ ] Implementar login y errores tipados.
- [ ] Implementar registro de usuario y espera de confirmación.
- [ ] Implementar reenvío con cooldown.
- [ ] Conectar deep links Android e iOS a `handleAuthCallback`.
- [ ] Implementar recuperación/cambio de contraseña.
- [ ] Implementar persistencia segura y pruebas de restauración.
- [ ] Implementar modal legal bloqueante y aceptación versionada.
- [ ] Adaptar Compose y SwiftUI al mismo ViewModel/estado.
- [ ] Mantener Firebase detrás de feature flag durante la validación.

Criterio de salida: paridad funcional Android/iOS, sesiones restauradas de forma
segura y correo confirmado antes del acceso productivo.

### Fase 5: corte controlado de Firebase Auth/perfiles

- [ ] Ejecutar pruebas end-to-end de usuarios nuevos y migrados.
- [ ] Probar modo offline/red inestable, rate limit y enlaces caducados.
- [ ] Activar Supabase por cohortes o feature flag.
- [ ] Monitorizar errores, callbacks y restauración de sesión.
- [ ] Mantener rollback sin borrar Firebase.
- [ ] Retirar el flujo Firebase solo tras el periodo acordado.

Criterio de salida: Supabase es la fuente canónica de sesión y perfiles sin
incidencias de reconciliación pendientes.

### Fase 6: tiendas, solicitudes y panel administrativo

- [ ] Revisar los JSON completos y cerrar el modelo relacional.
- [ ] Migrar el catálogo conservando slugs.
- [ ] Implementar solicitudes existing/new y pruebas privadas opcionales.
- [ ] Implementar membresías y roles por tienda.
- [ ] Implementar aprobación/rechazo y alta de tiendas por admin.
- [ ] Implementar registro de tienda en Compose y SwiftUI.
- [ ] Implementar panel administrativo con auditoría.

Criterio de salida: ninguna cuenta obtiene permisos comerciales sin membresía
aprobada; una tienda puede existir sin propietario.

### Fase 7: planes, fidelización y encuestas

- [ ] Definir planes y matriz de funcionalidades.
- [ ] Integrar RevenueCat KMP y webhook verificado.
- [ ] Aplicar membresía + entitlement + restricciones en servidor.
- [ ] Diseñar tarjetas/campañas de fidelización.
- [ ] Migrar encuestas decidiendo previamente la autenticación del voto.

Criterio de salida: los permisos pagados y funcionales no dependen de datos
manipulables desde el cliente.

## Orden de creación del esquema inicial

Cuando se escriban las migraciones SQL, seguir este orden para evitar
dependencias circulares:

1. Extensiones, esquemas y tipos/constraints base.
2. `profiles` e `identity_mappings`.
3. Documentos y aceptaciones legales.
4. Administradores y restricciones.
5. Cuentas y transacciones de Elipis.
6. Índices.
7. Triggers de alta/actualización.
8. Funciones RPC sin datos de bootstrap.
9. Grants y RLS.
10. Importación dry-run y real.
11. Alta del primer `super_admin` usando su UUID existente.
12. Pruebas de permisos con distintos actores.

No ejecutar el `INSERT` del primer administrador antes de que exista
`private.platform_admins` ni antes de comprobar que el correo identifica un
único usuario de `auth.users`.

## Pruebas imprescindibles

### Auth

- registro con confirmación activada y desactivada;
- login correcto, credenciales erróneas y correo sin confirmar;
- reenvío y rate limit;
- callback correcto, manipulado, repetido y caducado;
- recuperación y cambio de contraseña;
- arranque sin caché, con sesión, con refresh y con token revocado;
- logout local, otros dispositivos y global;
- cancelación de coroutines sin convertirla en error de UI;
- paridad Android/iOS.

### Base de datos y seguridad

- usuario A no puede leer/modificar al usuario B;
- `anon` solo lee documentos legales expresamente publicados;
- nadie modifica su rol, estado global o restricciones;
- nadie actualiza directamente el saldo;
- una operación con el mismo `idempotency_key` se contabiliza una sola vez;
- una restricción de `elipis` o `polls` bloquea solo ese ámbito;
- un admin inactivo no puede ejecutar RPC administrativas;
- las aceptaciones antiguas no se editan ni borran;
- los listados de transacciones están paginados y ordenados;
- RLS se prueba con tokens reales de cada rol, no solo con `service_role`.

### Migración

- recuento de Auth Firebase frente a Supabase;
- recuento de documentos Firestore frente a perfiles creados;
- usuarios sin email, duplicados o sin mapping se envían a cuarentena;
- suma de `points` fuente igual a suma de saldos iniciales importados;
- una segunda ejecución no cambia recuentos ni saldos;
- muestras manuales incluyen nombre, fecha, código antiguo y saldo.

## Observabilidad y auditoría

- Usar `correlation_id` para seguir una operación app -> función -> base de
  datos.
- Usar `idempotency_key` para cualquier entrega, canje, compra o webhook.
- No registrar access tokens, refresh tokens, contraseñas ni enlaces completos
  de Auth.
- Registrar métricas de login fallido por código, callbacks inválidos, refresh
  fallido y tiempo de bootstrap sin guardar datos sensibles.
- Mantener autor, fecha y motivo para cambios administrativos.

## Decisiones pendientes que requieren confirmación

1. Formato final y longitud de `member_code` y duración de compatibilidad con
   QR `ELIPI_`.
2. Si se conserva el bonus de registro de 100 Elipis y cuál será su fuente de
   configuración al retirar Firebase Remote Config.
3. Política de borrado, anonimización y retención de historial financiero y
   aceptaciones legales.
4. Si el almacenamiento seguro se implementa primero en `laarroba-kmp` o se
   ofrece como API reutilizable de `jchucomponents-supabase`.
5. Contenido, versión inicial y URL/hash de condiciones y privacidad.
6. SMTP, remitente y plantillas de correo en Supascale. Aplazado.
7. Uso o desactivación del proveedor telefónico observado en Supascale.
8. Planes comerciales y características de cada entitlement.
9. Requisito de sesión autenticada para votar en futuras encuestas.

Una decisión pendiente no debe resolverse silenciosamente si cambia datos,
seguridad, cobros o experiencia de registro.

## Protocolo para retomar el trabajo

Antes de modificar nada:

1. Leer este README completo.
2. Leer `AGENT.md`, `PROJECT_STATUS.md`, `ROADMAP.md`, `ARCHITECTURE.md` y
   `RELEASING.md` en `jchucomponents`.
3. Leer `laarroba-kmp/docs/supabase-auth-migration.md`.
4. Ejecutar `git status --short` en ambos repositorios.
5. Preservar todos los cambios existentes; no usar `reset --hard` ni descartar
   archivos ajenos.
6. Identificar la primera fase sin criterio de salida completado.
7. Verificar el estado real con tests antes de marcar una casilla.
8. Actualizar este documento al terminar una fase o cambiar una decisión.

Siguiente tarea recomendada al crear este documento:

```text
Fase 1: revisar y validar los cambios locales de
jchucomponents-supabase para 3.0.0-alpha11 antes de publicarlos.
```
