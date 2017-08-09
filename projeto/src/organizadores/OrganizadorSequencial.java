package organizadores;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import entidades.Aluno;
import interfaces.IOrganizador;
import helper.Conversor;

public class OrganizadorSequencial implements IOrganizador {

	private FileChannel channel;
	
	public OrganizadorSequencial(String fileName) throws IOException {
		File file = new File(fileName);
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		this.channel = raf.getChannel();
	}
	
	private long getPositionBin(long matric) throws IOException {
		long matricula;
		int registros = (int) this.channel.size() / Aluno.tamanho;
		int esq = 0; int dir = registros - 1; int meio;
		while (esq <= dir) {
			meio = (esq + dir) / 2;
			ByteBuffer buffer = ByteBuffer.allocate(8);
			this.channel.read(buffer, meio * Aluno.tamanho);
			buffer.flip();
			matricula = buffer.getLong();
			if (matricula == matric)
				return meio * Aluno.tamanho;
			if (matricula < matric) {
				esq = meio + 1;
			} else {
				dir = meio - 1;
			}
		}
		return -1;
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
							this.channel.read(buffer_read, (j * Aluno.tamanho) - Aluno.tamanho);
							buffer_read.flip();
							this.channel.write(buffer_read, j * Aluno.tamanho);
						}
						this.channel.write(buffer, i * Aluno.tamanho);
						break;
					} else if (i+1 == registros) {
						this.channel.write(buffer, this.channel.size());
					}
				}
			}
		}
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
			for (int i = (int) (position / Aluno.tamanho); i < registros; i++) {
				ByteBuffer buffer_read = ByteBuffer.allocate(Aluno.tamanho);
				this.channel.read(buffer_read, (i * Aluno.tamanho) + Aluno.tamanho);
				buffer_read.flip();
				this.channel.write(buffer_read, i * Aluno.tamanho);
			}
			this.channel.truncate(this.channel.size() - Aluno.tamanho);
			return true;
		}
	}

}
