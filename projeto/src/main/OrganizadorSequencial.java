package main;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import entidades.Aluno;
import interfaces.IOrganizador;
import util.Conversor;

public class OrganizadorSequencial implements IOrganizador {

	private FileChannel channel;
	
	public OrganizadorSequencial(String fileName) throws IOException {
		File file = new File(fileName);
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		this.channel = raf.getChannel();
	}
	
	private long getPosition(long matric) throws IOException {
		this.channel.position(0);
		long matricula;
		int registros = (int) this.channel.size() / Aluno.tamanho;
		for (int i = 0; i < registros; i++) {
			ByteBuffer buffer = ByteBuffer.allocate(8);
			this.channel.read(buffer, i * Aluno.tamanho);
			buffer.flip();
			matricula = buffer.getLong();
			if (matricula == matric) {
				return i * Aluno.tamanho;
			}
		}
		return -1;
	}
	
	@Override
	public void addAluno(Aluno a) throws IOException {
		long position = this.getPosition(a.getMatric());
		int registros = (int) this.channel.size() / Aluno.tamanho;
		ByteBuffer buffer = Conversor.toByteBuffer(a);
		if (position == -1) {
			if(registros == 0) {
				this.channel.write(buffer, 0);
			} else {
				for (int i = 0; i < registros; i++) {
					ByteBuffer buffer_leitura = ByteBuffer.allocate(Aluno.tamanho);
					this.channel.read(buffer_leitura, i * Aluno.tamanho);
					Aluno tmp_a = Conversor.toAluno(buffer_leitura);
					if (a.getMatric() < tmp_a.getMatric()) {
						for (int j = registros; j > i; j--) {
							ByteBuffer buffer_read = ByteBuffer.allocate(Aluno.tamanho);
							channel.read(buffer_read, (j * Aluno.tamanho) - Aluno.tamanho);
							buffer_leitura.flip();
							channel.write(buffer_read, (j * Aluno.tamanho));
						}
						channel.write(buffer, i * Aluno.tamanho);
						break;
					}
				}
			}
		}
	}

	@Override
	public Aluno getAluno(long matric) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delAluno(long matric) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

}
