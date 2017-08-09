package main;

import java.io.IOException;

import entidades.Aluno;
import organizadores.*;

public class Teste {

	public static void main(String[] args)throws IOException {
		
		Aluno a = new Aluno(6, "Dê Certo", "Av. Heraclito Rollemberg", "de_certo@hotmail.com", (short)170);
		
		OrganizadorBrent org = new OrganizadorBrent("alunos.dat");
		
		org.addAluno(a);

	}

}
