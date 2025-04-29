# CardVault
Test task for Java internship in EffectiveMobile

### How to run
1. Install git and Docker
2. Clone repo, change directory
    ```
   cd CardVault
   ```
3. Add usernames and passwords:
```
    echo SPRING_DATASOURCE_URL=jdbc:postgresql://cardvault-db-host:5432/cardvault-db>> src/main/resources/cardvault-app.env
    echo SPRING_DATASOURCE_USERNAME=cardvault>> src/main/resources/cardvault-app.env 
    echo SPRING_DATASOURCE_PASSWORD=cardvault>> src/main/resources/cardvault-app.env
```
   
   ```
     echo POSTGRES_USER=cardvault>>postgres/cardvault-db.env
     echo POSTGRES_PASSWORD=cardvault>>postgres/cardvault-db.env
     echo POSTGRES_DB=cardvault-db>>postgres/cardvault-db.env
     echo PGDATA=/var/lib/postgresql/data>>postgres/cardvault-db.env
   ```

4. Run
```
    docker compose up
  ```