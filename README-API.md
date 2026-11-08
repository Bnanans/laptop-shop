Quarkus Laptop Shop - REST API Endpoints

GET /api/laptops
GET /api/laptops/{id}
GET /api/laptops/search?q=keyword
POST /api/laptops
PUT /api/laptops/{id}
DELETE /api/laptops/{id}

POST /api/auth/login
GET/POST /api/customers
GET/POST /api/orders

Set the `DB_USERNAME` and `DB_PASSWORD` environment variables for your local MySQL account, then run the app with Maven. Do not commit credentials to the repository.

Quick run (dev):

1. Create database `laptop_shop` in MySQL and set `DB_USERNAME` and `DB_PASSWORD`.
2. Run dev mode:

	mvnw quarkus:dev

3. If you need the admin UI, create an admin account locally; this project does not seed a default admin account.
4. Open frontend:
	- Admin UI: http://localhost:8080/admin/login.html
	- Shop: http://localhost:8080/user/index.html

Notes:
- Uploaded files are saved to the `uploads/` folder in the project root and served at `/uploads/{file}` if your server serves static files from there.
- This demo stores passwords in plain text. Do not deploy it with real user accounts or production data without first implementing password hashing and production-grade authentication.
