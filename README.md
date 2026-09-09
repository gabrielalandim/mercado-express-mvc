# 🛒 Mercado Express — Web MVC

Aplicação **Spring Boot + Spring MVC + Thymeleaf** para gerenciamento de produtos de um mercado *express* (frutas, produtos de limpeza, higiene, etc.), com persistência em banco de dados **Oracle** e autenticação via **Spring Security**.

Projeto desenvolvido para o **Checkpoint 4 – Parte 2 (Spring Web / MVC, Security e Deploy)** da disciplina de **Java Advanced**, do curso de **Tecnologia em Análise e Desenvolvimento de Sistemas (TDS)** — FIAP.

> Este repositório é independente do repositório da Parte 1 (API REST com HATEOAS).

> *"Quem ouve, esquece. Quem vê, lembra. Quem faz, aprende."*

---

## 📌 Sobre o Projeto

Diferente da Parte 1 (API REST consumida via Postman/Insomnia), esta Parte 2 entrega uma **interface Web completa**, renderizada no servidor com **Thymeleaf**, permitindo cadastrar, listar, editar e excluir produtos diretamente pelo navegador, através de links e botões.

Os dados são persistidos no mesmo banco **Oracle** utilizado na Parte 1, na tabela `TDS_MVC_TB_mercado`.

---

## 🧰 Tecnologias Utilizadas

| Tecnologia | Finalidade |
|---|---|
| **Java 21** | Linguagem de programação |
| **Spring Boot** | Framework principal da aplicação |
| **Spring MVC** | Controllers web e roteamento das páginas |
| **Thymeleaf** | Motor de templates para renderização do HTML |
| **Spring Security** | Autenticação e definição de rotas públicas/privadas |
| **Spring Data JPA** | Persistência e abstração de acesso ao banco de dados |
| **Lombok** | Redução de código boilerplate (`@Data`) |
| **Oracle JDBC (ojdbc11)** | Driver de conexão com o banco Oracle |
| **Banco de Dados Oracle** | Ambiente `ORACLE_FIAP` |
| **Maven** | Gerenciador de dependências e build |
| **Docker** | Containerização da aplicação para deploy |

---

## 🗂️ Estrutura do Projeto

```
mercado-express-mvc/
├── src/
│   ├── main/
│   │   ├── java/br/com/fiap/mercadoexpress/
│   │   │   ├── config/
│   │   │   │   └── SecurityConfig.java        # Regras de autenticação e rotas públicas/privadas
│   │   │   ├── controllers/
│   │   │   │   ├── ProdutoWebController.java  # Endpoints MVC (Create, Read, Update, Delete)
│   │   │   │   └── AuthController.java        # Página de login customizada
│   │   │   ├── dtos/
│   │   │   │   └── ProdutoRequestDTO.java
│   │   │   ├── models/
│   │   │   │   └── Produto.java               # Entidade JPA (tabela TDS_MVC_TB_mercado)
│   │   │   ├── repositories/
│   │   │   │   └── ProdutoRepository.java
│   │   │   ├── services/
│   │   │   │   └── ProdutoService.java
│   │   │   └── MercadoexpressApplication.java
│   │   └── resources/
│   │       ├── static/css/style.css           # Estilo visual da aplicação
│   │       ├── templates/
│   │       │   ├── lista.html                 # Tela de listagem (Read)
│   │       │   ├── form.html                  # Tela de cadastro/edição (Create/Update)
│   │       │   └── login.html                 # Tela de login customizada
│   │       └── application.properties
│   └── test/
├── Dockerfile
├── pom.xml
└── README.md
```

### Arquitetura em camadas

```
Navegador (HTML)  <--- HTTP --->  Controller  --->  Service  --->  Repository  --->  Banco Oracle (TDS_MVC_TB_mercado)
```

- **Controller (`ProdutoWebController`)**: recebe as requisições do navegador e devolve as views Thymeleaf (`lista`, `form`).
- **Service (`ProdutoService`)**: contém a regra de negócio e delega ao Repository o *commit* no banco.
- **Repository**: interface `JpaRepository`, comunicação direta com o Oracle via JPA/Hibernate.
- **Model (`Produto`)**: entidade `@Entity` mapeada para `TDS_MVC_TB_mercado`.
- **SecurityConfig**: define quais rotas exigem login e qual página de login é usada.

---

## 🗄️ Modelagem do Banco de Dados

**Tabela:** `TDS_MVC_TB_mercado`

| Coluna | Tipo (Java) | Descrição |
|---|---|---|
| `id` | `Long` | Identificador único, gerado automaticamente (`IDENTITY`) |
| `nome` | `String` | Nome do produto |
| `tipo` | `String` | Tipo/categoria do produto |
| `setor` | `String` | Setor do mercado onde o produto se encontra |
| `tamanho` | `String` | Tamanho/porção do produto |
| `preco` | `Double` | Preço do produto |

A tabela é criada/atualizada automaticamente pelo Hibernate (`spring.jpa.hibernate.ddl-auto=update`).

---

## 🔐 Segurança (Spring Security)

A aplicação define **rotas públicas** e **rotas privadas**:

