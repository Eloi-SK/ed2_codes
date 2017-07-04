package interfaces;

import java.io.IOException;

import entidades.Aluno;

public interface IOrganizador {
	
	public void addAluno(Aluno a) throws IOException;
	public Aluno getAluno(long matric) throws IOException;
	public boolean delAluno(long matric) throws IOException;
}
