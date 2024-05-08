# Building RESTful WS using Spring Boot

Banuprakash C

Full Stack Architect, Corporate Trainer

Co-founder & CTO: Lucida Technologies Pvt Ltd., 

https://www.lucidatechnologies.com/

https://www.linkedin.com/in/banu-prakash-50416019/


Emails: 
banuprakashc@yahoo.co.in

banuprakash.cr@gmail.com

banu@lucidatechnologies.com

Repository for Training:
https://github.com/BanuPrakash/Spring

===================================

Softwares Required:
1)  openJDK 17
https://jdk.java.net/java-se-ri/17

2) IntelliJ Ultimate edition 
https://www.jetbrains.com/idea/download/?section=mac

OR

Eclipse for JEE  
	https://www.eclipse.org/downloads/packages/release/2022-09/r/eclipse-ide-enterprise-java-and-web-developers

3) MySQL  [ Prefer on Docker]

Install Docker Desktop

Docker steps:

```
a) docker pull mysql

b) docker run --name local-mysql –p 3306:3306 -e MYSQL_ROOT_PASSWORD=Welcome123 -d mysql

container name given here is "local-mysql"

For Mac:
docker run -p 3306:3306 -d --name local-mysql -e MYSQL_ROOT_PASSWORD=Welcome123 mysql


c) CONNECT TO A MYSQL RUNNING CONTAINER:

$ docker exec -t -i local-mysql bash

d) Run MySQL client:

bash terminal> mysql -u "root" -p

mysql> exit

```

4. POSTMAN

=====================

* Introduction to Spring and Spring Boot
* Spring Data JPA 
* Building Restful WS using Spring Boot
* AOP
* Exception handling and validation
* Testing REST Controller
* Caching [ETag, CacheManager, RedisCacheManager]
* Async and Reactive programming
* Security
* MicroServices 

Introduction to Spring and Spring Boot
--------------------------------------

SOLID Design Principle:

D --> Dependency Injection

Spring Framework:
* lightweight framework based on Inversion Of Control to provide dependency.
* Loose Coupling
* Modularized: web-mvc, Data JPA, templates 

Servlet Container --> Web components  
EJB Container -> EJBeans

public void doGet(HttpServletRequest req, HttpServletResponse res) throws Exception {
	req.getParameter(...)
}

----
Bean: An object managed by Spring container

XML as metadata:

```
interface BookDao {
	void addBook(Book b);
}

public class BookDaoMongoImpl implements BookDao {
	public void addBook(Book b) {...}
}

public class BookDaoMySQlImpl implements BookDao {
	public void addBook(Book b) {...}
}

public class AppService {
	private BookDao bookDao; // not tightly coupled to impl
	public void setDao(BookDao dao) {
		this.bookDao = dao;
	}

	public void insert(Book b) {
		this.bookDao.addBook(b);
	}
}

beans.xml
<beans>
	<bean id="mysql" class="pkg.BookDaoMySQlImpl" />
	<bean id="mongo" class="pkg.BookDaoMongoImpl" />
	<bean id="service" class="pkg.AppService">
		<property name="dao" ref="mongo" /> <!-- setter DI -->
		OR
		<constructor index="0" ref="mongo" /> <!-- Constructor DI -->
	</bean>
</beans>

ApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");

AppService ser = ctx.getBean("service", AppService.class);
```

Annotation as meta-data:

```
interface BookDao {
	void addBook(Book b);
}

// name of object "bookDaoMongoImpl"
@Repository
public class BookDaoMongoImpl implements BookDao {
	public void addBook(Book b) {...}
}

// name of object "appService"
@Service
public class AppService {

	@AutoWired 
	or
	@Inject
	private BookDao bookDao; // not tightly coupled to impl
	
	public void insert(Book b) {
		this.bookDao.addBook(b);
	}
}

ApplicationContext ctx = new AnnotationConfigApplicationContext();
ctx.componentScan("com.adobe");
ctx.refresh();

AppService ser = ctx.getBean("appService", AppService.class);
```

