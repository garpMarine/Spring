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
3) @Service
4) @Configuration
5) @Controller
6) @RestController
7) @ControllerAdvice

Wiring can be done using:
1) @Autowired
2) @Inject
3) Constructor DI

