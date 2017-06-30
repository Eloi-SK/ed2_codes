package interfaces;

import java.io.IOException;

import entidades.Aluno;

public interface IOrganizadorSimples {
	
	public void addAluno(Aluno a) throws IOException;
	public Aluno getAluno(long matric) throws IOException;
	public boolean deleteAluno(long matric) throws IOException;

}
