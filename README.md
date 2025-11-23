# ZenFlow - Sistema de Diário de Bem-Estar Anônimo

Sistema que permite aos funcionários registrarem seus níveis de estresse de forma anônima, com dashboard para visualização de métricas por departamento.

## 👥 Integrantes do Grupo

### Amanda Galdino - RM560066

### Bruno Cantacini - RM560242

### Gustavo Gonçalves - RM556823

## 🎥 Vídeos de Apresentação

🔗 **Link do Vídeo executando projeto java com swagger**: https://youtu.be/yC8XuJ6d-4s

🔗 **Link do Vídeo apresentando sistema completo**: https://youtu.be/DPHlj1m0nOo

### Conteúdo do Vídeo

O vídeo apresenta:

- **Proposta Tecnológica**: Sistema de diário de bem-estar anônimo com autenticação JWT
- **Público-Alvo**: Funcionários de empresas, RH, Gestores de departamentos
- **Problemas que a aplicação soluciona**:
  - Registro anônimo de níveis de estresse pelos funcionários
  - Visualização de métricas de bem-estar por departamento
  - Análise de tendências de estresse ao longo do tempo
  - Integração com banco de dados Oracle para persistência confiável
  - Autenticação segura com JWT

## 🚀 Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3.5.7**
- **Spring Data JPA**
- **Spring Security + JWT**
- **Oracle Database**
- **Swagger/OpenAPI 3**
- **Lombok**
- **Bean Validation**
- **Maven**

## 📋 Pré-requisitos

- Java 21 ou superior
- Maven 3.6+
- Oracle Database (FIAP ou local)
- Docker e Docker Compose (opcional, para deploy)

## ⚙️ Configuração

### 1. Configuração do Banco de Dados

#### Oracle FIAP (Padrão)

O projeto está configurado para usar o Oracle FIAP por padrão:

- Host: `oracle.fiap.com.br`
- Porta: `1521`
- SID: `ORCL`
- Usuário: `rm560242`
- Senha: `271005`

As configurações podem ser alteradas no arquivo `application.properties` ou via variáveis de ambiente.

### 2. Configuração JWT

O JWT está configurado no `application.properties`:

```properties
jwt.secret=ZenFlowSecretKeyForJWTTokenGeneration2024GlobalSolutionMinimo32Caracteres
jwt.expiration=86400000
```

**Importante**: A chave secreta deve ter no mínimo 32 caracteres para segurança adequada.

### 3. Usuários Padrão

O sistema possui usuários de teste pré-configurados:

- **Username:** `admin` | **Password:** `admin123`
- **Username:** `user` | **Password:** `admin123`

## 🏃‍♂️ Como Rodar a Aplicação

### Opção 1: Execução Local (Desenvolvimento)

#### Pré-requisitos

- Java 21 instalado
- Maven 3.6+ instalado
- Acesso ao Oracle FIAP

#### Passo a Passo

1. **Clone o repositório:**

```bash
git clone <URL_DO_REPOSITORIO>
cd ZenFlow
```

2. **Compile o projeto:**

```bash
mvn clean install
```

3. **Execute a aplicação:**

```bash
mvn spring-boot:run
```

4. **Acesse a aplicação:**

```
http://localhost:8080/swagger-ui.html
```

### Opção 2: Execução com JAR (Produção)

1. **Gere o JAR:**

```bash
mvn clean package
```

2. **Execute o JAR:**

```bash
java -jar target/ZenFlow-0.0.1-SNAPSHOT.jar
```

### Opção 3: Execução com Docker (Recomendado para Produção)

