# TODO: Switch Database to MySQL

- [x] Edit pom.xml: Remove H2 and MongoDB dependencies, add MySQL connector
- [x] Update application.properties: Remove MongoDB config, add MySQL datasource with username=root, password=root
- [x] Refactor User.java: Change from MongoDB @Document to JPA @Entity
- [x] Refactor Item.java: Change from MongoDB @Document to JPA @Entity
- [x] Refactor UserDao.java: Change from MongoRepository to JpaRepository
- [x] Refactor ItemRepository.java: Change from MongoRepository to JpaRepository
- [x] Test the application for MySQL connectivity and functionality
