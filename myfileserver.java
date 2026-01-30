import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class myfileserver {
    // constants
    private static final int PORT = 9000;
    private static final int THREAD_POOL_SIZE = 10;

    // track requests
    private static AtomicInteger totalRequests = new AtomicInteger(0);
    private static AtomicInteger successfulRequests = new AtomicInteger(0);

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server running on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                executor.execute(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            System.out.println("Server crashed: " + e.getMessage());
        }
    }

    // handle client requests
    static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try (
                    DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
                    DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream())
            ) {
                // get filename from client
                String fileName = dis.readUTF();

                int requestId = totalRequests.incrementAndGet();
                String clientIP = clientSocket.getInetAddress().getHostAddress();
                System.out.println("REQ " + requestId + ": File " + fileName + " requested from " + clientIP);

                // check if file exists
                File file = new File(fileName);

                // In myfileserver.java ClientHandler.run() method
// Replace the success case output with:

                if (file.exists() && file.isFile()) {
                    int successfulSoFar = successfulRequests.incrementAndGet();

                    // send file found and stats
                    dos.writeBoolean(true);
                    dos.writeInt(totalRequests.get());
                    dos.writeInt(successfulRequests.get());

                    System.out.println("REQ " + requestId + ": Successful");
                    System.out.println("REQ " + requestId + ": Total successful requests so far = " + successfulSoFar);

                    // send file size
                    dos.writeLong(file.length());

                    // send file
                    try (FileInputStream fis = new FileInputStream(file)) {
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = fis.read(buffer)) != -1) {
                            dos.write(buffer, 0, bytesRead);
                        }
                    }

                    System.out.println("REQ " + requestId + ": File transfer complete");

                } else {
                    // file not found
                    dos.writeBoolean(false);
                    dos.writeInt(totalRequests.get());
                    dos.writeInt(successfulRequests.get());

                    System.out.println("REQ " + requestId + ": Not Successful");
                    System.out.println("REQ " + requestId + ": Total successful requests so far = " + successfulRequests.get());
                }

                dos.flush();

            } catch (IOException e) {
                System.out.println("Client error: " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.out.println("Error closing socket: " + e.getMessage());
                }
            }
        }
    }
}