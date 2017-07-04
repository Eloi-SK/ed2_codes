package main;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import util.Conversor;
import entidades.Aluno;
import interfaces.IOrganizador;

public class OrganizadorSimples implements IOrganizador {
	
	private FileChannel channel;
	
	public OrganizadorSimples(String fileName) throws IOException {
		File file = new File(fileName);
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		this.channel = raf.getChannel();
	}

	@Override
	public void addAluno(Aluno a) throws IOException {
		this.channel.write(Conversor.toByteBuffer(a), this.channel.size());
	}

	@Override
	public Aluno getAluno(long matric) throws IOException {
		Aluno a;
		int registros = (int) this.channel.size() / Aluno.tamanho;
		for (int i = 0; i < registros; i++) {
			ByteBuffer buffer = ByteBuffer.allocate(Aluno.tamanho);
			this.channel.read(buffer, i * Aluno.tamanho);
			a = Conversor.toAluno(buffer);
			if (a.getMatric() == matric)
				return a;
		}
		return null;
	}

	@Override
	public boolean delAluno(long matric) throws IOException {
		Aluno a;
		int indice = -1;
		int registros = (int) this.channel.size() / Aluno.tamanho;
		for (int i = 0; i < registros; i++) {
			ByteBuffer buffer = ByteBuffer.allocate(Aluno.tamanho);
			this.channel.read(buffer, i * Aluno.tamanho);
			a = Conversor.toAluno(buffer);
			if (a.getMatric() == matric)
				indice = i;
		}
		if (indice == -1) {
			return false;
		} else if (indice == 0 && registros == 1) {
			this.channel.truncate(0);
			return true;
		} else if (indice + 1 == registros) {
			this.channel.truncate(this.channel.size() - Aluno.tamanho);
			return true;
		} else {
			ByteBuffer buff = ByteBuffer.allocate(Aluno.tamanho);
			this.channel.read(buff, this.channel.size() - Aluno.tamanho);
			buff.flip();
			this.channel.write(buff, indice * Aluno.tamanho);
			this.channel.truncate(this.channel.size() - Aluno.tamanho);
			return true;
		}
	}
}