Consulte a seção [🐳 Deploy com Docker](#-deploy-com-docker) abaixo para instruções completas de deploy com Docker e Docker Compose.

## 🐳 Deploy com Docker

### Pré-requisitos para Deploy

- Docker instalado
- Docker Compose instalado (ou plugin do Docker)
- Java 21 e Maven (para build local)

### 1. Build Local

```bash
# Compilar o projeto
mvn clean package -DskipTests

# Verificar JAR gerado
ls -lh target/ZenFlow-0.0.1-SNAPSHOT.jar
```

### 2. Deploy Local com Docker

```bash
# Build da imagem Docker
docker build -t zenflow:latest .

# Executar com Docker Compose (background)
docker compose up -d

# Verificar status
docker compose ps

# Ver logs
docker compose logs -f zenflow
# Pressione Ctrl+C para sair dos logs

# Parar a aplicação
docker compose down
```

### 3. Deploy na VM (Azure/AWS/GCP)

#### 3.1. Instalação de Dependências na VM

```bash
# Atualizar sistema
sudo apt update
sudo apt upgrade -y

# Instalar Git
sudo apt install git -y

# Instalar Java 21
sudo apt install openjdk-21-jdk -y
java -version

# Configurar JAVA_HOME
echo 'export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64' >> ~/.bashrc
echo 'export PATH=$JAVA_HOME/bin:$PATH' >> ~/.bashrc
source ~/.bashrc

# Instalar Maven
sudo apt install maven -y
mvn -version

# Instalar Docker (script oficial)
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh
sudo docker --version
sudo docker compose version

# Adicionar usuário ao grupo docker (opcional)
sudo usermod -aG docker $USER
# NOTA: Use 'sudo' nos comandos docker até fazer logout/login
```

#### 3.2. Clonar e Preparar Repositório

```bash
# Clonar repositório
cd ~
git clone <URL_DO_SEU_REPOSITORIO>
cd ZenFlow
```

#### 3.3. Build e Deploy na VM

```bash
# Build do JAR
mvn clean package -DskipTests

# Verificar JAR gerado
ls -lh target/ZenFlow-0.0.1-SNAPSHOT.jar

# Build da imagem Docker
sudo docker build -t zenflow:latest .

# Executar com Docker Compose (background)
sudo docker compose up -d

# Verificar status
sudo docker compose ps

# Ver logs (aguarde aparecer "Started ZenFlowApplication")
sudo docker compose logs -f zenflow
# Pressione Ctrl+C para sair dos logs

# IMPORTANTE: Aguarde 30-60 segundos após subir o container antes de testar endpoints
```

#### 3.4. Gerenciamento do Container

```bash
# Parar aplicação
sudo docker compose down

# Reiniciar aplicação
sudo docker compose restart

# Ver logs em tempo real
sudo docker compose logs -f

# Ver status
sudo docker compose ps

# Rebuild e restart
sudo docker compose up -d --build
```

#### 3.5. Configuração de Firewall (Azure)

Se estiver usando Azure VM, configure o Network Security Group (NSG) para permitir tráfego na porta 8080:

1. Acesse o portal Azure
2. Vá em **Network Security Groups**
3. Adicione regra de entrada:
   - **Porta**: 8080
   - **Protocolo**: TCP
   - **Ação**: Allow
   - **Prioridade**: 100

### 4. Verificar Deploy

```bash
# Health Check (local)
curl http://localhost:8080/swagger-ui.html

# Health Check (VM - substitua pelo IP público)
curl http://<IP_PUBLICO_VM>:8080/swagger-ui.html
```

## 🧪 Testes dos Endpoints

### Base URL

```
http://localhost:8080/api
```

Para VM, substitua `localhost` pelo IP público da VM.

### 1. Autenticação (JWT)

#### Fazer Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

**Resposta esperada:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "admin"
}
```

**Importante**: Use o token retornado no header `Authorization: Bearer {token}` para acessar endpoints protegidos.

### 2. Testes de Departamentos

#### Criar Departamento

```bash
curl -X POST http://localhost:8080/api/departamentos \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "nome": "Recursos Humanos",
    "descricao": "Departamento de Recursos Humanos"
  }'
```

#### Listar Departamentos

```bash
curl http://localhost:8080/api/departamentos?page=0&size=10 \
  -H "Authorization: Bearer {token}"
```

#### Buscar Departamento por ID

```bash
curl http://localhost:8080/api/departamentos/1 \
  -H "Authorization: Bearer {token}"
```

#### Buscar por Filtro

```bash
curl "http://localhost:8080/api/departamentos/buscar?nome=RH" \
  -H "Authorization: Bearer {token}"
```

#### Atualizar Departamento

```bash
curl -X PUT http://localhost:8080/api/departamentos/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "nome": "Recursos Humanos - Atualizado",
    "descricao": "Departamento atualizado"
  }'
```

#### Deletar Departamento

```bash
curl -X DELETE http://localhost:8080/api/departamentos/1 \
  -H "Authorization: Bearer {token}"
```

### 3. Testes de Níveis de Estresse

#### Listar Níveis

```bash
curl http://localhost:8080/api/niveis-estresse?page=0&size=10 \
  -H "Authorization: Bearer {token}"
```

#### Buscar por ID

```bash
curl http://localhost:8080/api/niveis-estresse/1 \
  -H "Authorization: Bearer {token}"
```

#### Buscar por Nível

```bash
curl http://localhost:8080/api/niveis-estresse/nivel/3 \
  -H "Authorization: Bearer {token}"
