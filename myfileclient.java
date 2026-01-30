import java.io.*;
import java.net.*;

public class myfileclient {
    // buffer size for file transfer
    private static final int BUFFER_SIZE = 4096;

    public static void main(String[] args) {
        // check if we got enough args
        if (args.length < 3) {
            System.out.println("Run it like: java myfileclient <server_IP> <port> <filename>");
            return;
        }

        String serverIp = args[0];
        int serverPort = Integer.parseInt(args[1]); // get port
        String filename = args[2];


        try (Socket socket = new Socket(serverIp, serverPort);
             DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
             DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()))) {

            // send filename to server
            out.writeUTF(filename);
            out.flush();

            // get server response
            boolean found = in.readBoolean();
            int N = in.readInt(); // total requests
            int M = in.readInt(); // successful ones

            System.out.println("File " + filename + (found ? " found at server" : " not found at server"));
            System.out.printf("Server handled %d requests, %d were successful\n", N, M);

            if (found) {
                long fileSize = in.readLong();
                System.out.println("Downloading file " + filename);

                // save file as downloaded_filename
                try (FileOutputStream fos = new FileOutputStream("downloaded_" + filename)) {
                    byte[] buffer = new byte[BUFFER_SIZE];
                    long remaining = fileSize;
                    while (remaining > 0) {
                        int read = in.read(buffer, 0, (int) Math.min(buffer.length, remaining));
                        if (read == -1) throw new EOFException("Stream ended early");
                        fos.write(buffer, 0, read);
                        remaining -= read;
                    }
                }
                System.out.println("Download done. Saved as downloaded_" + filename);
            }

        } catch (ConnectException e) {
            System.err.println("Can't connect: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IO error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Something went wrong: " + e.getMessage());
        }
    }
}