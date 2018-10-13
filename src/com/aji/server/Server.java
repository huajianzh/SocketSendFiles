package com.aji.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Server {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Server server = new Server();
		server.startServer();
	}

	public void startServer() {
		try {
			// 服务端监听
			ServerSocket server = new ServerSocket(8888);
			System.out.println("服务器启动");
			Socket socket = server.accept();
			new ReceivThread(socket).start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	class ReceivThread extends Thread {
		Socket socket;
		private Random r;

		public ReceivThread(Socket socket) {
			this.socket = socket;
			r = new Random();
		}

		public void run() {
			// 开始接收内容
			DataInputStream dIn = null;
			DataOutputStream dOut = null;
			try {
				dIn = new DataInputStream(socket.getInputStream());
				dOut = new DataOutputStream(socket.getOutputStream());
				// 接收客户端传来的文件数量
				int count = dIn.readInt();
				System.out.println("======客户端有" + count + "个文件要传上来======");
				for (int i = 0; i < count; i++) {
					System.out.println("开始接收第" + (i + 1) + "个");
					try {
						// 时间太快了，加个随机数
						File f = new File("F:\\file_test\\server\\"
								+ System.currentTimeMillis() + "-"
								+ r.nextInt(1000000) + ".jpg");
						// 保存到服务端磁盘的输出流
						FileOutputStream out = new FileOutputStream(f);
						// 开始从客户端取数据并且保存到服务端磁盘
						byte[] buf = new byte[1024];
						int num;
						while ((num = dIn.read(buf)) != -1) {
							out.write(buf, 0, num);
							// 读到该文件的最后一个部分，直接退出文件读取循环，读下一个文件
							if (num < 1024) {
								break;
							}
						}
						out.flush();
						out.close();
						// 将接收完毕的状态返回给客户端，让客户端上传下一个
						System.out.println("第" + (i + 1) + "个接收完毕");
						dOut.writeInt(i + 1);
					} catch (IOException e) {
						e.printStackTrace();
						break;
					}
				}
			} catch (IOException e1) {
				e1.printStackTrace();
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
			System.out.println("===接收完毕===");
		}
	}

}
