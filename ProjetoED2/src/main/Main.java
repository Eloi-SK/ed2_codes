package main;

import java.nio.ByteBuffer;
import util.Conversor;
import entidades.Aluno;

public class Main {

	public static void main(String[] args) {
		
		Aluno a = new Aluno(12, "eloi", "rua x", "eloi@ufs.br", (short) 170);
		
		ByteBuffer buffer = Conversor.toByteBuffer(a);
		
		Aluno b = Conversor.toAluno(buffer);
		
		System.out.println(b.getMatricula());
		System.out.println(b.getNome());
		System.out.println(b.getEndereco());
		System.out.println(b.getEmail());
		System.out.println(b.getCurso());

	}

}
