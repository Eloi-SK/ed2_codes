package helper;

import java.nio.ByteBuffer;
import entidades.Aluno;

public class Conversor {
	
	public static ByteBuffer toByteBuffer(Aluno a) {
		ByteBuffer buffer = ByteBuffer.allocate(a.tamanho);
		
		buffer.putLong(a.getMatric());
		buffer.put(a.getNome().getBytes());
		buffer.position(68);
		buffer.put(a.getEnde().getBytes());
		buffer.position(148);
		buffer.put(a.getEmail().getBytes());
		buffer.position(198);
		buffer.putShort(a.getCurso());
		buffer.flip();
		
		return buffer;
	}
	
	public static Aluno toAluno(ByteBuffer buffer) {
		buffer.clear();
		
		long mat = buffer.getLong();
		
		byte[] b_nome = new byte[60];
		byte[] b_end = new byte[80];
		byte[] b_email = new byte[50];
		
		buffer.get(b_nome);
		buffer.position(68);
		buffer.get(b_end);
		buffer.position(148);
		buffer.get(b_email);
		buffer.position(198);
		
		short curso = buffer.getShort();
		
		return new Aluno(mat, new String(b_nome), new String(b_end), new String(b_email), curso);
	}
	
}
