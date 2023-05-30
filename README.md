<h1 align="center">MyNotes - API</h1>

---

## 🔭 Visão Geral

MyNotes é uma aplicação web de gestão de anotações, no qual o usuário pode gerenciar e organizar suas anotações, através da criação de novas anotações, agrupa-las em diferentes coleções, assim como categoriza-las e modificá-las, de modo a atender suas necessidades.

### 🗃 Tecnologias

| ⚒️ Tecnologia | Versão |
|------------|----------|
| ☕ Java     | 17.0.0   |
| 🌱 Spring  | 3.1.0-M2   |

---

## 📋 Pré-requisitos

- 🐋 Docker | Versão: X.XX.X
- 🐳 Docker compose | Versão: X.XX.X
- 🖇️ Git CLI | qualquer versão

---

## 📥 Instalação

Antes de qualquer coisa, você precisa clonar o repositório no seu aparelho local. Já estando no diretório desejado, você somente precisa rodar no seu terminal o seguinte comando: 

```
git clone https://github.com/rodrigsmor/mynotes-api.git
```

---

## ⚙️ Configuração 

Para estar apto a rodar a aplicação e testa-la, você precisa fazer algumas configurações iniciais. 

Configurando as variáveis de ambientes do seu banco de dados.

```
DB_URL=jdbc:mysql://mysql:3306/<your-database-name>
DB_USERNAME=<your-database-user>
DB_PASSWORD=<-your-user-database-password>
DB_DATABASE=<your-database-name>
```

Uma vez que as configurações do seu banco de dados estão feitas, agora coloque os valores das variáveis de ambientes relacionadas a segurança (JWT) da sua API. Você pode escolher qualquer valor para ambas as variáveis de ambiente, somente tenha em mente que devem reforçar a segurança de sua API.

```
JWT_ISSUER=<your-jwt-issuer>
JWT_SECRET_KEY=<your-secret-key>
```

POr fim, precisa fazer as configurações relativas ao seu Google Firebase, para isso, será necessário criar uma conta ou se conectar a uma conta existe e então fazer algumas configurações. Pode encontrar mais informações no seguinte link.

```
FIREBASE_SERVICE_ACCOUNT_KEY=<diretório-do-arquivo-service-json>
FIREBASE_DOWNLOAD_URL=
FIREBASE_STORAGE_PROJECT_ID=
FIREBASE_BUCKET_NAME=om

# imagens padrões. Eu utilizei imagens armazenadas no Firebase, mas você não precisa utiliza-las
DEFAULT_FAVORITE_COVER=
DEFAULT_COLLECTION_COVER=
DEFAULT_NOTES_COVER=
DEFAULT_NOTES_ICON=
```


---

## 🚀 Como rodar?

---

## ✨ Recursos

- Exclusão automática dos itens na lixeira após 30 dias de sua exclusão, usando o Scheduled do Spring.
- Notificações: O usuário pode 


Usar o swagger