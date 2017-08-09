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

	private static final int MAX = 97;
	private static final int VAZIO = 0;
	private static final int DELETADO = -1;
	
	private FileChannel channel;
	
	public OrganizadorBrent(String fileName) throws IOException {
		File file = new File(fileName);
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		this.channel = raf.getChannel();
	}
	
	private long chave(long matric) {
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
		if (matric == VAZIO ||matric == DELETADO) {
			return true;
		}
		return false;
	}
	
	private
	
	private void createFile() throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(Aluno.tamanho * MAX);
		this.channel.write(buffer);
	}
	
	@Override
	public void addAluno(Aluno a) throws IOException {
		long hash, inc, pos, p2, pos2, pos_file, pos2_file;
		int depth, depth2, i;
		
		if (this.channel.size() == 0) {
			this.createFile();
		} else {
			hash = this.chave(a.getMatric());
			inc = this.inc(a.getMatric());
			
			for (pos = hash, depth = 0, pos_file = pos * Aluno.tamanho; !isEmptyOrDeleted(pos_file); pos = (pos + inc) % MAX, pos_file = pos * Aluno.tamanho) {
				depth++;
			}
			
			if (depth < 2) {
				ByteBuffer b = Conversor.toByteBuffer(a);
				this.channel.write(b, pos_file);
				return;
			}
			ByteBuffer buff = ByteBuffer.allocate(Aluno.tamanho);
			
			for (i = 0, pos = hash, pos_file = pos * Aluno.tamanho; i < depth; i++, pos = (hash + inc) % MAX, pos_file = pos * Aluno.tamanho) {
				this.channel.read(buff, pos_file);
				buff.flip();
				Aluno a_ = Conversor.toAluno(buff);
				p2 = this.inc(a_.getMatric());
				
				for (depth2 = 0, pos2 = (pos + p2) % MAX, pos2_file = pos2 * Aluno.tamanho; !isEmptyOrDeleted(pos2_file) && depth2 <= i; depth2++, pos2 = (pos2 + p2) % MAX, pos2_file = pos2 * Aluno.tamanho) {
					if (depth2 <= i) {
						this.channel.write(buff, pos2_file);
						break;
					}
				}
			}
			buff = Conversor.toByteBuffer(a);
			this.channel.write(buff, pos_file);
		}

	}

	@Override
	public Aluno getAluno(long matric) throws IOException {
		long hash, inc, pos, pos_file;
		
		hash = this.chave(matric);
		inc = this.inc(matric);
		
		for (pos = hash, pos_file = pos * Aluno.tamanho; )
		return null;
	}

	@Override
	public boolean delAluno(long matric) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

}
