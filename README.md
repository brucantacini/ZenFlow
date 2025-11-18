# ZenFlow API

API REST para o sistema ZenFlow - Diário de Bem-Estar Anônimo.

## 📋 Descrição

Sistema que permite aos funcionários registrarem seus níveis de estresse de forma anônima, com dashboard para visualização de métricas por departamento.

## 🚀 Tecnologias

- **Java 21**
- **Spring Boot 3.5.7**
- **Spring Data JPA**
- **Spring Security + JWT**
- **Oracle Database**
- **Swagger/OpenAPI**
- **Lombok**
- **Maven**

## 📦 Requisitos Implementados

- ✅ API Rest seguindo boas práticas
- ✅ Persistência com Spring Data JPA
- ✅ Relacionamentos entre entidades (1:N)
- ✅ Validação com Bean Validation
- ✅ Paginação, ordenação e filtros
- ✅ Documentação com Swagger
- ✅ Autenticação com JWT
- ✅ Integração com Procedures/Functions Oracle

## 🏗️ Arquitetura

```
Controller → Service → Repository → Database
     ↓         ↓
    DTO    Exception Handler
```

## 🔐 Autenticação

A API utiliza JWT (JSON Web Token) para autenticação.

### Usuários de Teste

- **Username:** `admin` | **Password:** `admin123`
- **Username:** `user` | **Password:** `admin123`

### Como usar

1. Faça login em `/api/auth/login` para obter o token
2. Use o token no header: `Authorization: Bearer {token}`
3. No Swagger UI, clique em "Authorize" e cole o token

## 📚 Endpoints Principais

### Autenticação
- `POST /api/auth/login` - Fazer login e obter token JWT

### Departamentos
- `GET /api/departamentos` - Listar departamentos (paginado)
- `GET /api/departamentos/{id}` - Buscar por ID
- `POST /api/departamentos` - Criar departamento
- `PUT /api/departamentos/{id}` - Atualizar departamento
- `DELETE /api/departamentos/{id}` - Deletar departamento

### Níveis de Estresse
- `GET /api/nivel-estresse` - Listar níveis
- `POST /api/nivel-estresse` - Criar nível

### Avaliações
- `GET /api/avaliacoes` - Listar avaliações (paginado)
- `POST /api/avaliacoes` - Criar avaliação
- `GET /api/avaliacoes/departamento/{idDepto}` - Buscar por departamento

## 🗄️ Banco de Dados

- **Oracle Database** (FIAP)
- **Tabelas:**
  - `T_ZF_DEPARTAMENTO`
  - `T_ZF_NIVEL_ESTRESSE`
  - `T_ZF_AVALIACAO`

## 🔧 Configuração

### application.properties

```properties
# Database
spring.datasource.url=jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha

# JWT
jwt.secret=sua_chave_secreta_minimo_32_caracteres
jwt.expiration=86400000
```

## 📖 Documentação

Acesse a documentação Swagger em:
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **API Docs:** http://localhost:8080/api-docs

## 🧪 Testes

Consulte o arquivo `TESTE_JWT.txt` para instruções detalhadas de teste.

## 📝 Próximos Passos

- [ ] Deploy em nuvem (Azure/Heroku/Railway)
- [ ] Testes unitários
- [ ] CI/CD Pipeline

## 👥 Autores

- Bruno Cantacini

## 📄 Licença

Este projeto é parte do trabalho acadêmico da FIAP.

