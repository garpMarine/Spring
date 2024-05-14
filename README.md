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
```
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="order_fk" )
    private List<LineItem> items = new ArrayList<>();
A)
save(order);
B) 
delete(order);
```

EAGER Fetch and Lazy Fetching:
```
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
```
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

Difference between @Controller and @RestController

@Controller: return type is "String" or ModelAndView Object

```
GET
http://localhost:8080/getProducts

Controller vs RestController:

@Controller
public class ProductController {

	@RequestMapping(value="/getProducts", method=RequestMethod.GET)
	public String getdata() {
		...
		return "print.jsp";
	}
redirected to "print.jsp"

	@RequestMapping(value="/getData", method=RequestMethod.GET)
	public ModelAndView getdata2() {
		ModelAndView mav = new ModelAndView();
		mav.setObject("data", new Date());
		mav.setObject("name", "banu@gmail.com");
		mav.setView("print.jsp");
		return mav;
	}

	@RequestMapping(value="/products", method=RequestMethod.GET)
	public @ResponseBody List<Product> getProducts() {
		return service.getProducts();
	}
}

```

===================

Day 3:

Recap:
* Spring Data Jpa --> JPA Repository
* JP-QL vs SQL to write custom methods in JPARepository
@Query , @Modifying [INSERT, DELETE and UPDATE] 
* one-to-many, many-to-one mapping
* RESTful webservices

URI --> Noun
Http Methods for CRUD operations
GET --> READ -> Select statement
POST --> CREATE --> INSERT statement
PUT --> Update [payload contains new representation of entity to update]
PATCH --> update [ partial update, useful if your entity is complex and we need partial update]
DELETE -> DELETE
http://localhost:8080/api/products/2 to delete product by id 2
--> 200 OK
--> 404 Not found


IDEMPOTENT methods: GET, PUT and DELETE are safe methods
Not IDEMPOTENT: POST, PATCH
 
GET, DELETE --> no payload
PUT, PATCH, POST --> needs payload

==========

PATCH operation
The HTTP PATCH request method applies partial modifications to a resource.
A PATCH request is considered a set of instructions on how to modify a resource. 
Contrast this with PUT; which is a complete representation of a resource.

JSON Patch
https://jsonpatch.com/

```
Current data:
{
  "biscuits": [
    { "name": "Digestive" },
    { "name": "Choco Leibniz" }
  ]
}

operations : add, replace, remove, move, test
Payload:
[
	{ "op": "add", "path": "/biscuits/1", "value": { "name": "Ginger Nut" } }
]

Change to:

{
  "biscuits": [
    { "name": "Digestive" },
	{ "name": "Ginger Nut" },
    { "name": "Choco Leibniz" }
  ]
}

	{ "op": "remove", "path": "/biscuits/0" },

```

AOP: Aspect Oriented Programming
--> to remove code scattering and code tangling

Code Tangling
```
public void transferFunds(Account fa, Account ta, double amt) {
	if(ctx.getRole().equals("WRITE_PERMISSION")) { // security
		log.debug("transaction started"); //logger
		double bal = fa.getBalance(); 
		if(bal > amt) { // business logic
			log.debug("amt present");
			try {
				con.setAutoCommit(false); //tx
				// actual code

				con.commit(); // tx
			}
			catch(SQLException ex) {
				con.rollback(); //tx
				log...
			}
		} else {
			log.debug("tx failed...");
			throw new InsufficientBalanceException(..);
		}
	}
}
```
Terminologies:
1) Aspect: bit of concern which can be used along with the main logic
Logger, Security, Profile, Transaction, ...
Generally these are the ones which lead to code tangling and code scattering

@Aspect
public class LogAspect {

}

@Aspect
public class TransactionAspect {

}

@Aspect
public class Security {

}

2) JoinPoint: a place where aspect can be weaved
any methods or exception is a valie joinpoint

3) PointCut: selected joinpoint

