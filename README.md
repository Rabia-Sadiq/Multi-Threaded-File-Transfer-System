
# Multi-Threaded File Transfer System (Java)

## 📌 Overview
This project implements a **client–server file transfer system** using **Java TCP sockets**.  
The server can handle **multiple client requests concurrently** using a thread pool, while clients can request and download files reliably from the server.

The system also tracks:
- Total number of client requests
- Total number of successful file transfers

---

## 🛠️ Technologies Used
- Java
- TCP Socket Programming
- Multithreading (ExecutorService)
- File I/O Streams
- Atomic Variables (Thread-safe counters)

---

## ⚙️ System Architecture

### Server
- Listens on a fixed port
- Accepts multiple client connections
- Uses a thread pool to handle concurrent clients
- Maintains request statistics using `AtomicInteger`
- Streams files in chunks for efficient transfer

### Client
- Connects to server using IP and port
- Sends requested file name
- Receives server response and statistics
- Downloads the file if available

---

## 📂 Project Structure
```

myfileserver.java   // Multi-threaded file server
myfileclient.java   // File requesting client

````

---

## 🚀 How to Run

### 1️⃣ Compile
```bash
javac myfileserver.java
javac myfileclient.java
````

### 2️⃣ Start Server

```bash
java myfileserver
```

### 3️⃣ Run Client

```bash
java myfileclient <server_IP> <port> <filename>
```

Example:

```bash
java myfileclient 127.0.0.1 9000 sample.txt
```

---

## 📊 Features

* Multi-client support using thread pool
* Thread-safe request tracking
* Chunk-based file transfer
* Reliable error handling
* Simple and efficient protocol design

---

## 📈 Learning Outcomes

* Practical understanding of TCP networking
* Multithreading and concurrency control
* Efficient file transfer mechanisms
* Real-world client–server architecture design

---

## 📌 Future Improvements

* Add authentication
* Support file upload
* Encrypt file transfer
* GUI-based client

---

