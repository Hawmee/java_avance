
# JAVA AVANCE

Un projet JAVA basee sur SWING , MYSQL , NodeJS , et PRISMA


## PRE-REQUIS

 - [NodeJS](https://nodejs.org/en/download)
 - [Yarn](https://classic.yarnpkg.com/lang/en/docs/install/#debian-stable) (de preference)
 - [Java](https://www.java.com/fr/download/manual.jsp)
 - [Mysql](https://www.mysql.com/downloads/)
 - SWING UI DESIGNER


## Initialisations

Installation des dependences backend

```bash
  cd backend_Swing
  yarn
  # ou 
  npm install
```

Generer la base de donnee

```bash
  yarn prisma migrate reset
  yarn prisma migrate dev  #ou  yarn prisma db push
  # ou
  npx prisma migrate reset
  npx prisma migrate dev  #ou  yarn prisma db push  
```

Connexion vers la BDD :(prisma/schema.prisma)
```bash
datasource db {
  provider = "SGBD"
  url      = "SGBD://utilisateur:motdepasse@lien_vers_SGBD:port/nom_base_de_donnee"
}
```

exemple de connexion ves BGG:
```bash
datasource db {
  provider = "mysql"
  url      = "mysql://root:@localhost:3306/java_av"
}
```

    
## Deployment

Backend:

```bash
  node index.js
  # ou  
  yarn dev   (si yarn est le pakage manager utilise)

```

