
# RMIT COSC2299 SEPT Major Project

# Group Information

## Group-P09-03

## Members
* Preeti Goel (s3879991)
* Ashmit Sachan (s3873827)
* Kaiyang Zheng (s3992987)
* Aphisith Siphaxay (s3987059)
* Kai Hei Kong (s3971187)
* Henry Van Toledo (s3849054)

## Records

* Github repository: https://github.com/cosc2299-2024/team-project-group-p09-03
* Github Project Board: https://github.com/orgs/cosc2299-2024/projects/121
* Link to Teams: https://teams.microsoft.com/l/team/19%3AT_jSCB2ca-T2PLz5NJjtojXIpdIL-DLIeWa01AjZxGo1%40thread.tacv2/conversations?groupId=1c4b5113-9c78-4949-83f4-c76ef19069cd&tenantId=d1323671-cdbe-4417-b4d4-bdb24b51316b


See [Instructions](INSTRUCTIONS.md)

# How to use Tailwind CSS
Tailwind CSS has already been configured to the application. Make sure you have Node installed on your machine.

You need to compile Tailwind CSS present in the HTML to reflect changes in the Browser using the following command:

```sh
npx tailwindcss -i ./src/main/resources/static/css/tailwind.css -o ./src/main/resources/static/css/main.css --watch
```

I am still working on making this process automated and will update this configuration ASAP.


# Dockerized Spring Boot Application with MySQL

## Introduction

This README file provides detailed instructions on how to set up, install, and run a Dockerized Spring Boot application with a MySQL database. The application uses Docker Compose to manage the different services, ensuring a smooth setup process. Additionally, it covers the installation and configuration of MySQL Server and MySQL Workbench on both Windows and macOS, as well as instructions for building the application using Maven.

## Prerequisites

Before you can run the Dockerized application and set up MySQL, ensure you have the following installed:

1. **Docker Desktop** (includes Docker Engine and Docker Compose)
2. **Git** (optional, for cloning the repository)
3. **MySQL Server**
4. **MySQL Workbench**
5. **Maven** (for building the Spring Boot application)

### Install Docker Desktop

#### Windows and macOS

