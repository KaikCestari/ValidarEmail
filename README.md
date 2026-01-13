# API de Validação de Email

API REST profissional desenvolvida em Java com Spring Boot para validação de emails, com foco em bloquear emails descartáveis/temporários que podem ser usados para abusar de testes gratuitos.

## 🎯 Funcionalidades

- ✅ Validação de formato de email (RFC 5322)
- ✅ Verificação de domínios descartáveis via banco de dados (H2 por padrão)
- ✅ Integração opcional com Hunter API para validação externa
- ✅ Autenticação via API Key (header X-API-KEY) para endpoints públicos
- ✅ Autenticação JWT para endpoints administrativos
- ✅ Rate limiting (60 req/min por IP)
- ✅ Arquitetura seguindo princípios SOLID
- ✅ Documentação Swagger/OpenAPI integrada
- ✅ Flyway para migrations do banco de dados

## 🚀 Início Rápido

### Pré-requisitos

- Java 17 ou superior (21 recomendado)
- Maven 3.6+

### Executando localmente

1. Clone o repositório
2.  Crie um arquivo `.env` (exemplo abaixo)
3. Execute:

```bash
mvn spring-boot:run
```

A API estará disponível em: `http://localhost:8080`

Se estiver usando `.env`, ele sera carregado automaticamente na inicializacao.

### Compilando e executando com Java

```bash
mvn clean package
java -jar target/validacao-email-api-1.0.0.jar
```

### Acessando o Swagger

- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **H2 Console** (dev): http://localhost:8080/h2-console
- No Swagger UI, use **Authorize** com `BearerAuth` (JWT) ou `ApiKeyAuth` (X-API-KEY) conforme o endpoint

## 📡 Endpoints

### Públicos (requerem API Key no header X-API-KEY)

#### POST /email/verify

Verifica se um email é válido.

**Headers:**
```
X-API-KEY: sua-api-key-aqui
```

**Request:**
```json
{
  "email": "usuario@exemplo.com",
  "useExternalCheck": false
}
```

**Response:**
```json
{
  "allowed": true,
  "email": "usuario@exemplo.com",
  "domain": "exemplo.com",
  "isDisposable": false,
  "externalChecked": false,
  "externalProvider": null,
  "reason": "VALID_EMAIL"
}
```

**Motivos possíveis:**
- `INVALID_FORMAT`: Formato de email inválido
- `DISPOSABLE_DOMAIN`: Domínio está na blacklist
- `EXTERNAL_PROVIDER_REJECTED`: Hunter API rejeitou o email
- `EXTERNAL_PROVIDER_FAILED`: Falha ao chamar o provedor externo
- `VALID_EMAIL`: Email válido

**Configuracao do Hunter (env vars):**
Exporte no terminal ou use um `.env` carregado automaticamente.
```
HUNTER_ENABLED=true
HUNTER_API_KEY=suachave
```

### Autenticação

#### POST /auth/login

Autentica um administrador e retorna JWT token.

**Credenciais padrão:**
- Username: `admin`
- Password: `admin123`

**Request:**
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**Response:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### Admin (requerem JWT token no header Authorization: Bearer <token>)

#### GET /admin/disposable-domains
Lista todos os domínios descartáveis

#### POST /admin/disposable-domains
Adiciona um novo domínio descartável

**Request:**
```json
{
  "domain": "tempmail.com"
}
```

#### DELETE /admin/disposable-domains/{domain}
Remove um domínio descartável

#### GET /admin/api-keys
Lista todas as API Keys

#### POST /admin/api-keys
Cria uma nova API Key

**Request:**
```json
{
  "name": "Minha API Key"
}
```

**Response:**
```json
{
  "id": 1,
  "name": "Minha API Key",
  "key": "abc123...",  // Apenas na criação!
  "active": true,
  "createdAt": "2024-01-01T00:00:00"
}
```

⚠️ **Importante**: A API Key é mostrada apenas uma vez na criação. Salve-a imediatamente!

#### DELETE /admin/api-keys/{id}
Remove uma API Key

## 🔒 Segurança

### API Key
- Endpoints públicos requerem header `X-API-KEY`
- API Keys são armazenadas como hash SHA-256 no banco
- Crie API Keys via endpoint admin

### JWT
- Tokens JWT para autenticação admin
- Configurável via `JWT_SECRET` (mínimo 32 caracteres)
- Expiração: 24 horas (configurável)

