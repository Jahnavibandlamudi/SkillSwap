# SkillSwap – SRM UNIVERSITY AP
### Student-to-Student Skill Exchange & Collaborative Learning Platform

[![Java](https://img.shields.io/badge/Java-17%2B-orange.svg)](https://docs.oracle.com/en/java/)
[![Servlet](https://img.shields.io/badge/Jakarta%20Servlet-6.0-blue.svg)](https://jakarta.ee/)
[![JSP](https://img.shields.io/badge/JSP-3.1-informational.svg)](https://jakarta.ee/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![Tomcat](https://img.shields.io/badge/Apache%20Tomcat-10.1%2B-yellow.svg)](https://tomcat.apache.org/)
[![Bootstrap](https://img.shields.io/badge/Bootstrap-5.3-purple.svg)](https://getbootstrap.com/)

---

## 1. Project Overview

**SkillSwap** is a full-featured web-based student-to-student skill exchange platform engineered **exclusively for SRM UNIVERSITY AP**. The platform solves the problem of one-directional, expensive online courses by introducing an intelligent, peer-to-peer knowledge bartering system. 

Students can act as both **mentors** and **learners**:
* A Computer Science student can teach Core Java to an Electronics student.
* In return, the Electronics student teaches UI/UX Design and Figma to the Computer Science student.
* Both students expand their capabilities at **zero monetary cost**, coordinated across SRM AP campus locations (Central Library, ALC classrooms, Tech Park) or online via Google Meet.

---

## 2. Technology Stack & Architecture

SkillSwap is strictly built using the **Model-View-Controller (MVC)** enterprise architecture without heavy third-party frameworks like Spring Boot, adhering to core **Advanced Java** academic specifications.

| Layer | Technology | Details |
|---|---|---|
| **View Layer** | JSP 3.1, JSTL 3.0, HTML5, CSS3, JavaScript | Modern SaaS interface styled with Bootstrap 5, Bootstrap Icons, and custom CSS |
| **Controller Layer** | Java Servlets (Jakarta Servlet 6.0) | Request validation, business coordination, session handling |
| **Filter Layer** | Jakarta WebFilter | Role-based access control (`AuthFilter`, `AdminFilter`) |
| **Service Layer** | Core Java | Business logic, multi-factor matching engine, atomic credit transfer |
| **DAO Layer** | JDBC with `PreparedStatement` | SQL injection prevention, CRUD operations, transaction control |
| **Connection Pooling** | HikariCP 5.1.0 | High-performance JDBC connection management |
| **Database** | MySQL 8.0 (InnoDB) | Foreign keys, cascading constraints, unique indexes |
| **Security** | SHA-256 + Application Salting | Constant-time password hashing and XSS input escaping |
| **Application Server** | Apache Tomcat 10.1+ | Embedded runner or standalone WAR deployment |
| **Build Tool** | Apache Maven | Packaging `skillswap.war` |

---

## 3. Project Directory Structure

```text
SkillSwap/
│
├── database/
│   └── skillswap.sql               # Complete MySQL schema & SRM AP seed data
│
├── src/main/java/com/skillswap/
│   ├── model/                      # Entity Models & DTOs
│   │   ├── User.java               # Student / Admin entity
│   │   ├── Skill.java              # Skill entity
│   │   ├── UserSkill.java          # Student-Skill relationship (OFFER / WANT)
│   │   ├── UserAvailability.java   # Weekly schedule availability
│   │   ├── ExchangeRequest.java    # Peer proposal entity
│   │   ├── LearningSession.java    # Scheduled session entity
│   │   ├── CreditAccount.java      # Wallet account entity
│   │   ├── CreditTransaction.java  # Immutable transaction ledger
│   │   ├── Review.java             # Rating (1-5) and feedback
│   │   ├── Notification.java       # In-app notifications
│   │   └── MatchResult.java        # Matching algorithm DTO
│   │
│   ├── dao/                        # Data Access Object Layer (JDBC)
│   │   ├── UserDAO.java
│   │   ├── SkillDAO.java
│   │   ├── ExchangeDAO.java
│   │   ├── SessionDAO.java
│   │   ├── CreditDAO.java          # ACID atomic transfers with rollback
│   │   ├── ReviewDAO.java
│   │   └── NotificationDAO.java
│   │
│   ├── service/                    # Business Logic Layer
│   │   ├── AuthService.java        # Registration, login, password hashing
│   │   ├── UserService.java        # Profile editing, search
│   │   ├── SkillService.java       # Skill catalog & student skills
│   │   ├── MatchService.java       # Intelligent multi-factor matching
│   │   ├── ExchangeService.java    # Proposal acceptance/rejection
│   │   ├── SessionService.java     # Scheduling & 2-way completion
│   │   ├── CreditService.java      # Virtual wallet ledger
│   │   ├── ReviewService.java      # Star ratings & reputation
│   │   ├── NotificationService.java# Alert management
│   │   └── AdminService.java       # Platform metrics & student governance
│   │
│   ├── controller/                 # Jakarta Servlets
│   │   ├── AuthServlet.java        # /login, /register, /logout
│   │   ├── DashboardServlet.java   # /dashboard
│   │   ├── ProfileServlet.java     # /profile
│   │   ├── SkillServlet.java       # /skills
│   │   ├── MatchServlet.java       # /matches
│   │   ├── ExchangeServlet.java    # /requests
│   │   ├── SessionServlet.java     # /sessions
│   │   ├── CreditServlet.java      # /credits
│   │   ├── ReviewServlet.java      # /reviews
│   │   ├── NotificationServlet.java# /notifications
│   │   └── AdminServlet.java       # /admin, /admin-dashboard
│   │
│   ├── filter/                     # Security Filters
│   │   ├── AuthFilter.java         # Protects student routes
│   │   └── AdminFilter.java        # Protects admin portal
│   │
│   ├── util/                       # Utilities
│   │   ├── DBConnection.java       # HikariCP pool manager
│   │   ├── PasswordUtil.java       # Salted SHA-256 hashing
│   │   └── ValidationUtil.java     # SRM AP ID (AP***********) & email validation
│   │
│   └── server/
│       └── EmbeddedTomcatServer.java # 1-Click Embedded Tomcat launcher
│
├── src/main/resources/
│   └── db.properties               # MySQL database connection settings
│
├── src/main/webapp/
│   ├── assets/
│   │   ├── css/style.css           # SaaS design (Navy, Indigo, Lavender)
│   │   └── js/main.js              # Interactive validation & modal scripts
│   │
│   ├── WEB-INF/
│   │   ├── partials/
│   │   │   ├── header.jsp          # HTML head, Bootstrap 5 CDN
│   │   │   ├── navbar.jsp          # Dynamic navigation bar & user dropdown
│   │   │   └── footer.jsp          # SRM AP branding footer
│   │   └── web.xml                 # Web deployment descriptor
│   │
│   ├── index.jsp                   # Modern landing page
│   ├── login.jsp                   # Login page with 1-click test credentials
│   ├── register.jsp                # Student registration with AP*********** validation
│   ├── dashboard.jsp               # Student dashboard
│   ├── profile.jsp                 # Profile & schedule manager
│   ├── skills.jsp                  # Explore catalog & manage skills
│   ├── matches.jsp                 # Smart skill matching & proposals
│   ├── requests.jsp                # Received & sent request tabs
│   ├── sessions.jsp                # Learning sessions & confirmation
│   ├── credits.jsp                 # Virtual wallet & ledger
│   ├── notifications.jsp           # Notifications center
│   └── admin-dashboard.jsp         # Administrator governance console
│
├── src/test/java/com/skillswap/
│   ├── SkillSwapUnitTest.java      # JUnit 5 test cases
│   └── TestRunner.java             # Standalone test suite (18 test cases)
│
├── pom.xml                         # Maven project definition
└── README.md                       # Comprehensive documentation
```

---

## 4. Key Functional Modules & Business Rules

### Module 1: Registration & Authentication
* **SRM AP Student ID Validation**: Must follow format `AP` followed by 11 digits (e.g. `AP21110010001`). Enforced on both frontend and backend.
* **Campus Email**: Validated for `@srmap.edu.in`.
* **Password Security**: Passwords are salted and hashed using SHA-256.
* **Welcome Gift**: Every newly registered student automatically receives **5 Welcome Credits** in their virtual wallet.

### Module 2: Profile & Schedule Management
* Students customize their bio, department, and academic year.
* **Weekly Availability**: Students set specific free hours per day (e.g. Monday 17:00 - 19:00) to optimize match scores.
* **Role Protection**: Students can never edit another student's profile.

### Module 3: Skill Management
* 15+ pre-loaded domains (Programming, Web Dev, AI/ML, Public Speaking, Design, Video Editing, Financial Literacy, CAD).
* Students assign skills as **OFFER** (teaching) or **WANT** (learning) with a proficiency tier: *Beginner*, *Intermediate*, *Advanced*, *Expert*.

### Module 4: Intelligent Skill Matching System
Calculates a transparent matching percentage (0–100%) based on 4 criteria:
1. **Mutual Skill Compatibility (60 pts)**: Student A offers what Student B wants, AND Student B offers what Student A wants (highlighted with glowing *Mutual Match* badges). One-way learning matches receive 30 pts.
2. **Schedule Availability (20 pts)**: Overlapping free days of the week on campus.
3. **Proficiency Suitability (10 pts)**: Higher score when the mentor has Advanced or Expert proficiency.
4. **Profile Completeness (10 pts)**: Bio, academic department, and previous session ratings.

### Module 5: Exchange Proposals
* Students propose skill exchanges directly from match cards.
* Cannot propose exchange to oneself.
* Duplicate pending proposals are blocked.
* Only the intended receiver can Accept or Decline.
* Senders can cancel pending proposals.

### Module 6: Session Management & Conflict Prevention
* Only **ACCEPTED** proposals can book learning sessions.
* **Overlapping Conflict Prevention**: The backend verifies that neither teacher nor learner has an existing scheduled session during that time slot.
* **Two-Way Completion Confirmation**: A session is marked completed **only when both participants confirm**.

### Module 7: Virtual Credit Wallet (ACID Transactions)
* Non-monetary peer bartering economy.
* Teaching reward: **+2 Credits**. Learning cost: **-2 Credits**.
* **Database Transactions**: Credit updates use `connection.setAutoCommit(false)` and `rollback()`.
* **Negative Balance Prevention**: Learner must possess at least 2 credits before attending.
* **Duplicate Prevention**: Prevents double credit transfer for the same session.

### Module 8: Peer Ratings & Reviews
* Reviews can only be submitted for **COMPLETED** sessions.
* One review per participant per session (cannot review oneself).
* Ratings (1 to 5 stars) update the student's dynamic average reputation.

### Module 9: Notifications Center
* In-app notification bell with unread counter.
* Real-time alerts for received proposals, accepted sessions, credit transactions, and reviews.

### Module 10: Administrator Console
* Protected by `AdminFilter`. Only users with role `ADMIN` can access `/admin`.
* Live statistical cards (Students, Active accounts, Total Skills, Proposals, Completed sessions, Total Reviews).
* One-click student account activation/deactivation.
* Skill catalog addition and deletion.

---

## 5. Database Setup Guide

1. Open your MySQL client (MySQL Workbench, phpMyAdmin, or Command Line):
   ```bash
   mysql -u root -p
   ```
2. Execute the included SQL initialization script:
   ```sql
   SOURCE database/skillswap.sql;
   ```
   *This script automatically creates `skillswap_db`, all 10 InnoDB relational tables, constraints, foreign keys, and seeds pre-loaded students, skills, mutual matches, accepted requests, and reviews.*

3. Copy `src/main/resources/db.properties.example` to `src/main/resources/db.properties`, then set your local database credentials in the copied file. The real `db.properties` file is ignored by Git so local passwords are not published:
   ```properties
   db.url=jdbc:mysql://localhost:3306/skillswap_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8
   db.username=root
   db.password=YOUR_LOCAL_MYSQL_PASSWORD
   ```

---

## 6. Pre-Configured Test Accounts

The platform includes seed accounts ready for instant demonstration:

| Name | Role | Student ID / Email | Password | Pre-loaded Capabilities |
|---|---|---|---|---|
| **Aarav Sharma** | Student | `AP21110010001`<br>`aarav_sharma@srmap.edu.in` | `Password@123` | Offers: Java (Expert), Web Dev<br>Wants: Graphic Designing |
| **Diya Reddy** | Student | `AP21110010002`<br>`diya_reddy@srmap.edu.in` | `Password@123` | Offers: Graphic Designing, Photography<br>Wants: Java Programming<br>*(Forms exact Mutual Match with Aarav!)* |
| **Karthik Varma** | Student | `AP22110010045`<br>`karthik_varma@srmap.edu.in` | `Password@123` | Offers: Machine Learning, Python<br>Wants: Public Speaking |
| **Sneha Patel** | Student | `AP22110010088`<br>`sneha_patel@srmap.edu.in` | `Password@123` | Offers: Public Speaking, Communication<br>Wants: Python Programming<br>*(Forms Mutual Match with Karthik!)* |
| **Administrator** | Admin | `APADMIN00001`<br>`admin@srmap.edu.in` | `Admin@123` | Platform Governance & Analytics |

> **Tip:** The Login page features **1-Click Quick Fill buttons** that instantly populate credentials for testing.

---

## 7. How to Run the Project

### First-time setup on Windows

The app stores its persistent data in a MySQL database named `skillswap_db`. The SQL file creates these related tables:

* `users` and `skills` store student/admin accounts and the skill catalogue.
* `user_skills` and `user_availability` store each student's offered/wanted skills and free times.
* `exchange_requests` and `learning_sessions` store exchange proposals and scheduled lessons.
* `credit_accounts` and `credit_transactions` store balances and the credit ledger.
* `reviews` and `notifications` store session feedback and in-app alerts.

The SQL also adds demonstration accounts, skills, and example activity so the app has something to explore on first launch. New registrations and activity are saved to MySQL.

1. Install **MySQL Server 8.0**, **JDK 17 or newer**, and **Apache Maven**. Copy `src/main/resources/db.properties.example` to `src/main/resources/db.properties` and set `db.password` to your local MySQL password. Never commit or publish that local file.
2. Start the Windows service named `MySQL80`. In PowerShell, run `Get-Service MySQL80`; if it is stopped, run PowerShell as Administrator and use `Start-Service MySQL80`.
3. Open a terminal in this project folder and run `setup_database.bat`. Enter `root` when asked for the username and enter your MySQL password when MySQL prompts. This imports `database/skillswap.sql` and creates `skillswap_db` with its schema and sample records.
4. Start the web server by running `run.ps1` in PowerShell, or run `mvn compile exec:java` from the project folder.
5. Open the **Server URL printed in the terminal**. The default is `http://localhost:8080/skillswap`; if that port is already occupied, the launcher automatically tries the next available port and prints the correct URL.

If PowerShell blocks `run.ps1`, use `mvn compile exec:java` in the same folder instead. If `mysql` is not recognized, add the MySQL Server `bin` directory (commonly `C:\Program Files\MySQL\MySQL Server 8.0\bin`) to Windows PATH, then reopen the terminal. If login to MySQL fails, confirm the service is running and that the password in `src/main/resources/db.properties` matches your MySQL root password. Stop any old app run with **Ctrl+C** before starting a new one; always use the URL printed by the current run.

The demo users' passwords are `Password@123`; the demo administrator password is `Admin@123`. These are application login passwords, separate from the MySQL root password.

The SQL import is intended for a fresh demo database. Keep a backup before re-importing into a database with information you want to preserve.

### Returning to an existing installation

Start `MySQL80`, then run `run.ps1` (or `mvn compile exec:java`) from this folder. Do not rerun the database import unless you are setting up the demo data again.

### Option A: 1-Click Embedded Tomcat Runner (Fastest & Easiest)
You can launch the full application with the embedded Tomcat runner directly from Maven:

```bash
mvn compile exec:java
```
Or run `com.skillswap.server.EmbeddedTomcatServer` directly from IntelliJ IDEA or Eclipse.

Open your browser at:
```text
http://localhost:8080/skillswap
```

---

### Option B: Deploying WAR to External Apache Tomcat in IntelliJ IDEA

1. Open the project folder in **IntelliJ IDEA**.
2. Go to **Run > Edit Configurations...**
3. Click **+ > Tomcat Server > Local**.
4. Set the Application Server to your local Apache Tomcat 10+ directory.
5. In the **Deployment** tab, click **+ > Artifact...** and select `skillswap:war` (or `skillswap:war exploded`).
6. Set Application Context to `/skillswap`.
7. Click **Run**. IntelliJ will launch Tomcat and open the browser.

---

### Option C: Deploying WAR to External Apache Tomcat in Eclipse

1. Import the project: **File > Import > Existing Maven Projects**.
2. Build the WAR: Right-click project > **Run As > Maven build...** (Goals: `package -DskipTests`).
3. Under the **Servers** tab in Eclipse, right-click Tomcat 10+ server > **Add and Remove...**
4. Add `skillswap` and click **Finish**.
5. Start the server and navigate to `http://localhost:8080/skillswap`.

---

## 8. Verification & Test Execution

SkillSwap includes an automated test runner verifying all 18 functional test cases from Section 15 of the project thesis.

To execute the test suite:
```bash
java -cp "target/classes;target/test-classes" com.skillswap.TestRunner
```

### Test Case Execution Results:
```text
======================================================================
  SKILLSWAP (SRM UNIVERSITY AP) - AUTOMATED VERIFICATION TEST SUITE  
======================================================================
  [PASS] TC01: Register with valid details                           
  [PASS] TC02: Register with existing email / invalid student ID     
  [PASS] TC03: Login with valid credentials                          
  [PASS] TC04: Login with invalid password                           
  [PASS] TC05: Add a teaching skill (OFFER)                          
  [PASS] TC06: Add a learning skill (WANT)                           
  [PASS] TC07: Search and filter catalog skills                      
  [PASS] TC08: Send exchange request (Aarav to Diya)                 
  [PASS] TC09: Accept a request (Only Receiver)                      
  [PASS] TC10: Reject a request                                      
  [PASS] TC11: Schedule a session for accepted exchange              
  [PASS] TC12: Overlapping session conflict check                    
  [PASS] TC13: Complete a session confirmation (Partial - Teacher only)
  [PASS] TC14: Complete both confirmations and trigger credit transfer
  [PASS] TC15: Spend more credits than available rejected            
  [PASS] TC16: Submit valid review for completed session             
  [PASS] TC17: Submit duplicate review rejected                      
  [PASS] TC18: Student accesses admin page denied                    
======================================================================
  SUMMARY: 18 PASSED, 0 FAILED  
======================================================================
```

---

## 9. Academic Thesis Conclusion
SkillSwap demonstrates a production-grade implementation of **Advanced Java Web Technologies**:
* Strict adherence to **MVC Architecture** with decoupled layers.
* Robust **JDBC PreparedStatement** queries preventing SQL injection.
* **ACID Transactions** ensuring zero data inconsistency during virtual credit exchanges.
* Enterprise **Role-Based Access Control (RBAC)** via Filters.
* Modern, responsive commercial SaaS frontend tailored specifically for the academic ecosystem of **SRM University AP**.