1. **Download Docker Desktop**:
    - Visit the Docker official website: [Docker Desktop](https://www.docker.com/products/docker-desktop)
    - Download the installer for your operating system.

2. **Install Docker Desktop**:
    - **Windows**:
        - Run the downloaded installer.
        - Follow the installation instructions.
        - During installation, ensure to enable the **WSL 2** integration (if prompted).
    - **macOS**:
        - Open the downloaded `.dmg` file.
        - Drag the Docker icon to the Applications folder.

3. **Start Docker Desktop**:
    - Launch Docker from your Applications folder or Start Menu.
    - Docker will ask for your password (on macOS) to install some additional components.
    - Wait for Docker to initialize (you'll see a green status bar in the Docker Dashboard when it's ready).

### Verify Docker Installation

To verify Docker is installed correctly, open a terminal (or Command Prompt/PowerShell on Windows) and run:

```sh
docker --version
docker-compose --version
```

These commands should return the installed versions of Docker and Docker Compose.

### Install MySQL Server and MySQL Workbench

#### Windows and macOS

1. **Download MySQL Installer**:
    - Visit the MySQL official website: [MySQL Downloads](https://dev.mysql.com/downloads/installer/)
    - Download the **MySQL Community Server** and **MySQL Workbench**.

2. **Install MySQL Server**:
    - **Windows**:
        - Run the MySQL Installer.
        - Select "MySQL Server" from the list of available products.
        - Follow the installation instructions.
        - Configure the server as needed (you can use the default settings for a basic setup).
        - Set a strong root password and remember it, as you’ll need it later.
    - **macOS**:
        - Open the `.dmg` file you downloaded.
        - Run the installer and follow the prompts to install MySQL Server.
        - Use the MySQL preference pane to start the server automatically at startup.
        - Set a strong root password during installation.

3. **Install MySQL Workbench**:
    - **Windows**:
        - MySQL Workbench is typically included in the MySQL Installer. Ensure it is selected during installation.
    - **macOS**:
        - Drag the MySQL Workbench icon to the Applications folder.

4. **Start MySQL Server**:
    - **Windows**:
        - Open the MySQL Installer or Services Manager and start the MySQL service.
    - **macOS**:
        - Use the MySQL preference pane in System Preferences to start the server.

5. **Launch MySQL Workbench**:
    - Open MySQL Workbench from the Start Menu (Windows) or Applications folder (macOS).
    - Create a new connection to your MySQL Server using `localhost` and the root credentials you set during installation.
    - Test the connection to ensure everything is working correctly.

### Install Maven

Skip this step if you are using IntelliJ Idea.

If Maven is not already installed, follow these steps to install it:

1. **Download Maven**:
    - Visit the Apache Maven official website: [Maven Downloads](https://maven.apache.org/download.cgi)
    - Download the binary archive for your operating system.

2. **Install Maven**:
    - **Windows**:
        - Extract the downloaded archive to a directory of your choice (e.g., `C:\Program Files\Apache\Maven`).
        - Add the `bin` directory of Maven to your system's `PATH` environment variable.
    - **macOS**:
        - Use Homebrew to install Maven by running the following command in the terminal:
          ```sh
          brew install maven
          ```

3. **Verify Maven Installation**:
    - Open a terminal (or Command Prompt/PowerShell on Windows) and run:
      ```sh
      mvn --version
      ```
    - This should display the installed version of Maven.

## Building the Spring Boot Application

### Step 1: Clone the Repository (Optional)

If you haven't already cloned the repository, you can do so using Git:

```sh
git clone git@github.com:cosc2299-2024/team-project-group-p09-03.git
cd team-project-group-p09-03
```

### Step 2: Build the Application Using Maven

Before running the Dockerized application, you need to build the Spring Boot application to create the JAR file.

1. **Run Maven Install**:
    - In the root directory of your project (where the `pom.xml` file is located), run the following command:
      ```sh
      mvn clean install
      ```
    - This command does the following:
        - Cleans any previously compiled files.
        - Compiles the application source code.
        - Runs tests (if any).
        - Packages the application into a JAR file.

2. **Confirm the JAR File Creation**:
    - After the Maven build is complete, navigate to the `target` directory in your project folder.
    - Confirm that a JAR file (e.g., `webapp-0.0.1-SNAPSHOT.jar`) has been created.
    - This JAR file will be used by the Docker container to run your Spring Boot application.

### Step 3: Configure Environment Variables

Ensure that the `docker-compose.yml` file is correctly configured. If your application requires environment variables (e.g., for MySQL credentials), they should be specified in a `.env` file or directly in the `docker-compose.yml` file.

Here is an example `.env` file:

```env
MYSQL_ROOT_PASSWORD=rootpassword
MYSQL_DATABASE=yourdatabase
MYSQL_USER=user
MYSQL_PASSWORD=password
```

### Step 4: Build and Start the Containers

In the root directory of your project (where the `docker-compose.yml` file is located), run the following command to build and start the Docker containers:

```sh
docker-compose up --build
```

This command does the following:
- Builds the Docker images for your Spring Boot application and MySQL database.
- Starts the containers for the application and database.
- Logs the output of the running services to your terminal.

### Step 5: Access the Application

Once the containers are running, you can access your Spring Boot application via the browser:

- **Windows/Mac**: Open your web browser and navigate to `http://localhost:8080`.

The MySQL database will also be accessible using the configured ports (usually `3306`).

### Step 6: Stop the Containers

To stop the running containers, press `CTRL+C` in the terminal where the containers are running. Then, to remove the containers, networks, and volumes created by `docker-compose up`, run:

```sh
docker-compose down
```

This will stop and remove the containers, but the images and volumes will remain intact.

## Setting Up MySQL Workbench with Dockerized MySQL

After running the Docker containers, you may want to connect to the MySQL database from MySQL Workbench.

1. **Open MySQL Workbench**.
2. **Create a New Connection**:
    - Click on the `+` icon next to `MySQL Connections`.
    - Set the `Connection Name` (e.g., "Docker MySQL").
    - Set the `Hostname` to `localhost`.
    - Set the `Port` to the port specified in your `docker-compose.yml` (default is `3306`).
    - Set the `Username` to `root` or the user defined in your `.env` or `docker-compose.yml`.
    - Click `Test Connection`.
    - Enter the password (the one set in `.env` or `docker-compose.yml`).

3. **Manage Your Database**:
    - Once the connection is successful, you can start managing your database directly from MySQL Workbench.

## Troubleshooting

### Common Issues

- **Port Conflicts**: Ensure that the ports specified in `docker-compose.yml` are not being used by other applications.
- **Docker Initialization Issues**: If Docker fails to start, ensure that virtualization is enabled in your BIOS settings (especially for Windows users).
- **Permission Issues**: On macOS, you might need to grant Docker permission to access files and directories.

### Logs and Debugging

To view the logs for a specific container, run:

```sh
docker-compose logs <service_name>
```

Replace `<service_name>` with the name of the service (e.g., `app`, `db`) as defined in your `docker-compose.yml`.

## Conclusion

You should now have a running Dockerized Spring Boot application with a MySQL database, and your MySQL Workbench configured to connect to both the Dockerized and local MySQL instances. The Maven build process ensures that your application is properly compiled and packaged before being deployed in the Docker container. For any additional configuration or custom setup, refer to the Docker, MySQL, and Maven documentation. Happy coding!

