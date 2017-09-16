package organizadores;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import helper.Conversor;
import entidades.Aluno;
import interfaces.IOrganizador;

public class OrganizadorBrent implements IOrganizador {

	private long MAX;
	private static final int VAZIO = 0;
	private static final int DELETADO = -1;
	
	private FileChannel channel;
	
	public OrganizadorBrent(String fileName, long tableSize) throws IOException {
		MAX = tableSize;
		File file = new File(fileName);
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		this.channel = raf.getChannel();
		if (this.channel.size() == 0)
			this.createFile();
	}
	
	private long hash(long matric) {
		return matric % MAX;
	}
	
	private long inc(long matric) {
		return (matric % (MAX - 2)) + 1;
	}
	
	private boolean isEmptyOrDeleted(long pos) throws IOException {
		ByteBuffer b = ByteBuffer.allocate(8);
		this.channel.read(b, pos);
		b.flip();
		long matric = b.getLong();
		if (matric == VAZIO || matric == DELETADO) {
			return true;
		}
		return false;
	}
	
	private long getPosition(long matric) throws IOException {
		long hash, inc, pos;
		
		hash = this.hash(matric);
		inc = this.inc(matric);
		
		for(pos = hash; !this.isEmpty(pos * Aluno.tamanho); pos = (pos + inc) % MAX) {
			if (this.compareMatric(pos * Aluno.tamanho, matric)) {
				return pos * Aluno.tamanho;
			}
		}
		
		return -1;
	}
	
	private boolean compareMatric(long pos, long matric) throws IOException {
		ByteBuffer b = ByteBuffer.allocate(8);
		this.channel.read(b, pos);
		b.flip();
		long m = b.getLong();
		if (matric == m) {
			return true;
		}
		return false;
	}
	
	private boolean isEmpty(long pos) throws IOException {
		ByteBuffer b = ByteBuffer.allocate(8);
		this.channel.read(b, pos);
		b.flip();
		long matric = b.getLong();
		if (matric == VAZIO) {
			return true;
		}
		return false;
	}
	
	private void createFile() throws IOException {
		for(long i = 0; i < MAX; i++) {
			ByteBuffer buffer = ByteBuffer.allocate(Aluno.tamanho);
			this.channel.write(buffer, i * Aluno.tamanho);
		}
	}
	
	@Override
	public void addAluno(Aluno a) throws IOException {
		long hash, inc, inc2, pos, pos2, pos_file, pos2_file;
		int custo, custo2;
		
		hash = this.hash(a.getMatric());
		inc = this.inc(a.getMatric());
		
		ByteBuffer buff = ByteBuffer.allocate(Aluno.tamanho);
		
		custo = 0;
		pos = hash;
		pos_file = pos * Aluno.tamanho;
		while (!isEmptyOrDeleted(pos_file)) {
			custo++;
			pos = (pos + inc) % MAX;
			pos_file = pos * Aluno.tamanho;
		}		
		if (custo < 2) {
			ByteBuffer b = Conversor.toByteBuffer(a);
			this.channel.write(b, pos_file);
			return;
		} else {
			pos = hash;
			for (int i = 0; i < custo; i++) {
				pos_file = pos * Aluno.tamanho;
				this.channel.read(buff, pos_file);
				buff.flip();
				Aluno a_ = Conversor.toAluno(buff);
				inc2 = this.inc(a_.getMatric());
				custo2 = 0;
				pos2 = (pos + inc2) % MAX;
				pos2_file = pos2 * Aluno.tamanho;
				while (custo2 <= i && !isEmptyOrDeleted(pos2_file)) {
					pos2 = (pos2 + inc2) % MAX;
					pos2_file = pos2 * Aluno.tamanho;
					custo2++;
				}
				if (custo2 <= i) {
					this.channel.write(Conversor.toByteBuffer(a_), pos2_file);
					break;
				}
			}
		}
		ByteBuffer b = Conversor.toByteBuffer(a);
		this.channel.write(b, pos_file);
	}

	@Override
	public Aluno getAluno(long matric) throws IOException {
		long pos = this.getPosition(matric);
		if (pos >= 0) {
			ByteBuffer b = ByteBuffer.allocate(Aluno.tamanho);
			b.flip();
			this.channel.read(b, pos);
			return Conversor.toAluno(b);
		}
		return null;
	}

	@Override
	public boolean delAluno(long matric) throws IOException {
		long pos = this.getPosition(matric);
		if ( pos >= 0) {
			ByteBuffer b = ByteBuffer.allocate(Aluno.tamanho);
			this.channel.read(b, pos);
			b.flip();
			Aluno a = Conversor.toAluno(b);
			a.setMatric(-1);
			b = Conversor.toByteBuffer(a);
			this.channel.write(b, pos);
			return true;
		}
		return false;
	}
	
}