| Rota | Acesso | Descrição |
|---|---|---|
| `GET /produtos` | 🔓 Público | Qualquer visitante pode ver a lista de produtos |
| `/login` | 🔓 Público | Página de autenticação |
| `/css/**` | 🔓 Público | Arquivos estáticos (estilo) |
| `GET /produtos/novo` | 🔒 Privado | Formulário de cadastro exige login |
| `POST /produtos` | 🔒 Privado | Criar/atualizar produto exige login |
| `GET /produtos/editar/{id}` | 🔒 Privado | Formulário de edição exige login |
| `GET /produtos/excluir/{id}` | 🔒 Privado | Excluir produto exige login |

**Usuário de teste (em memória):**
```
usuário: admin
senha:   1234
```

> ⚠️ Este usuário fixo (`InMemoryUserDetailsManager`) é apenas para fins didáticos do checkpoint.

---

## ⚙️ Configuração (application.properties)

```properties
server.port=${PORT:8082}

spring.datasource.url=${DB_URL:jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver

spring.jpa.hibernate.ddl-auto=update
```

> 🔒 As credenciais do Oracle **não ficam fixas no código** (evita expor usuário/senha no GitHub público).
> Defina as variáveis de ambiente `DB_USER` e `DB_PASSWORD` com o RM e senha do ambiente `ORACLE_FIAP`:
> - **Local (IntelliJ/Eclipse/NetBeans):** configure as variáveis de ambiente na *Run Configuration*.
> - **Local (terminal):** `export DB_USER=SEU_RM DB_PASSWORD=SUA_SENHA && ./mvnw spring-boot:run`
> - **Deploy (Render/Fly.io):** cadastre `DB_USER` e `DB_PASSWORD` (e `PORT`, se exigido pela plataforma) na seção de variáveis de ambiente do serviço.

---

## ▶️ Como Executar Localmente

### Pré-requisitos
- Java 21 instalado
- Maven (ou usar o wrapper `mvnw` incluso no projeto)
- Acesso ao banco Oracle `ORACLE_FIAP`

### Passos

```bash
git clone <link-deste-repositorio>
cd mercado-express-mvc

export DB_USER=SEU_RM
export DB_PASSWORD=SUA_SENHA

./mvnw spring-boot:run
```

A aplicação ficará disponível em:
```
http://localhost:8082/produtos
```

### Executando via Docker

```bash
docker build -t mercado-express-mvc .
docker run -p 8082:8082 -e DB_USER=SEU_RM -e DB_PASSWORD=SUA_SENHA mercado-express-mvc
```

---

## 🌐 Deploy

<!-- TODO: preencher após o deploy -->
🔗 **URL de produção:** `https://SEU-LINK-AQUI`
🛠️ **Plataforma utilizada:** `Render` / `Fly.io` (indicar qual foi usada)

Ao publicar, lembre-se de configurar `DB_USER` e `DB_PASSWORD` nas variáveis de ambiente da plataforma escolhida.

---

## 🖥️ Funcionalidades (CRUD via Interface Web)

### 🔹 READ — Listagem de produtos (`GET /produtos`)

Tela pública que lista todos os produtos cadastrados, com botões de **Editar** e **Excluir** visíveis apenas para usuários autenticados.

<!-- TODO: inserir print da tela de listagem -->
`![Listagem de produtos](docs/lista.png)`

### 🔹 CREATE — Cadastro de novo produto (`GET /produtos/novo` + `POST /produtos`)

Formulário (acesso restrito a usuários logados) para cadastrar um novo produto. Ao salvar, o `ProdutoService` persiste o registro na tabela `TDS_MVC_TB_mercado`.

<!-- TODO: inserir print do formulário de cadastro -->
`![Cadastro de produto](docs/form-novo.png)`

### 🔹 UPDATE — Edição de produto (`GET /produtos/editar/{id}` + `POST /produtos`)

O mesmo formulário é reaproveitado para edição: o campo `id` (oculto) identifica o registro, e o `POST /produtos` atualiza o produto existente.

<!-- TODO: inserir print do formulário de edição preenchido -->
`![Edição de produto](docs/form-editar.png)`

### 🔹 DELETE — Exclusão de produto (`GET /produtos/excluir/{id}`)

Link de exclusão disponível apenas para usuários logados, com confirmação via `confirm()` antes de remover o produto do banco.

<!-- TODO: inserir print da confirmação de exclusão -->
`![Exclusão de produto](docs/excluir.png)`

### 🔹 Login

Tela de autenticação customizada, necessária para acessar as funcionalidades de criação, edição e exclusão.

<!-- TODO: inserir print da tela de login -->
`![Tela de login](docs/login.png)`

---

## 🎥 Vídeo de Demonstração

<!-- TODO: inserir link do vídeo (~5 minutos) mostrando o CRUD completo pela interface Web -->
🔗 **Link do vídeo:** `https://...`

---

## 👤 Autores

| Nome | RM |
|---|---|
| Maria Gabriela Landim Severo | 565146 |
| Samara Porto Souza | 559072 |

**IDE utilizada:** IntelliJ IDEA

---

## 📄 Licença

Projeto acadêmico desenvolvido para fins educacionais na disciplina de Java Advanced — FIAP.
