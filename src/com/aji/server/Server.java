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
			// ����˼���
			ServerSocket server = new ServerSocket(8888);
			System.out.println("����������");
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
			// ��ʼ��������
			DataInputStream dIn = null;
			DataOutputStream dOut = null;
			try {
				dIn = new DataInputStream(socket.getInputStream());
				dOut = new DataOutputStream(socket.getOutputStream());
				// ���տͻ��˴������ļ�����
				int count = dIn.readInt();
				System.out.println("======�ͻ�����" + count + "���ļ�Ҫ������======");
				for (int i = 0; i < count; i++) {
					System.out.println("��ʼ���յ�" + (i + 1) + "��");
					try {
						// ʱ��̫���ˣ��Ӹ������
						File f = new File("F:\\file_test\\server\\"
								+ System.currentTimeMillis() + "-"
								+ r.nextInt(1000000) + ".jpg");
						// ���浽����˴��̵������
						FileOutputStream out = new FileOutputStream(f);
						// ��ʼ�ӿͻ���ȡ���ݲ��ұ��浽����˴���
						byte[] buf = new byte[1024];
						int num;
						while ((num = dIn.read(buf)) != -1) {
							out.write(buf, 0, num);
							// �������ļ������һ�����֣�ֱ���˳��ļ���ȡѭ��������һ���ļ�
							if (num < 1024) {
								break;
							}
						}
						out.flush();
						out.close();
						// ��������ϵ�״̬���ظ��ͻ��ˣ��ÿͻ����ϴ���һ��
						System.out.println("��" + (i + 1) + "���������");
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
			System.out.println("===�������===");
		}
	}

}