public class LogAspect {
	logic() {

	}
}

4) Advice
Before, After, AfterReturning, Around, AfterThrowing

=======

AOP, @Transactional, Exception handling, validation,...
https://docs.spring.io/spring-framework/docs/2.0.x/reference/aop.html



@Transactional --> Pre-defined aspect provided by Spring Data, can be used for JPA / Redis/ MongoDB, ...

* This commits if no exceptions are thrown from your method.
* Rollsback if method throws Exception

===============================

Default JpaRepository methods are Transactional, custom methods we need @Transactional

Exception Handlers for Controllers and RestController which are HttpServletRequest and HttpServletResponse aware --> @ControllerAdvice

Adding Validations:

```
 <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
 </dependency>

 public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
    private int id;
    @NotBlank(message = "Name is required")
    private String name;
    @Min(value = 10, message = "Price ${validatedValue}  should be more than {value}")
    private double price;
    @Min(value = 1, message = "Quantity ${validatedValue}  should be more than {value}")
    private int quantity;
}

 public Product addProduct(@RequestBody @Valid Product p) {
        return service.addProduct(p);
    }
```

MethodArgumentNotValidException: 
ProductController.addProduct(com.adobe.orderapp.entity.Product) with 2 errors: 
[Field error ; default message [Quantity 0  should be more than 1]] 
[Field error ; default message [Price -100.0  should be more than 10]] 


======================

Monitoring and Observability
Monitoring:
1) Notifies that the system is at fualt
2) health, info, metrics ,..

Spring boot provides actuator library for monitoring

```
 <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
 </dependency>

management.endpoint.health.show-details=always
#management.endpoints.web.exposure.include=health,metrics,info
management.endpoints.web.exposure.exclude=
management.endpoints.web.exposure.include=*
management.metrics.distribution.percentiles-histogram.http.server.requests=true
```
ab -c 100 -n 200 http://localhost:8080/api/products
ab -c 50 -n 100 http://localhost:8080/api/products/2

http://localhost:8080/actuator
http://localhost:8080/actuator/metrics/http.server.requests

Prometheus is an open-source systems monitoring and alerting toolkit. Time Series database
need Promethues server running:

```
 <dependency>
            <groupId>io.micrometer</groupId>
            <artifactId>micrometer-registry-prometheus</artifactId>
            <scope>runtime</scope>
 </dependency>
  <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-docker-compose</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>

http://localhost:8080/actuator/prometheus
http://localhost:9090/
```

Testing, Caching, HATEOAS, Rest clients


Day 4:

Recap:
1) AOP
2) @Transactional aspect
Instead of programmatic tx we have declarative transaction

@Transactional
method() {}

3) @ControllerAdvice, @ExceptionHandler

---
Spring boot Actuator module: production ready features to monitor your application.
various endpoints for monitoring health check, info, metrics
Examples:
http://localhost:8080/actuator/health
http://localhost:8080/actuator/beans
http://localhost:8080/actuator/metrics
http://localhost:8080/actuator/metrics/http.server.requests

Prometheus server: time-series database + server
scrape metrics from provided endpoint

========================================

Testing: --> Unit testing
spring-boot-starter-test is added for every spring boot application

* JUnit is provided as unit testing framework
* Mockito library for mocking

Controller --> Service --> Repository --> database

to test repository we need to mock database
to test service we need to mock repo
to test controller we mock service

* JsonPath
https://jsonpath.com/

* Hamcrest

---

@WebMvcTest --> creates a Minimalistic Spring container with TestDispatcherServlet instead of DispatcherServlet, creates web related beans like HandlerMapping, ...[ not create service / repo / etc]

Not like @SpringBootApplication where all beans are created

@WebMvcTest(ProductController.class) --> create bean ProductController within the container

===================================

