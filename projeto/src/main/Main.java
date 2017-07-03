package main;

import java.io.IOException;

import entidades.Aluno;

public class Main {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		Aluno a = new Aluno();
		a.setMatric(123456);
		a.setNome("IZABEL MORAIS DE MATOS");
		a.setEnde("HERACLITO ROLLEMBERG");
		a.setEmail("izabel_matos@hotmail.com");
		a.setCurso((short) 170);
		
		Aluno b = new Aluno();
		b.setMatric(654321);
		b.setNome("ELOI MORAIS DE MATOS");
		b.setEnde("HERACLITO ROLLEMBERG");
		b.setEmail("eloi_matos@hotmail.com");
		b.setCurso((short) 170);
		
		OrganizadorSimples orgSimples = new OrganizadorSimples("alunos.db");
		
		System.out.println("Adicionando alunos...");
		orgSimples.addAluno(a);
		orgSimples.addAluno(b);
		
                orgSimples.showAlunos();
                
		System.out.println("Pesquisando pela matrícula: 654321");
		Aluno c = orgSimples.getAluno(654321);
		
		if (c == null) {
			System.out.println("Matrícula não encontrada...");
		} else {
			System.out.println("Matícula: " + c.getMatric());
			System.out.println("Nome: " + c.getNome());
			System.out.println("Endereço: " + c.getEnde());
			System.out.println("Email: " + c.getEmail());
			System.out.println("Curso: " + c.getCurso());
		}
		
		
		System.out.println("Deletando matrícula: 654321");
		if(orgSimples.deleteAluno(654321)) {
			System.out.println("Matrícula deletada com sucesso");
		}
		else {
			System.out.println("Matrícula não encontrada...");
		}
		System.out.println("Pesquisando pela matrícula: 654321");
		Aluno d = orgSimples.getAluno(654321);
		if (d == null) {
			System.out.println("Matrícula não encontrada...");
		} else {
			System.out.println("Matícula: " + d.getMatric());
			System.out.println("Nome: " + d.getNome());
			System.out.println("Endereço: " + d.getEnde());
			System.out.println("Email: " + d.getEmail());
			System.out.println("Curso: " + d.getCurso());
		}
	}

}
