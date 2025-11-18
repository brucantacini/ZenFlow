# Testes de Autenticação e Endpoints no Swagger-UI

## Pré-requisitos

- Aplicação Spring Boot rodando na porta 8080
- Acesse: http://localhost:8080/swagger-ui.html

---

## 1. Fazer Login e Obter Token

1. Na página do Swagger UI, localize o endpoint **POST /api/auth/login**
2. Clique em **"Try it out"**
3. No campo **Request body**, insira:
   ```json
   {
     "username": "admin",
     "password": "admin123"
   }
   ```
4. Clique em **"Execute"**
5. Na resposta, copie o valor do campo **"token"**

---

## 2. Autenticar no Swagger UI

1. No topo da página, clique no botão **"Authorize"** (ícone de cadeado)
2. No campo **"Value"**, cole o token copiado (sem adicionar "Bearer")
3. Clique em **"Authorize"**
4. Clique em **"Close"**

Agora você está autenticado e pode testar os endpoints protegidos.

---

## 3. Testar Endpoints Protegidos

### Exemplo: Listar Departamentos

1. Localize o endpoint **GET /api/departamentos**
2. Clique em **"Try it out"**
3. Clique em **"Execute"**
4. Deve retornar **200 OK** com a lista de departamentos

### Exemplo: Criar Departamento

1. Localize o endpoint **POST /api/departamentos**
2. Clique em **"Try it out"**
3. No campo **Request body**, insira:
   ```json
   {
     "nomeDepto": "TI",
     "descricao": "Departamento de Tecnologia"
   }
   ```
4. Clique em **"Execute"**
5. Deve retornar **201 Created** com o departamento criado

---

## 4. Testar Endpoints Públicos

Estes endpoints não precisam de autenticação:

- **POST /api/auth/login** - Fazer login
- **GET /swagger-ui.html** - Interface do Swagger
- **GET /api-docs** - Documentação da API

---

## 5. Verificar Proteção (Teste Negativo)

1. Clique em **"Authorize"** novamente
2. Clique em **"Logout"**
3. Tente executar qualquer endpoint protegido (ex: GET /api/departamentos)
4. Deve retornar **401 Unauthorized**

---

## Usuários de Teste

| Username | Password |
|----------|----------|
| admin    | admin123 |
| user     | admin123 |

---

## Endpoints Principais para Testar

### Autenticação
- `POST /api/auth/login` - Obter token JWT

### Departamentos
- `GET /api/departamentos` - Listar departamentos
- `GET /api/departamentos/{id}` - Buscar por ID
- `POST /api/departamentos` - Criar departamento
- `PUT /api/departamentos/{id}` - Atualizar departamento
- `DELETE /api/departamentos/{id}` - Deletar departamento

### Níveis de Estresse
- `GET /api/niveis-estresse` - Listar níveis
- `POST /api/niveis-estresse` - Criar nível

### Avaliações
- `GET /api/avaliacoes` - Listar avaliações
- `POST /api/avaliacoes` - Criar avaliação
- `GET /api/avaliacoes/departamento/{idDepto}` - Buscar por departamento

---

## Problemas Comuns

**Erro 401 Unauthorized:**
- Verifique se autenticou corretamente no botão "Authorize"
- Verifique se o token não expirou (válido por 24 horas)
- Certifique-se de ter copiado o token completo

**Erro ao fazer login:**
- Verifique se o username e password estão corretos
- Usuários disponíveis: `admin/admin123` ou `user/admin123`

---

## Checklist de Testes

- [ ] Login retorna token JWT
- [ ] Token funciona nos endpoints protegidos
- [ ] Sem token retorna 401 Unauthorized
- [ ] Swagger UI permite autenticação via botão "Authorize"
- [ ] Endpoints públicos funcionam sem token
- [ ] Endpoints protegidos funcionam com token

