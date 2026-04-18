📦 Smart Courier Management System

A scalable backend system built using Java, Spring Boot, Spring Security (JWT), and MySQL to manage end-to-end courier operations including order placement, delivery assignment, tracking, and user management.

🚀 Features
- JWT-based Authentication & Authorization
- Role-Based Access Control (RBAC) (Admin, Manager, Agent, Customer)
- Order & Package Management
- Delivery Assignment & Reassignment
- Real-time Package Tracking
- Asynchronous Processing using @Async
- Scheduled Jobs using @Scheduled
- Global Exception Handling


🏗️ Tech Stack

| Layer      | Technology           |
| ---------- | -------------------- |
| Backend    | Java, Spring Boot    |
| Security   | Spring Security, JWT |
| Database   | MySQL                |
| ORM        | Hibernate / JPA      |
| Build Tool | Maven                |
| Utilities  | Lombok               |
| Testing    | Postman              |


🗄️ Database Design

The system is designed using relational database principles with the following core entities:

- Users
- Orders
- Packages
- Locations
- DeliveryAssignment
- PackageTracking

FULL SYSTEM FLOW (YOUR SCENARIO)
Customer → Place Order
        ↓
Order → Packages (CREATED)
        ↓
Manager → Assign Package → Agent
        ↓
Agent → Pickup → IN_TRANSIT → OUT_FOR_DELIVERY
        ↓
Success → DELIVERED ✅
Failure → FAILED ❌
        ↓
Manager → Reassign → Agent
        ↓
Repeat until DELIVERED
        ↓
Customer → Track anytime



📌 ER Diagram

🔗 API Overview
🔐 Authentication APIs
Register
- POST /courier/auth/signup

Login
- POST /courier/auth/login

Customer: 
📦 Order APIs
Place Order
- POST /courier/orders

🚚 Delivery APIs
Assign Package
- POST /courier/manager/assign

👷 Agent APIs
- Get Assigned Packages
- GET /courier/agent/packages

Update Status
- POST /courier/agent/update-status

📍 Tracking APIs
Track Package
- GET /courier/tracking/{packageId}

👑 Admin APIs
- Get All Users
- Delete User
- Update User Roles

🔐 Role-Based Access
| Role     | Permissions                   |
| -------- | ----------------------------- |
| ADMIN    | Full system access            |
| MANAGER  | Assign & monitor deliveries   |
| AGENT    | Update delivery status        |
| CUSTOMER | Place orders & track packages |


⚙️ Key Highlights
- Secure authentication using JWT
- Clean architecture with DTO + Service + Repository layers
- Efficient database handling using JPA & Hibernate
- Asynchronous processing for performance optimization
- Scalable and modular design

      
🔄 System Flow
Customer → Place Order  
        → Manager Assigns Delivery  
        → Agent Delivers Package  
        → System Updates Tracking  
        → Customer Tracks Package


👨‍💻 Author

Harsh Chopda
Backend Developer

📍 Open to Work (Remote / On-site)
