```

#### Criar Nível

```bash
curl -X POST http://localhost:8080/api/niveis-estresse \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "nivel": 3,
    "descricao": "Estresse Moderado"
  }'
```

### 4. Testes de Avaliações

#### Criar Avaliação

```bash
curl -X POST http://localhost:8080/api/avaliacoes \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "idDepartamento": 1,
    "idNivelEstresse": 3,
    "comentario": "Dia muito estressante"
  }'
```

#### Criar Avaliação via Procedure

```bash
curl -X POST http://localhost:8080/api/avaliacoes/procedure \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "idDepartamento": 1,
    "idNivelEstresse": 3,
    "comentario": "Dia muito estressante"
  }'
```

#### Listar Avaliações

```bash
curl http://localhost:8080/api/avaliacoes?page=0&size=10 \
  -H "Authorization: Bearer {token}"
```

#### Buscar por Departamento

```bash
curl http://localhost:8080/api/avaliacoes/departamento/1?page=0&size=10 \
  -H "Authorization: Bearer {token}"
```

#### Buscar por Nível

```bash
curl http://localhost:8080/api/avaliacoes/nivel/3?page=0&size=10 \
  -H "Authorization: Bearer {token}"
```

#### Buscar por Período

```bash
curl "http://localhost:8080/api/avaliacoes/periodo?inicio=2025-01-01&fim=2025-01-31" \
  -H "Authorization: Bearer {token}"
```

#### Calcular Média por Departamento

```bash
curl http://localhost:8080/api/avaliacoes/departamento/1/media \
  -H "Authorization: Bearer {token}"
```

#### Calcular Média Semanal (via Function Oracle)

```bash
curl "http://localhost:8080/api/avaliacoes/departamento/1/media-semanal?dataInicio=2025-01-01&dataFim=2025-01-31" \
  -H "Authorization: Bearer {token}"
```

#### Validar Nível de Estresse (via Function Oracle)

```bash
curl http://localhost:8080/api/avaliacoes/validar-nivel/3 \
  -H "Authorization: Bearer {token}"
```

### 5. Testes com Postman/Insomnia

Para facilitar os testes, você pode importar a coleção de endpoints disponível ou usar o arquivo [TESTE_JWT.txt](TESTE_JWT.txt) para instruções detalhadas de autenticação.

### 6. Testes via Swagger UI

1. Acesse: `http://localhost:8080/swagger-ui.html`
2. Faça login em `/api/auth/login` para obter o token
3. Clique em "Authorize" no topo da página
4. Cole o token (sem "Bearer")
5. Teste os endpoints diretamente na interface

### 7. Troubleshooting

#### Aplicação não inicia

```bash
# Verificar logs
sudo docker compose logs -f zenflow

# Verificar se a porta está em uso
sudo netstat -tulpn | grep 8080

# Verificar status do container
sudo docker compose ps
```

#### Erro de conexão com banco

- Verifique as credenciais do Oracle no `docker-compose.yml` ou `application.properties`
- Confirme que a VM tem acesso ao Oracle FIAP
- Teste a conexão: `telnet oracle.fiap.com.br 1521`

#### Erro de autenticação (401)

- Verifique se o token JWT está sendo enviado no header
- Confirme que o token não expirou (padrão: 24 horas)
- Faça login novamente para obter um novo token

#### Erro de permissão Docker

```bash
# Adicionar usuário ao grupo docker
sudo usermod -aG docker $USER

# OU usar sudo temporariamente
sudo docker compose up -d
```

## 📡 Documentação Completa da API - Endpoints

### Base URL

```
http://localhost:8080/api
```

### Autenticação

| Método  | Endpoint            | Descrição                   | Autenticação |
| -------- | ------------------- | ----------------------------- | -------------- |
| `POST` | `/api/auth/login` | Fazer login e obter token JWT | Não requer    |

### Departamentos

| Método    | Endpoint                      | Descrição                              | Autenticação |
| ---------- | ----------------------------- | ---------------------------------------- | -------------- |
| `GET`    | `/api/departamentos`        | Listar todos os departamentos (paginado) | Requer         |
| `GET`    | `/api/departamentos/{id}`   | Buscar departamento por ID               | Requer         |
| `GET`    | `/api/departamentos/buscar` | Buscar por filtro (nome ou descrição)  | Requer         |
| `POST`   | `/api/departamentos`        | Criar novo departamento                  | Requer         |
| `PUT`    | `/api/departamentos/{id}`   | Atualizar departamento existente         | Requer         |
| `DELETE` | `/api/departamentos/{id}`   | Deletar departamento                     | Requer         |

