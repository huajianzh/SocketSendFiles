package com.aji.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Socket socket = new Socket("127.0.0.1", 8888);
			System.out.println("====启动客户端====");
			new SendThread(socket).start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	static class SendThread extends Thread {
		Socket socket;

		public SendThread(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			// 打开输出到服务端的输出流
			DataOutputStream dOut = null;
			DataInputStream dIn = null;
			try {
				dOut = new DataOutputStream(socket.getOutputStream());
				dIn = new DataInputStream(socket.getInputStream());
				// 读取本地文件(遍历文件夹)
				File f = new File("F:\\file_test\\client");
				File[] fs = f.listFiles();
				int count = fs.length;
				System.out.println("====客户端发送文件数量====");
				// 发送数量给服务端
				dOut.writeInt(count);
				System.out.println("====客户端开始发送文件====");
				// 遍历每一个文件依次发给服务端
				for (int i = 0; i < count; i++) {
					System.out.println("客户端发送第" + (i + 1) + "个文件");
					// 依次从本地中读取的文件流
					FileInputStream in = new FileInputStream(fs[i]);
					byte[] buf = new byte[1024];
					int num;
					while ((num = in.read(buf)) != -1) {
						dOut.write(buf, 0, num);
					}
					dOut.flush();
					in.close();
					// 检测服务端是否接收完毕
					int a = dIn.readInt();
					System.out.println("服务端接收第" + a + "个文件完毕");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (null != dIn) {
					try {
						dIn.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (null != dOut) {
					try {
						dOut.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

}
