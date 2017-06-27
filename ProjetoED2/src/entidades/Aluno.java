package entidades;

public class Aluno {
	
	private long matricula; // 8 bytes
	private String nome; // 60 bytes
	private String endereco; // 80 bytes
	private String email; // 50 bytes
	private short curso; // 2 bytes
	
	public static final int tamanho = 200;
	
	public Aluno(long matricula,
			String nome,
			String endereco,
			String email,
			short curso) {
		this.matricula = matricula;
		this.nome = nome;
		this.endereco = endereco;
		this.email = email;
		this.curso = curso;
	}
	
	public long getMatricula() {
		return matricula;
	}
	public void setMatricula(long matricula) {
		this.matricula = matricula;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEndereco() {
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public short getCurso() {
		return curso;
	}
	public void setCurso(short curso) {
		this.curso = curso;
	}
}
