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
			System.out.println("====�����ͻ���====");
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
			// �����������˵������
			DataOutputStream dOut = null;
			DataInputStream dIn = null;
			try {
				dOut = new DataOutputStream(socket.getOutputStream());
				dIn = new DataInputStream(socket.getInputStream());
				// ��ȡ�����ļ�(�����ļ���)
				File f = new File("F:\\file_test\\client");
				File[] fs = f.listFiles();
				int count = fs.length;
				System.out.println("====�ͻ��˷����ļ�����====");
				// ���������������
				dOut.writeInt(count);
				System.out.println("====�ͻ��˿�ʼ�����ļ�====");
				// ����ÿһ���ļ����η��������
				for (int i = 0; i < count; i++) {
					System.out.println("�ͻ��˷��͵�" + (i + 1) + "���ļ�");
					// ���δӱ����ж�ȡ���ļ���
					FileInputStream in = new FileInputStream(fs[i]);
					byte[] buf = new byte[1024];
					int num;
					while ((num = in.read(buf)) != -1) {
						dOut.write(buf, 0, num);
					}
					dOut.flush();
					in.close();
					// ��������Ƿ�������
					int a = dIn.readInt();
					System.out.println("����˽��յ�" + a + "���ļ����");
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