Spring boot Rest clients:
1) RestTemplate
```
 @Bean
    RestTemplate createRestTemplate(RestTemplateBuilder builder) {
		// set headers
		// authorization token
        return builder.build();
    }
```
2) WebClient [webflux dependency --> async module]
3) RestClient from Spring boot 3.x version onwards

===

Zipkin is a distributed tracing sytem. collection of log
http://localhost:9411/zipkin/

@HttpExchange is the root annotation we can apply to an HTTP interface and its exchange methods
@GetExchange for HTTP GET requests
@PostExchange for HTTP POST requests
@PutExchange for HTTP PUT requests
@PatchExchange for HTTP PATCH requests
@DeleteExchange for HTTP DELETE requests

https://jsonplaceholder.typicode.com/posts

Resume @ 11:25

Caching:
client-side caching:
* HTTP Headers Cache-control
* ETag --> Entity Tag
The ETag (or entity tag) HTTP response header is an identifier for a specific version of a resource.
```
 @GetMapping("/etag/{id}")
    public ResponseEntity<Product> getProductEtag(@PathVariable("id") int id) throws NotFoundException {
        Product p =  service.getProductById(id);
        return ResponseEntity.ok().eTag(Long.toString(p.hashCode())).body(p);
    }
```
Check ETAG.pdf

Server Side Caching:
```
 <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-cache</artifactId>
        </dependency>
@Configuration
@EnableCaching
public class AppConfig {

SPeL
   @Cacheable(value="productCache", key="#id")
    @GetMapping("/cache/{id}")
    public Product getProductCache(@PathVariable("id") int id) throws NotFoundException {
        System.out.println("Cache Miss!!!");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) { }
        return  service.getProductById(id);
    }

http://localhost:8080/actuator/caches

"caches": {
"productCache": {
	"target": "java.util.concurrent.ConcurrentHashMap"
}

 @Cacheable(value="productCache", key="#id")
 @CachePut(value = "productCache", key="#id")
 @CacheEvict(value = "productCache", key="#id")

 Scheduler to clear cache

 @Configuration
@EnableCaching
@EnableScheduling
public class AppConfig {

https://spring.io/blog/2020/11/10/new-in-spring-5-3-improved-cron-expressions
```

Redis as CacheManager:
docker run --name some-redis -p 6379:6379 -d redis

spring.data.redis.host=localhost
spring.data.redis.port=6379

```
 <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>

Serialization: process of replicating the state to a stream
Check RedisConfig.java

DefaultSerializer requires a Serializable payload but received an object of type [com.adobe.orderapp.entity.Product]

public class Product implements Serializable {

Redis Client:
nodeJS:
$ npx redis-commander
```

HATEOAS --> Hypermedia as the engine of Application State
Level 3 RESTful WS
https://martinfowler.com/articles/richardsonMaturityModel.html

Order placed
--> Cancel link
--> payment link
------> check status
------> change address

Spring Document

WebMvcLinkBuilder allows us to add links programatically

ResourceModel
--> EntityModel ==> Entity Representation + links
--> CollectionModel ==> List<EntityModel> + links

```
  <dependency>
    <groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-hateoas</artifactId>
 </dependency>
 http://localhost:8080/api/products/hateoas/2

 @EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL_FORMS)

```

Spring Data REST on top of Spring Data [ JPA / Mongo/ ..] repositories
-> takes HATEOAS + Spring Data Jpa 
--> creates endpoints based on methods of Spring Data
--> no need for RestController

New Spring boot applicaiton:
mysql. lombok. web, Rest repositories

http://localhost:8080/products?page=2&size=2
http://localhost:8080/products
http://localhost:8080/products/2
http://localhost:8080/products/search/findByQuantity?q=99
http://localhost:8080/products/search/getByRange?l=100&h=5000

We can't write RestController with "Spring Data REST"
instead use BasePathAwareController

====

Async, Security, MS



Day 5:

Recap:
* Spring and Spring Boot Framework
Dockerfile
FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

java -jar app.jar

