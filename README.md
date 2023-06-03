<div align="center">
    <img src="https://camo.githubusercontent.com/7943347007726a0b0cb33be34990c59a74a5dfeee27bc8376768da1c917163e2/68747470733a2f2f692e6962622e636f2f744c42435956672f566563746f722d312e706e67" alt="MyNotes logo" height="84px" width="84px" align="center" />
</div>

<h1 align="center">MyNotes - API</h1>

---

## 🔭 Overview

MyNotes is a note management web application, in which the users can manage and organize his notes by creating new notes, grouping them into different collections, as well as categorizing and modifying them to suit their needs.

### 🗃 Technologies

| ⚒️ Technology | Version  |
|---------------|----------|
| ☕ Java        | 17.0.0   |
| 🌱 Spring     | 3.1.0-M2 |

---

## 📋 Pre-requisites

- 🖇️ Git CLI | Any version
- 🐋 Docker | Any version (I'm using it from version 20.10.21)
- 🐳 Docker compose | Any version (I'm using it from version: 1.25.0)

---

## 📥 Installation

First of all, you need to clone the repository on your personal device. Being located in the desired directory, simply run the following command in your terminal:
```
git clone https://github.com/rodrigsmor/mynotes-api.git
```

---

## ⚙️ Settings

To be able to run the application and also test it, you need to make some initial settings.

setting the environment variables for your database.

```
DB_URL=jdbc:mysql://mysql:3306/<your-database-name>
DB_USERNAME=<your-database-user>
DB_PASSWORD=<-your-user-database-password>
DB_DATABASE=<your-database-name>
```

Once the database settings are done, you will now set values for the security environment variables (JWT). You can choose any value for both environment variables, but note that these values should enforce the security of your API.

```
JWT_ISSUER=<your-jwt-issuer>
JWT_SECRET_KEY=<your-secret-key>
```

You now need to make Google Firebase related settings. To do this you will need to create an account or log into an existing account. For more information, please click [here](https://firebase.google.com/docs/storage/web/start).

```
FIREBASE_SERVICE_ACCOUNT_KEY=directory-file-service-json
FIREBASE_DOWNLOAD_URL=https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media
FIREBASE_STORAGE_PROJECT_ID=the identificator of your firebase project
FIREBASE_BUCKET_NAME=the bucket name of your project

# default images. Note that all these images are URLS.

DEFAULT_FAVORITE_COVER=Default Favorite Collection cover Url
DEFAULT_COLLECTION_COVER=Default Collection Cover Url
DEFAULT_NOTES_COVER=Default Annotation Cover Url
DEFAULT_NOTES_ICON=Default Annotation Icon Url
```

With these settings, your application is likely to work well.

---

## 🚀 How to run?

Having already set the environment variables and made the necessary Docker adaptations, simply run the following command in your terminal:

```
docker-compose up --build
```

---

## ✨ Features

- Storing files in Firebase storage.
- Using Swagger to provide API documentation.
- Real-time notifications using Kafka and WebSocket.
- Automatic deletion of items in the recycle garbage can after 30 days of their deletion, using Spring Scheduled.

---

## 🌎 Support links

- 🔗 Firebase setup: https://firebase.google.com/docs/admin/setup#java_2
- 🔗 Install Docker: https://docs.docker.com/engine/install/
- 🔗 Knows docker-compose: https://docs.docker.com/compose/
- 🔗 Knows Kafka: https://kafka.apache.org/intro

---

## 🧑🏾‍💻 Author

<img src="https://avatars.githubusercontent.com/u/78985382?v=4" alt="Profile picture of Rodrigo Moreira"  height="100px" border-radius="100%" />

### Rodrigo Moreira ☁️
Developed with 💜 by **Rodrigo Moreira** ⌨️🖱️
