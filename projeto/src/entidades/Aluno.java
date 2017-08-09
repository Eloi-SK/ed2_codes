package entidades;

public class Aluno {
	
	private long matric; // 8 bytes
	private String nome; // 60 bytes
	private String ende; // 80 bytes
	private String email; // 50 bytes
	private short curso; // 2 bytes
	
	public static final int tamanho = 200;

	public Aluno(
			long matric,
			String nome,
			String ende,
			String email,
			short curso) {
		this.matric = matric;
		this.nome = nome;
		this.ende = ende;
		this.email = email;
		this.curso = curso;
	}
	
	public Aluno() {
		
	}

	public long getMatric() {
		return matric;
	}

	public void setMatric(long matric) {
		this.matric = matric;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEnde() {
		return ende;
	}

	public void setEnde(String ende) {
		this.ende = ende;
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