* JPA --> RDBMS
* JpaRepository , @Query
* Building RESTful WS
Spring MVC module, DispatcherServlet, HandlerMapping, @RestController , ....
* AOP
* @Transactional, @ControllerAdvice, @ExceptionHandler
* Validation: @Valid --> MethodArgumentNotValidException
* Caching: ConcurrentHashMap CacheManager, RedisCacheManager
@Cacheable, @CachePut, @CacheEvit --> @EnableCache , @Scheduled --> @EnableSchedule
* HATEAOS --> HyperText As The Extension Of Application State
WebMvcLinkBuilder --> EntityModel / CollectionModel --> Entity + Link
Level 3 Restful WS
* Spring Data Rest --> REST Repository [ RESTful + Repository]
@BasePathAwareController
@RespositoryRestController
instead of @RestController

* RestTemplate / RestClient
* @HttpException: @GetExchange, @PostExchange, @PutExchage, @DeleteExchange
--> Declarative REST client

@GetExchange
List<Product> getProducts();

CGLib [proxy ]/ Byte Buddy or Javaassist for Byte code instrumentation

==================

Spring Documentation:
* RAML
https://raml.org/
* The OpenAPI Specification provides a formal standard for describing HTTP APIs.
--> Swagger OpenAI

Scan all packages for @Controller and @RestController and creates Documention
```
 <!-- Spring Docs -->
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>2.0.2</version>
        </dependency>
Appconfig.java
@Bean                                                                   
public OpenAPI springOrderAppOpenApi() {                                
    return  new OpenAPI()                                               
            .info(new Info().title("Shopping Application")              
                    .description("Spring Boot RESTful API")             
                    .version("1.0.0")                                   
                    .license(new License().name("Apache 2.0")));        
}  
ProductController.java
@Tag(name="products", description = "Product API")
public class ProductController {    
 @Operation(summary = "Get Product by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the product",
                    content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Product.class)) }),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content)})
    @GetMapping("/{id}")
    public Product getProduct(@PathVariable("id") int id) throws NotFoundException {                                                                 
```

http://localhost:8080/v3/api-docs

http://localhost:8080/swagger-ui/index.html --> jquery

@Hidden on Controller to specify that it should not be a part of documention

===========

Async Operations and using Custom Thread Pool
https://jsonplaceholder.typicode.com/users
https://jsonplaceholder.typicode.com/posts

Future and CompletableFuture --> Java 5 version

