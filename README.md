<div align="center">
    <img src="https://camo.githubusercontent.com/7943347007726a0b0cb33be34990c59a74a5dfeee27bc8376768da1c917163e2/68747470733a2f2f692e6962622e636f2f744c42435956672f566563746f722d312e706e67" alt="MyNotes logo" height="84px" width="84px" align="center" />
</div>

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

- 🖇️ Git CLI | qualquer versão
- 🐋 Docker | Versão: 20.10.21
- 🐳 Docker compose | Versão: 1.25.0

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

Por fim, precisa fazer as configurações relativas ao seu Google Firebase, para isso, será necessário criar uma conta ou se conectar a uma conta existente e então fazer algumas configurações. Pode encontrar mais informações no seguinte link.

```
FIREBASE_SERVICE_ACCOUNT_KEY=<diretório-do-arquivo-service-json>
FIREBASE_DOWNLOAD_URL=https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media
FIREBASE_STORAGE_PROJECT_ID=the identificator of your firebase project
FIREBASE_BUCKET_NAME=the bucket name of your project

# imagens padrões. Atente-se ao fato de que todas essas imagens são URLS.
DEFAULT_FAVORITE_COVER=<essa deve ser a imagem padrão de capa das coleções de favoritos>
DEFAULT_COLLECTION_COVER=<essa deve ser a imagem padrão das coleções>
DEFAULT_NOTES_COVER=<essa deve ser a imagem padrão das anotações>
DEFAULT_NOTES_ICON=<essa deve ser o icone padrão das anotações>
```

Dado essas configurações, muito que provavelmente a aplicação funcionará bem.

---

## 🚀 Como rodar?

Já tendo configurado as variáveis de ambiente e também feito as devidas adaptações do Docker, somente irá precisar rodar em seu terminal o comando abaixo:

```
docker-compose up --build
```

---

## ✨ Recursos

- Armazenamento de documentos no Firebase Storage.
- Notificação em tempo real utilizando Kafka e WebSocket.
- Utilização de Swagger para disponibilização da documentação da API.
- Exclusão automática dos itens na lixeira após 30 dias de sua exclusão, usando o Scheduled do Spring.

---

## 🌎 Links de apoio

- 🔗 Setup do firebase: https://firebase.google.com/docs/admin/setup#java_2
- 🔗 Install Docker: https://docs.docker.com/engine/install/
- 🔗 Knows docker-compose: https://docs.docker.com/compose/
- 🔗 Knows Kafka: https://kafka.apache.org/intro

---

## 🧑🏾‍💻 Author

<img src="https://avatars.githubusercontent.com/u/78985382?v=4" alt="Profile picture of Rodrigo Moreira"  height="100px" border-radius="100%" />

### Rodrigo Moreira ☁️