Spring instantiates beans which has one of these annotations at class-level:
1) @Component
2) @Repository
https://github.com/spring-projects/spring-framework/blob/main/spring-jdbc/src/main/resources/org/springframework/jdbc/support/sql-error-codes.xml
3) @Service
4) @Configuration
5) @Controller
6) @RestController
7) @ControllerAdvice

Wiring can be done using:
1) @Autowired
2) @Inject
3) Constructor DI

try {

}catch(SQLException ex) {
	if(ex.getErrorCode() == 1501) {

	} else if(ex.getErrorCode() == 2100) {
		
	}
}
=====================================

Guice by Google is alternate

Spring Boot is a framework on top of Spring Framework
For Example
Spring Boot 2.x is built on top of Spring Framework 5.x
Spring Boot 3.x is built on top of Spring Framework 6.x

Why Spring Boot?
1) Simplifies development
2) Most of the configurations are provided out of the box
3) Highly opiniated framework
4) Ready for dockerization

For Example if we are using ORM, spring boot configures database connection pooling and Hibernate out of the box

If we are building web applications: Tomcat Embedded web container is configured out of the box

=============

 <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
</dependency>
```
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
```
@SpringBootApplication is 3 in one:
1) @Configuration
	--> helps instantiate DemoApplication as demoApplication
	--> it is also a configuration object
2) @EnableAutoConfiguration
	depending on context create built-in beans like:
	TomcatEmbeddedServer , HibernateJpaProvider, HikariCP, ...
3) @ComponentScan
	by default it scans classes present in package [com.adobe.demo] where "main" is present and it's sub-packages	

	Example: if I write class in "com.example"; that class is not scanned and bean is not created

	@ComponentScan("com.pkg1", "com.pkg2")
--
SpringApplication.run(DemoApplication.class, args); is same as AnnotationConfigApplicationContext


Spring is using ByteCode Instrumentation libraries:
CGLib, Java Assist, Byte Buddy

```
Auto-wiring by type:
Field bookDao in com.adobe.demo.service.AppService required a single bean, but 2 were found:
	- bookDaoMongoImpl: 
	- bookDaoSqlImpl:

Solution 1: using @Primary
mark one of the bean as @Primary

Solution 2: using @Qualifier at field level
	@Autowired
    @Qualifier("bookDaoMongoImpl")
    private BookDao bookDao;

This is better if both Implemenations are used in application

Service 1 use Mongo
Service 2 use SQL

@Repository("sql")
public class BookDaoSqlImpl implements BookDao{

@Autowired
@Qualifier("sql")
private BookDao bookDao;

Solution 3: using @Profile

@Profile("dev")
@Repository("sql")
public class BookDaoSqlImpl implements BookDao{

@Profile("prod")
@Repository("mongo")
public class BookDaoMongoImpl implements BookDao{

a) 
application.properties
spring.profiles.active=prod

b) Program arguments
More Run/Debug --> Modify Configurations --> Active Profile: dev or prod

Program Arguments --> Environemnt variables --> application.properties

Solution 4: using custom proerties
application.properties
DAO=SQL

@ConditionalOnProperty(name ="DAO", havingValue = "MONGO")
@Repository("mongo")
public class BookDaoMongoImpl implements BookDao{

@ConditionalOnProperty(name ="DAO", havingValue = "SQL")
@Repository("sql")
public class BookDaoSqlImpl implements BookDao{

Solution 5: @ConditionalOnMissingBean

@ConditionalOnMissingBean(name="sql")
@Repository("mongo")
public class BookDaoMongoImpl implements BookDao{

```

Factory Method using @Bean
Include in pom.xml
com.mchange:c3p0:0.10.0