@EnableAsync
public class AppConfig {

======

Reactive Programming:
declarive paradigm concerned with data streams and propagation of change.

React to something --> run a function whene user clicks

Iterator Pattern to Observer Pattern
```
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
```

webflux uses Netty server as Servlet Container instead of Tomcat as servlet Container
o.s.b.web.embedded.netty.NettyWebServer  : Netty started on port 8080

Tomcat --> thread based [ one thread per client]
Netty --> Event loop based.

Project with reactive web and lombok
Subscription: one-to-one relationship between Publihser and Subscriber.

interface Subscription <T> {
	public void request(long n);
	public void cancel();
}

==========

JDBC --> Spring Data R2DBC instead of Spring Data Jpa
MongoDB --> reactive MongoRepository vs MongoReactiveRepository

@Async uses Future / CompletableFuture
--> Still sync when response is sent

Spring WebFlux comes with two publishers:
Mono: return 0 or 1 element
Flux: returns 0...N elements

time blocking code: 10043 ms
time non-blocking code: 6 ms

Server Side Events

Functional Endpoints:
We can also use Functional Routers instead of @RestController in webflux

Pending:
--> Share the code [ pending ]
https://docs.spring.io/spring-framework/reference/web/webflux-functional.html

--> Mongo Repository [ reactive ]

==========

Spring Security: Authentication and Authorization

```
 <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

1) By default all resources are protected.
2) creates login and logout pages
http://localhost:8080/login
http://localhost:8080/logout
3) creates a single user with "username" as "user" and generated password
Using generated security password: 60a93d5a-75c0-4c34-ad72-66abbcae75df


===========

Day 6:

Recap:
Async and Reactive programming [webflux] --> Mono, Flux
* Reactive can be done using @RestController
* Reactive Controller can be done using RouterFunction 

Spring Security:
including Security dependency by default provides:
1) All resources are protected by default
2) gives login and logout pages
3) creates one user with "username" : "user" and "password" : <<generated_password>>
4) uses JSESSIONID to track conversational state of the client
User Authentication object [ which contains principle, isAuthenticated, authorities/ role] will be stored in SecurityContext, each SecurityContext is associated with JSESSIONID

Customization:
1) AuthenticationManager to use different providers like
InMemory, DaoAuthenticationProvider, LDap, ...

UserDetailsService interface has to be implemented by all providers

2) Beans to configure protected resources and specify provider

3) @EnableWebSecurity

DelegatingProxyFilter, UsernamePasswordAuthenticationFilter,

==========================

DaoAuthenticationProvider needs RDBMS and needs the table to be following specifc Schema

https://docs.spring.io/spring-security/reference/servlet/appendix/database-schema.html

===========================

RESTful WS has to Stateless.
Use Tokens --> JWT
JSON Web Tokens are an open, industry standard RFC 7519 method for representing claims securely between two parties.

```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c


Header:
{
  "alg": "HS256",
  "typ": "JWT"
}

Payload:
{
	"sub" : "banu@gmail.com",
	"iat" : 12342342,
	"exp" : 63424111,
	"iss": "http://adobe.com/secure",
	"authorities": "ADMIN", "MANAGER"
}

SIGNATURE:
HMACSHA256(
  base64UrlEncode(header) + "." +
  base64UrlEncode(payload),
  your-256-bit-secret_top_secret_key [ generally comes from Configuration server or use Private Key and public key]
) 

keytoll / openSSL
Private Key is used to generate the token [Authorization Server]
Public key is used to validate the token [Resource Server]
```

User and Role
UserDao
DTOs:
SignUpRequest --> Registration
SigninRequest --> Login
JwtAuthenticationResponse --> to send Token to client

Services:
UserDetailsServiceImpl used for JPA implementation instead of JdbcUserImpl[built-in]
JwtService: 
 login: generateToken()
 Access Resource:
 Http header 
 Authroization: Bearer <<token>>
 isTokenValid()
AuthenticationService: 
 return token on signup and signin

cfg:
JwtAuthenticationFilter -> OncePerRequestFilter
read token, validate and store UserDetails in SecurityContext
SecurityConfiguration
https://bcrypt-generator.com


AuthenticationController Controller

=======

Monolithic : not scalable
Microservices:
Each microservice you design shall concentrate only on one service of the application.
Between MS we can have Synchronous communication or Asynchronous communication [eventual consistency]

* Discovery Server
* Services
* Api Gateway
* Confiration Server

====
Spring Cloud APIs
1) Build Discovery Server [Netflix --> Eureka  server]
discovery-server
dependency --> eureka-server
```
 <dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
 </dependency>
 @SpringBootApplication
@EnableEurekaServer
public class DiscoveryServerApplication {

configure application.yml
start:
http://localhost:8761/
check registered instances

```

2) Student Service
student-service
Actuator, jpa, lombok, mysql, web, Eureka Discovery client

Start and see that it's registered with Eureka-server

3) School Service
Openfeign, Actuator, jpa, lombok, mysql, web, Eureka Discovery client
Openfeign --> declartive client for MicroService like @HttpExchange

@EnableFeignClients
public class SchoolServiceApplication {

create table schools (school_id int PRIMARY KEY AUTO_INCREMENT,name VARCHAR(100));

create table students(id int PRIMARY KEY AUTO_INCREMENT, first_name VARCHAR(100), email VARCHAR(100), school_id int);


http://localhost:8070/api/schools/with-students/2