**Parâmetros de Paginação (para listar):**

- `page`: Número da página (padrão: 0)
- `size`: Tamanho da página (padrão: 10)

**Parâmetros de Busca (para buscar):**

- `nome`: Nome do departamento (opcional)
- `descricao`: Descrição do departamento (opcional)
- `page`: Número da página (padrão: 0)
- `size`: Tamanho da página (padrão: 10)

### Níveis de Estresse

| Método    | Endpoint                               | Descrição                        | Autenticação |
| ---------- | -------------------------------------- | ---------------------------------- | -------------- |
| `GET`    | `/api/niveis-estresse`               | Listar todos os níveis (paginado) | Requer         |
| `GET`    | `/api/niveis-estresse/{id}`          | Buscar nível por ID               | Requer         |
| `GET`    | `/api/niveis-estresse/nivel/{nivel}` | Buscar por nível numérico        | Requer         |
| `GET`    | `/api/niveis-estresse/buscar`        | Buscar por descrição             | Requer         |
| `POST`   | `/api/niveis-estresse`               | Criar novo nível                  | Requer         |
| `PUT`    | `/api/niveis-estresse/{id}`          | Atualizar nível existente         | Requer         |
| `DELETE` | `/api/niveis-estresse/{id}`          | Deletar nível                     | Requer         |

**Parâmetros de Paginação (para listar):**

- `page`: Número da página (padrão: 0)
- `size`: Tamanho da página (padrão: 10)

**Parâmetros de Busca (para buscar):**

- `descricao`: Descrição do nível (obrigatório)
- `page`: Número da página (padrão: 0)
- `size`: Tamanho da página (padrão: 10)

### Avaliações

| Método    | Endpoint                                                  | Descrição                                | Autenticação |
| ---------- | --------------------------------------------------------- | ------------------------------------------ | -------------- |
| `GET`    | `/api/avaliacoes`                                       | Listar todas as avaliações (paginado)    | Requer         |
| `GET`    | `/api/avaliacoes/{id}`                                  | Buscar avaliação por ID                  | Requer         |
| `GET`    | `/api/avaliacoes/departamento/{idDepto}`                | Buscar por departamento                    | Requer         |
| `GET`    | `/api/avaliacoes/nivel/{nivel}`                         | Buscar por nível de estresse              | Requer         |
| `GET`    | `/api/avaliacoes/periodo`                               | Buscar por período                        | Requer         |
| `GET`    | `/api/avaliacoes/departamento/{idDepto}/periodo`        | Buscar por departamento e período         | Requer         |
| `GET`    | `/api/avaliacoes/departamento/{idDepto}/media`          | Calcular média de estresse                | Requer         |
| `GET`    | `/api/avaliacoes/departamento/{idDepto}/total`          | Contar avaliações por departamento       | Requer         |
| `GET`    | `/api/avaliacoes/departamento/{idDepto}/media-semanal`  | Média semanal via Function Oracle         | Requer         |
| `GET`    | `/api/avaliacoes/validar-nivel/{nivel}`                 | Validar nível via Function Oracle         | Requer         |
| `GET`    | `/api/avaliacoes/departamento/{idDepto}/contar-periodo` | Contar registros por período via Function | Requer         |
| `POST`   | `/api/avaliacoes`                                       | Criar nova avaliação                     | Requer         |
| `POST`   | `/api/avaliacoes/procedure`                             | Criar avaliação via Procedure Oracle     | Requer         |
| `PUT`    | `/api/avaliacoes/{id}`                                  | Atualizar avaliação existente            | Requer         |
| `DELETE` | `/api/avaliacoes/{id}`                                  | Deletar avaliação                        | Requer         |

**Parâmetros de Paginação (para listar):**

- `page`: Número da página (padrão: 0)
- `size`: Tamanho da página (padrão: 10)

**Parâmetros de Período:**

- `inicio`: Data de início (formato: yyyy-MM-dd)
- `fim`: Data de fim (formato: yyyy-MM-dd)

---

**Total de Endpoints**: 30+ endpoints disponíveis

## 🧪 Testando com Postman/Insomnia

### Exemplo de Login

```json
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

### Exemplo de Criação de Departamento

```json
POST /api/departamentos
Content-Type: application/json
Authorization: Bearer {token}

{
  "nome": "Recursos Humanos",
  "descricao": "Departamento de Recursos Humanos"
}
```

### Exemplo de Criação de Avaliação

```json
POST /api/avaliacoes
Content-Type: application/json
Authorization: Bearer {token}

