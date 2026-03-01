# Authorization Service

## Local Setup

1. PostgreSQL Database Setup
2. Spring Boot Backend Application Setup


### PostgresSQL Database Setup

You can setup postgresql on your linux OS using [this](https://github.com/appu912/software-development-setup/blob/main/ubuntu-postgresql-setup.md). Now we'll setup the authorization database using below instructions.
So from your `postgres` user shell create the `auth-db` using the `createdb` command.

```console
postgres@ubuntu:~$ createdb auth-db
```
You can verify this using the below commands.

```console
postgres@apoorv:~$ psql -c '\l'
                                                   List of databases
   Name    |  Owner   | Encoding | Locale Provider | Collate |  Ctype  | ICU Locale | ICU Rules |   Access privileges
-----------+----------+----------+-----------------+---------+---------+------------+-----------+-----------------------
 auth-db   | postgres | UTF8     | libc            | C.UTF-8 | C.UTF-8 |            |           |
 postgres  | postgres | UTF8     | libc            | C.UTF-8 | C.UTF-8 |            |           |
 template0 | postgres | UTF8     | libc            | C.UTF-8 | C.UTF-8 |            |           | =c/postgres          +
           |          |          |                 |         |         |            |           | postgres=CTc/postgres
 template1 | postgres | UTF8     | libc            | C.UTF-8 | C.UTF-8 |            |           | =c/postgres          +
           |          |          |                 |         |         |            |           | postgres=CTc/postgres
(4 rows)
```
You can see that the `auth-db` is created and it is created by OS user `postgres`.
Now enter `auth-db` in interactive mode using `psql` using the `postgres` user.

```console
postgres@ubuntu:~$ psql -d auth-db
```
After getting inside interactive mode, we can create our schema `auth`.

```console
auth-db=# CREATE SCHEMA auth;
```
Verify if the schema is created using the below command.
```console
auth-db=# \dn
      List of schemas
  Name  |       Owner
--------+-------------------
 auth   | postgres
 public | pg_database_owner
(2 rows)
```
Though the owner of the schema is `postgres` user, you can also do that manually using the below command.

```console
auth-db=# ALTER SCHEMA auth OWNER TO postgres;
```
Now create the `user_credentials` table. Execute the create table [query](data/auth/create-auth-credentials-table.sql). After you can verify if the `user_credentials` table is created or not.

```console
auth-db=# \dt auth.*
              List of relations
 Schema |       Name       | Type  |  Owner
--------+------------------+-------+----------
 auth   | user_credentials | table | postgres
```
We this, we have our database, schema and table ready. Now we want our authorization data service to connect to the database. For that, it will use a user. So, we create an application level user with read/write permissions and use it to connect to database. This user will have a login role and read write permissions. It does not have create and drop table level permissions, which will prevent any attacks related to SQL Injection.

```console
auth-db=# CREATE ROLE auth_user_rw WITH LOGIN PASSWORD 'your-password'
```
We can check if the user is created or not with the following command.

```console
auth-db=# \du
                               List of roles
  Role name   |                         Attributes
--------------+------------------------------------------------------------
 auth_user_rw |
 postgres     | Superuser, Create role, Create DB, Replication, Bypass RLS
```
Now we want this user to connect to our `auth-db`, has access to use our `auth` schema and has permissions to read/write our `user_credentials` table.

```console
GRANT CONNECT ON DATABASE "auth-db" TO auth_user_rw;
GRANT USAGE ON SCHEMA auth TO auth_user_rw;
GRANT SELECT, INSERT, UPDATE, DELETE ON auth.user_credentials TO auth_user_rw;
```
Verify if the user has schema usage privileges. Output should be t.

```console
auth-db=# SELECT has_schema_privilege('auth_user_rw', 'auth', 'USAGE');
 has_schema_privilege
----------------------
 t
(1 row)
```
Also check if the user has read/write permissions on `user_credentials` table.

```console
auth-db=> \dp auth.user_credentials
                                       Access privileges
 Schema |       Name       | Type  |     Access privileges      | Column privileges | Policies
--------+------------------+-------+----------------------------+-------------------+----------
 auth   | user_credentials | table | postgres=arwdDxt/postgres +|                   |
        |                  |       | auth_user_rw=arwd/postgres |                   |
(1 row)
```
We can see that on schema `auth` and table `user_credentials` user `auth_user_rw` has `arwd` permissions given by user `postgres`. Here `a` is for insert, `r` is for read, `w` is for write and `d` is for delete. So our user has the necessary permissions.

**NOTE:** *If you create new tables in the future, this user won't have read/write permissions to that table.*

Now through SQL Injection, someone can use this user to `CREATE` and `DROP` tables. So we should revoke these permissions. First use the below command to check if these permissions are enabled or not.

```console
auth-db=# \dn+ auth
                     List of schemas
 Name |  Owner   |    Access privileges    | Description
------+----------+-------------------------+-------------
 auth | postgres | postgres=UC/postgres   +|
      |          | auth_user_rw=U/postgres |
(1 row)
```
You can see that on `auth` schema user `auth_user_rw` has usage(U) permission given by `postgres` user. It does not have `C`, which means this user cannot create or drop table. If you have `UC`, then revoke `C` using the below command:

```console
auth-db=# REVOKE CREATE ON SCHEMA auth FROM auth_user_rw;
```
After doing this, lets verify

```console
auth-db=> SET ROLE auth_user_rw;
SET
auth-db=> SELECT * FROM auth.user_credentials;
 user_id
---------
(0 rows)

auth-db=> DROP TABLE auth.user_credentials;
ERROR:  must be owner of table user_credentials 
```
You can see that we can make a `SELECT` query using `auth_user_rw` but cannot drop the table. Now we are all set!!

### Spring Boot Backend Application Setup

After setting up your database, clone this repo on your system and do a gradle build.

```console
gradle clean build --refresh-dependencies --no-build-cache --console=verbose
```
If the build is successful, please use the jar command to run your application.

```console
java -DUSERNAME=auth_user_rw -DPASSWORD=auth_user_rw -jar build/libs/authorization-service-1.0.0-SNAPSHOT.jar
```


