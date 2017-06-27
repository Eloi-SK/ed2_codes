package main;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws IOException {
		inserirAlunoInicio();
		inserirAlunoFinal();
		inserirAlunoNaPosicao(0);
		exibeTodos();
	}
		
	
	public static void inserirAlunoInicio() throws IOException {
		int mat;
		String nome;
		short curso;
		
		Scanner in = new Scanner(System.in);
		
		System.out.print("MATRÍCULA: ");
		mat = in.nextInt();
		System.out.print("NOME: ");
		nome = in.next();
		System.out.print("CURSO: ");
		curso = in.nextShort();

		
		ByteBuffer buffer = ByteBuffer.allocate(50);
		buffer.putInt(mat);
		byte[] b_nome = new byte[44];
		b_nome = nome.getBytes();
		buffer.put(b_nome);
		buffer.position(48);
		buffer.putShort(curso);
		buffer.flip();
		
		File file = new File("alunos.db");
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		FileChannel channel = raf.getChannel();
		
		int qtd_registros = (int) (channel.size() / 50);
		
		if (qtd_registros > 0) {
			for (int i = qtd_registros; i > 0; i--) {
				ByteBuffer buffer_leitura = ByteBuffer.allocate(50);
				channel.read(buffer_leitura, (i * 50) - 50);
				buffer_leitura.flip();
				channel.write(buffer_leitura, (i * 50));
			}
			channel.write(buffer, 0);
		} else {
			channel.write(buffer, 0);
		}
		
		channel.close();
		raf.close();
		in.close();
	}
	public static void inserirAlunoFinal() throws IOException {
		int mat;
		String nome;
		short curso;
		
		Scanner in = new Scanner(System.in);
		
		System.out.print("MATRÍCULA: ");
		mat = in.nextInt();
		System.out.print("NOME: ");
		nome = in.next();
		System.out.print("CURSO: ");
		curso = in.nextShort();
		
		File file = new File("alunos.db");
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		FileChannel channel = raf.getChannel();
			
		ByteBuffer buffer = ByteBuffer.allocate(50);
		
		buffer.putInt(mat);
		byte[] b_nome = new byte[44];
		b_nome = nome.getBytes();
		buffer.put(b_nome);
		buffer.position(48);
		buffer.putShort(curso);
		buffer.flip();
				
		channel.write(buffer, channel.size());
			
		channel.close();
		raf.close();
		in.close();
	}
	
	public static void inserirAlunoNaPosicao(int pos) throws IOException {
		int mat;
		String nome;
		short curso;
		
		Scanner in = new Scanner(System.in);
		
		System.out.print("MATRÍCULA: ");
		mat = in.nextInt();
		System.out.print("NOME: ");
		nome = in.next();
		System.out.print("CURSO: ");
		curso = in.nextShort();

		
		ByteBuffer buffer = ByteBuffer.allocate(50);
		buffer.putInt(mat);
		byte[] b_nome = new byte[44];
		b_nome = nome.getBytes();
		buffer.put(b_nome);
		buffer.position(48);
		buffer.putShort(curso);
		buffer.flip();
		
		File file = new File("alunos.db");
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		FileChannel channel = raf.getChannel();
		
		int qtd_registros = (int) (channel.size() / 50);
		
		for (int i = qtd_registros; i > pos; i--) {
			ByteBuffer buffer_leitura = ByteBuffer.allocate(50);
			channel.read(buffer_leitura, (i * 50) - 50);
			buffer_leitura.flip();
			channel.write(buffer_leitura, (i * 50));
		}
		channel.write(buffer,  (pos * 50));
		
		raf.close();
		channel.close();
		in.close();
	}
	
	public static void exibeTodos() throws IOException {
		int mat;
		String nome;
		short curso;
		
		File file = new File("alunos.db");
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		FileChannel channel = raf.getChannel();
		
		int qtd_registros = (int) (channel.size() / 50);
		
		for (int i = 0; i < qtd_registros; i++) {
			ByteBuffer buffer_leitura = ByteBuffer.allocate(50);
			channel.read(buffer_leitura, (i * 50));
			buffer_leitura.flip();
			mat = buffer_leitura.getInt();
			byte[] b_nome = new byte[44];
			buffer_leitura.get(b_nome);
			nome = new String(b_nome);
			buffer_leitura.position(48);
			curso = buffer_leitura.getShort();
			System.out.println("Matrícula: " + mat);
			System.out.println("Nome: " + nome);
			System.out.println("Curso: " + curso);
			System.out.println("------------------------------");
		}
		
		raf.close();
		channel.close();
	}
	
}
