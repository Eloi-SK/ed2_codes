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
		a.setEmail("jose@hotmail.com");
		a.setEnde("Rua J");
		a.setMatric(40);
		a.setNome("Jos�");
		

		org.delAluno(40);
		
	}

}