```
@Configuration
public class AppConfig {

@Bean("postgres-cp")
public DataSource getDataSource() {
	ComboPooledDataSource cpds = new ComboPooledDataSource();
	cpds.setDriverClass( "org.postgresql.Driver" ); //loads the jdbc driver
	cpds.setJdbcUrl( "jdbc:postgresql://localhost/testdb" );
	cpds.setUser("swaldman");
	cpds.setPassword("test-password");

	// the settings below are optional -- c3p0 can work with defaults
	cpds.setMinPoolSize(5);
	cpds.setAcquireIncrement(5);
	cpds.setMaxPoolSize(20);
	return cpds;
}

}
@Repository("sql")
public class BookDaoSqlImpl implements BookDao{
	@Autowired
	@Qualifier("postgres-cp")
	DataSource ds;

	public void addBook(Book b) {
		Connection con = ds.getConnection(); 
		...
	}

```

Spring Data JPA  module is built on top of Spring Core

1) Spring Data JPA provides Connection pooling by default using HikariCP based on entries present in "application.properties"
https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html

2) Spring Data JPA provides Hibernate as the default JPA Vendor

ORM --> Object Relational Mapping


Java Class <---> Relational database table
fields <---> Columns of table

ORM frameworks will generate DDL and DML
* Hibernate
* TopLink
* EclipseLink
* KODO
* OpenJPA
...

public void addProduct(Product p) {
	em.persist(p);
}

JPA is a specification for ORM

===========================

DataSource, PersistenceContext, EntityManagerFactory, EntityManager, HibernateJpaVendor

Using Spring:

```

@Configuration
public class AppConfig {

@Bean("postgres-cp")
public DataSource getDataSource() {
	ComboPooledDataSource cpds = new ComboPooledDataSource();
	cpds.setDriverClass( "org.postgresql.Driver" ); //loads the jdbc driver
	cpds.setJdbcUrl( "jdbc:postgresql://localhost/testdb" );
	cpds.setUser("swaldman");
	cpds.setPassword("test-password");

	// the settings below are optional -- c3p0 can work with defaults
	cpds.setMinPoolSize(5);
	cpds.setAcquireIncrement(5);
	cpds.setMaxPoolSize(20);
	return cpds;
}

@Bean
public EntityManagerFactory emf(DataSource ds) {
	LocalContainerEntityManagerFactory emf = new LocalContainerEntityManagerFactory();
	emf.setDataSource(ds);
	emf.setJpaVendor(new HibernateJpaVendor());
	emf.setPackagesToScan("com.adobe.demo.entity");
	...
	return emf;
}
}
```

Repository classes:
```
@Repository("sql")
public class BookDaoSqlImpl implements BookDao{
	@PersistenceContext
	EntityManager em;

	void addBook(Book b) {
		em.persist(b);
	}
}
```

Spring Data JPA which is available to Spring Boot simpiles using JPA
* no need to explicitly configure datasource , EntityManager, etc.
* just need to create interface extends one of the JpaRepository
* Spring boot creates instances of the class for the interface

public interface BookDao extends JpaRepository {}

all default CRUD operations are provided out-of-box
* it allows us to add custom CRUD operations

==================
application.properties
application-dev.properties
application-prod.properties
application.yml
========

Spring Boot applicaiton
groupId:com.adobe
artifactId:orderapp
Java, Maven project

"orderapp"
depdencies:
1) mysql
2) lombok [code generation library]
3) spring data jpa

@Data --> generates getters, setters, equals and hashCode

===========

Day 2: JPA with RESTful WS

create database ADOBE_MAY_24;

1) spring.jpa.hibernate.ddl-auto=update
create table if not exists, if exists use it, if required alter
Good for Top to Bottom appraoch

2) spring.jpa.hibernate.ddl-auto=create
for every run of application drop existing tables and re-create
-> good for testing environment

3) spring.jpa.hibernate.ddl-auto=verify
Bottom to Top Approach
map to existing tables. if any mismatch throw errors

em.save(p); // INSERT SQL

em.findAll(); // Select SQL
