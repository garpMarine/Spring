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

application.properties
entities: Order and LineItem

Scope of beans:
```
1) Singleton [default]
@Scope("singleton") or
@Singleton

@Repository
@Scope("singleton")
public class SomeRepo {
}

@Service
class AService {
	@Autowired
	SomeRepo repo;
}

@Service BService {
	@Autowired
	SomeRepo repo;
}

Here both AService and BService gets access to the same bean

2) Prototype
@Scope("prototype")
public class SomeRepo {
}

@Service
class AService {
	@Autowired
	SomeRepo repo;
}

@Service BService {
	@Autowired
	SomeRepo repo;
}

Here both AService and BService will get different instances of SomeRepo

3) Request
@Scope("request")
or
@RequestScope
public class SomeRepo {
}

Here SomeRepo bean instance is created when client makes a request, this bean is destroyed once response is commited to the client

4) Session
@Scope("session")
or
@SessionScope
public class SomeRepo {
}

bean is attached to conversational state of a client
1 bean per client

5) application
one per web container
```

OrderService service = new OrderService(a, b);

```
Hibernate: 
    create table customers (
        email varchar(255) not null,
        fname varchar(100),
        lname varchar(100),
        primary key (email)
    ) engine=InnoDB
Hibernate: 
    create table line_items (
        itemid integer not null auto_increment,
        amount float(53) not null,
        quantity integer not null,
        product_fk integer,
        order_fk integer,
        primary key (itemid)
    ) engine=InnoDB
Hibernate: 
    create table orders (
        oid integer not null auto_increment,
        order_date datetime(6),
        total float(53) not null,
        customer_fk varchar(255),
        primary key (oid)
    ) engine=InnoDB
Hibernate: 
    create table products (
        id integer not null auto_increment,
        name varchar(255),
        price float(53) not null,
        quantity integer not null,
        primary key (id)
    ) engine=InnoDB
Hibernate: 
    alter table line_items 
       add constraint FK7bcmyaf081a54pqagiuo2boo 
       foreign key (product_fk) 
       references products (id)
Hibernate: 
    alter table line_items 
       add constraint FKjvi2gypwgl46v67xa2bgqp0uj 
       foreign key (order_fk) 
       references orders (oid)
Hibernate: 
    alter table orders 
       add constraint FKlctjwy900y7l1xmwulg4rkeb3 
       foreign key (customer_fk) 
       references customers (email)
```

https://martinfowler.com/bliki/DomainDrivenDesign.html


Association Mapping:
1) one-to-one
2) one-to-many
3) many-to-one
4) many-to-many

one order has 5 items;

Without Cascade:
A)
save(order);
save(i1);
save(i2);
save(i3);
save(i4);
save(i5);

B) 
delete(order);
delete(i1);
delete(i2);
delete(i3);
delete(i4);
delete(i5);

With Cascade:

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="order_fk" )
    private List<LineItem> items = new ArrayList<>();
A)
save(order);
B) 
delete(order);

EAGER Fetch and Lazy Fetching:

1) By default it's Lazy:
orderDao.findById(1);
select * from orders where oid = 1;
Items are not fetched

2) EAGER
 @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="order_fk" )
    private List<LineItem> items = new ArrayList<>();

orderDao.findById(1);
select * from orders where oid = 1;
AND
select * from line_items where order_fk = 1;

===
 With the above settings we don't need ItemDao

 =======================================

JP-QL: it's polymorphic, case sensensitve, uses class and fields
gets records from all tables
from Object
SQL is not poly-morphic, uses table and columns

==========================

Pending: OrderDao and OrderService

============================================

Building RESTful WS:

REST --> REpresentational State Transfer --> 2000 Roy Fielding

Resource: document, image, database , printer
Represenation: state of resource in readable format like XML / JSON / CSV

REST APIs uses Uniform Resource Identifiers to address resources

* Singleton and Collection Resources
"customers" --> collection
"customer" --> is a singleton

* Collection and sub-collection resources
	* "/customers/banu@gmail.com/accounts"

Content Negotiation:
asking for suitable presentation by a client [ json / xml]

by using HTTP Header:
Accept: text/xml

Accept: application/json

Best Practices:
* use nouns to represetn resources
* Collection
-> server managed directory of resources
* store
--> client managed resource repository
--> client can add , delete , ...
https://spotify.com/users/banu@gmail.com/playlist
* Controller
 --> procedural concept , executable functions
 https://spotify.com/users/banu@gmail.com/playlist/play

* use hyphens to imporove readability
* lower case URIs
* never use CRUD functions in URIS

Use Path paramter to fetch a single resource based on PK
http://localhost:8080/api/products/3

use Query parameter to fetch sub-sets [ filtered]
http://localhost:8080/api/products?low=1000&high=10000
http://localhost:8080/api/products?page=3&size=20

----
Principles of REST:
1) Uniform Interface
2) Client Server: Seperation of concerns
3) Stateless: No conversational state of the client [ no  session tracking]
4) Cacheble
5) Layered System
Component A --> Component B --> Component C
http://server.com/products

Spring Boot:
 <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

1) adds Tomcat Embedded Web Container / server
2) Adds Spring MVC module
* DispatcherServlet running in web container intercepts all requests comming from client [Front Controller]
* provides HandlerMapping: every request from client is mapped to @Controller or @RestController
* Jackson for ContentNegotiationHandler to handle JSON
Java <--> JSON
Other alternative for JSON:
Jettison / GSON / MOXY [ needs to configured explicilty]
* for XML we need to configure explicitly
<dependency>
    <groupId>com.fasterxml.jackson.dataformat</groupId>
    <artifactId>jackson-dataformat-xml</artifactId>
</dependency>

---

GET http://localhost:8080/api/products

public ContentNegotiationConfigurer defaultContentTypeStrategy(ContentNegotiationStrategy defaultStrategy)

==========

HttpMessageConverters for String to Java primitives are available by default



 




 
