# Expense Tracker Project

## Introduction

Welcome to the Expense Tracker project! This application helps you manage your expenses efficiently by providing a user-friendly interface and powerful features.

## Getting Started

To get started with the Expense Tracker project, follow these steps:

### 1. Install Git

If Git is not installed on your system, you can download and install it from the [official Git website](https://git-scm.com/).

### 2. Clone Repository

Clone the Expense Tracker repository using the following command:

git clone https://github.com/parazit-IRAN/expense-tracker.git

### 3. Navigate to Project Directory

Change your current directory to the expense-tracker folder:

cd expense-tracker

### 4. Install Docker

Docker needs to be installed on your system. You can download and install it from the [official Docker website](https://www.docker.com/get-started).

### 5. Install Docker Compose

Docker Compose is required to manage multi-container Docker applications. You can find installation instructions in the [official Docker Compose documentation](https://docs.docker.com/compose/install/).

### 6. Run the Application

Execute the following command to start the application:

docker-compose up

This command will download dependencies and start the application.

### 7. Get App IP Address

You can find the IP address of the application using the following command:

docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' app


### 8. Access Swagger UI

Open the following URL in your browser:

http://{your-IP}:8484/swagger-ui/index.html


This will give you access to the Swagger UI for interacting with the API.

### 9. Create User

You must first create a user using the User API.

### 10. Authenticate and Get Token

Obtain an authentication token from the Authenticate API.

### 11. Create Account

Create an account of type CARD or CASH using the Account API.

### 12. Manage Categories

Per user, there is a default category of expense, but you can also create custom categories using the Category API.

### 13. Create Transactions

Now you can create transactions for each category.

Feel free to explore and enjoy working with the Expense Tracker!
