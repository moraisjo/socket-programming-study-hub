import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
	public static void main(String args[]) {

		ServerSocket listenSocket = null;

		try {
			int serverPort = 7896;

			listenSocket = new ServerSocket(serverPort);
			System.out.println("Servidor: ouvindo porta TCP/" + serverPort + ".");

			while (true) {
				Socket clientSocket = listenSocket.accept();
				new Connection(clientSocket);
			}
		} catch (IOException e) {
			System.out.println("Listen socket:" + e.getMessage());
		} finally {
			if (listenSocket != null)
				try {
					listenSocket.close();
					System.out.println("Servidor: liberando porta TCP/7896.");
				} catch (IOException e) {
					/* close falhou */
				}
		}
	}
}

class Connection extends Thread {
	DataInputStream in;
	DataOutputStream out;
	Socket clientSocket;

	public Connection(Socket aClientSocket) {
		try {
			clientSocket = aClientSocket;
			System.out.println("Client port: " + clientSocket.getPort() + "   address:  " + clientSocket.getInetAddress());
			in = new DataInputStream(clientSocket.getInputStream());
			out = new DataOutputStream(clientSocket.getOutputStream());
			this.start();
		} catch (IOException e) {
			System.out.println("Conexao:" + e.getMessage());
		}
	}

	public void run() {
		try {
			int i = 0;
			while (i < 3) {
				String data = in.readUTF();
				System.out.println("Recebido: " + data);
				Thread.sleep(1000);
				out.writeUTF(data);
				i++;
			}

		} catch (EOFException e) {
			System.out.println("EOF:" + e.getMessage());
		} catch (IOException e) {
			System.out.println("readline:" + e.getMessage());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			try {
				clientSocket.close();
				System.out.println("Servidor: fechando conexao com cliente.");
			} catch (IOException e) {
				/* close falhou */
			}
		}
	}
}