{
  "idDepartamento": 1,
  "idNivelEstresse": 3,
  "comentario": "Dia muito estressante, muitas reuniões"
}
```

## 📊 Estrutura do Banco de Dados

### T_ZF_DEPARTAMENTO

- ID_DEPTO (NUMBER) - Primary Key
- NOME (VARCHAR 100) - Not Null
- DESCRICAO (VARCHAR 500)
- DATA_CRIACAO (TIMESTAMP)
- DATA_ATUALIZACAO (TIMESTAMP)

### T_ZF_NIVEL_ESTRESSE

- ID_NIVEL_ESTRESSE (NUMBER) - Primary Key
- NIVEL (NUMBER) - Not Null, Unique (1-5)
- DESCRICAO (VARCHAR 200) - Not Null
- DATA_CRIACAO (TIMESTAMP)
- DATA_ATUALIZACAO (TIMESTAMP)

### T_ZF_AVALIACAO

- ID_AVALIACAO (NUMBER) - Primary Key
- ID_DEPTO (NUMBER) - Foreign Key para T_ZF_DEPARTAMENTO
- ID_NIVEL_ESTRESSE (NUMBER) - Foreign Key para T_ZF_NIVEL_ESTRESSE
- COMENTARIO (VARCHAR 1000)
- DATA_AVALIACAO (TIMESTAMP) - Not Null
- DATA_CRIACAO (TIMESTAMP)
- DATA_ATUALIZACAO (TIMESTAMP)

### Relacionamentos

**T_ZF_DEPARTAMENTO ↔ T_ZF_AVALIACAO**

- **Tipo**: Um-para-Muitos (1:N)
- **Relacionamento**: Um departamento pode ter múltiplas avaliações
- **Constraint**: `T_ZF_AVALIACAO.ID_DEPTO` é Foreign Key para `T_ZF_DEPARTAMENTO.ID_DEPTO`

**T_ZF_NIVEL_ESTRESSE ↔ T_ZF_AVALIACAO**

- **Tipo**: Um-para-Muitos (1:N)
- **Relacionamento**: Um nível de estresse pode estar em múltiplas avaliações
- **Constraint**: `T_ZF_AVALIACAO.ID_NIVEL_ESTRESSE` é Foreign Key para `T_ZF_NIVEL_ESTRESSE.ID_NIVEL_ESTRESSE`

### Procedures e Functions Oracle

O sistema utiliza procedures e functions Oracle para operações específicas:

- **PC_INSERIR_AVALIACAO**: Procedure para inserir avaliações
- **FC_CALCULAR_MEDIA_SEMANAL**: Function para calcular média semanal de estresse
- **FC_VALIDAR_NIVEL_ESTRESSE**: Function para validar nível de estresse
- **FC_CONTAR_REGISTROS_PERIODO**: Function para contar registros em um período

## 🔧 Configurações Avançadas

### Logs

O projeto está configurado para logs detalhados em desenvolvimento:

- SQL queries
- Parâmetros de binding
- Requests HTTP
- Logs de autenticação JWT

### Paginação

Todos os endpoints de listagem suportam paginação:

- `page`: Número da página (padrão: 0)
- `size`: Tamanho da página (padrão: 10)

### Validações

- Campos obrigatórios
- Tamanhos máximos de campos
- Validação de níveis de estresse (1-5)
- Formato de datas

### Segurança

- Autenticação JWT obrigatória para endpoints protegidos
- Endpoints públicos: `/api/auth/login`, `/swagger-ui.html`, `/api-docs`
- Tokens JWT com expiração configurável (padrão: 24 horas)
- Chave secreta JWT configurável via `application.properties` ou variáveis de ambiente

## 📚 Documentação Adicional

- **[TESTE_JWT.txt](TESTE_JWT.txt)**: Instruções detalhadas sobre como testar a autenticação JWT
- **[testes.md](testes.md)**: Documentação adicional de testes
- **Swagger UI**: Acesse `http://localhost:8080/swagger-ui.html` para documentação interativa da API

## 📝 Próximos Passos

- [ ] Deploy em nuvem (Azure/Heroku/Railway)
- [ ] Testes unitários
- [ ] CI/CD Pipeline
- [ ] Dashboard de métricas
- [ ] Relatórios de bem-estar

## 👥 Autores

- Bruno Cantacini - RM560242

## 📄 Licença

Este projeto é parte do trabalho acadêmico da FIAP.
