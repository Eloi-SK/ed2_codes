package main;

import java.io.IOException;

import entidades.Aluno;

public class Main {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		OrganizadorSequencial org = new OrganizadorSequencial("alunos.db");
		
		Aluno a = new Aluno();
		a.setCurso((short) 170);
		a.setEmail("eloi_matos@hotmail.com");
		a.setEnde("Rua B");
		a.setMatric(25);
		a.setNome("Eloi Morais de Matos");
		
		org.addAluno(a);
	}

}
