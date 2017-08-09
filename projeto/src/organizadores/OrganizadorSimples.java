package organizadores;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import helper.Conversor;
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
		long position = this.getPosition(matric);
		if (position != -1) {
			ByteBuffer buffer = ByteBuffer.allocate(Aluno.tamanho);
			this.channel.read(buffer, position);
			return Conversor.toAluno(buffer);
		}
		return null;
	}

	@Override
	public boolean delAluno(long matric) throws IOException {
		int registros = (int) this.channel.size() / Aluno.tamanho;
		long position = this.getPosition(matric);
		if (position == -1) {
			return false;
		} else if(position == 0 && registros == 1) {
			this.channel.truncate(0);
			return true;
		} else if (position == this.channel.size() - Aluno.tamanho) {
			this.channel.truncate(this.channel.size() - Aluno.tamanho);
			return true;
		} else {
			ByteBuffer buff = ByteBuffer.allocate(Aluno.tamanho);
			this.channel.read(buff, this.channel.size() - Aluno.tamanho);
			buff.flip();
			this.channel.write(buff, position);
			this.channel.truncate(this.channel.size() - Aluno.tamanho);
			return true;
		}
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

}