### Rate Limiting
- 60 requisições por minuto por IP para `/email/verify`
- Retorna HTTP 429 quando excedido

## ⚙️ Configuração

### Variáveis de Ambiente

Crie um arquivo `.env` (carregado automaticamente) ou configure as variaveis no ambiente:

```env
# Database (H2 por padrão; ajuste se for usar MySQL)
DB_URL=jdbc:h2:file:./data/validacao_email;AUTO_SERVER=TRUE;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=MySQL
DB_USER=sa
DB_PASS=

# JWT Secret (mínimo 32 caracteres)
JWT_SECRET=your-256-bit-secret-key-change-in-production-min-32-chars

# Hunter API (opcional)
HUNTER_ENABLED=false
HUNTER_API_KEY=your-hunter-api-key-here
```

### Hunter API (Opcional)

Para habilitar verificação externa com Hunter:

1. Obtenha uma API Key em https://hunter.io
2. Configure `HUNTER_ENABLED=true`
3. Configure `HUNTER_API_KEY=sua-chave`

## 📋 Exemplos de Uso

### Verificar email (com API Key)

```bash
curl -X POST http://localhost:8080/email/verify \
  -H "Content-Type: application/json" \
  -H "X-API-KEY: sua-api-key" \
  -d '{"email": "usuario@gmail.com"}'
```

### Login Admin

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "admin123"}'
```

### Criar API Key (com JWT)

```bash
curl -X POST http://localhost:8080/admin/api-keys \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer seu-jwt-token" \
  -d '{"name": "Minha API Key"}'
```

### Adicionar domínio descartável (com JWT)

```bash
curl -X POST http://localhost:8080/admin/disposable-domains \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer seu-jwt-token" \
  -d '{"domain": "tempmail.com"}'
```

## 🏗️ Arquitetura

### Princípios SOLID

- **Single Responsibility**: Cada classe tem uma única responsabilidade
- **Open/Closed**: Fácil extensão sem modificar código existente
- **Liskov Substitution**: Abstrações substituíveis
- **Interface Segregation**: Interfaces específicas
- **Dependency Inversion**: Dependências via injeção

### Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/validacaoemail/
│   │   ├── config/          # Configurações (Swagger, Security)
│   │   ├── controller/      # Controllers REST
│   │   ├── entity/          # Entidades JPA
│   │   ├── repository/      # Repositories JPA
│   │   ├── security/        # Filtros de segurança (JWT, API Key, Rate Limit)
│   │   ├── service/         # Lógica de negócio
│   │   ├── validator/       # Validadores específicos
│   │   ├── dto/             # Data Transfer Objects
│   │   ├── exception/       # Tratamento de exceções
│   │   └── util/            # Utilitários (JWT, Hash)
│   └── resources/
│       ├── db/migration/    # Migrations Flyway
│       └── application.properties
└── test/                    # Testes
```

## 🗄️ Banco de Dados

Por padrão a aplicação usa H2 em arquivo. Para MySQL, ajuste as variáveis `DB_URL`, `DB_USER` e `DB_PASS`.

### Migrations Flyway

- `V1__create_disposable_domain_table.sql`: Tabela de domínios descartáveis
- `V2__create_api_key_table.sql`: Tabela de API Keys
- `V3__create_admin_user_table.sql`: Tabela de usuários admin
- `V4__seed_initial_data.sql`: Dados iniciais (admin padrão e domínios)

### Dados Iniciais

- **Admin padrão**: username: `admin`, password: `admin123`
- **Domínios descartáveis**: Lista inicial de domínios conhecidos

⚠️ **IMPORTANTE**: Altere a senha do admin padrão em produção!

## 🧪 Testes

```bash
mvn test
```

## 📝 Licença

Este projeto é um exemplo educacional para portfólio.

## 🔧 Troubleshooting


### Login admin falhando

Se estiver usando H2 em arquivo, apague o banco antigo para recriar o usuário admin com a senha padrão.

### API Key inválida

Certifique-se de que a API Key foi criada via endpoint admin e está sendo enviada no header correto.

### Rate limit excedido

Aguarde 1 minuto ou ajuste o limite em `RateLimitFilter`.

### Hunter API não funciona

Verifique se `HUNTER_ENABLED=true` e `HUNTER_API_KEY` estão configurados. A API continua funcionando sem Hunter (fail-safe).
