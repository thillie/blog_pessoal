package com.generation.blogpessoal.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.model.UsuarioLogin;
import com.generation.blogpessoal.repository.UsuarioRepository;
import com.generation.blogpessoal.security.JwtService;

@Service //Avisa o ambiente do spring que est´tratando sobre regras de negócio
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private JwtService jwtService;
	
	
	private AuthenticationManager authenticationManager;
	//classe security tem gestão de autenticação
	//permite acessar métodos que podem entregar ao objeto autorizações concedidas
	
	//primeira regra de negócio, definir as regras para permitir o cadastro de um usuário
	public Optional<Usuario> cadastrarUsuario(Usuario usuario) {
		
		//verifica se o usuário existe: nome | usuario(email) | senha | foto
		if (usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent())
			return Optional.empty();

		usuario.setSenha(criptografarSenha(usuario.getSenha()));

		return Optional.of(usuarioRepository.save(usuario));
	
	}
	
	//método que trata a senha para ser criptografada antes de chegar ao banco 
	
	private String criptografarSenha(String senha) {
		//classe que  trata a criptografia
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		return encoder.encode(senha);

	}

	//objetivo: evitar dois usuários com o mesmo email quando acontecer o update
	
	public Optional<Usuario> atualizarUsuario(Usuario usuario) {
		//validando se o id (que está na requisição put) existe no banco de dados
		if(usuarioRepository.findById(usuario.getId()).isPresent()) {

			//optional pode existir ou não
			Optional<Usuario> buscaUsuario = usuarioRepository.findByUsuario(usuario.getUsuario());

			if ( (buscaUsuario.isPresent()) && ( buscaUsuario.get().getId() != usuario.getId()))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já existe!", null);

			usuario.setSenha(criptografarSenha(usuario.getSenha()));

			return Optional.ofNullable(usuarioRepository.save(usuario));
		}
		return Optional.empty();
	}
	
	//objetivo: garantir regras de negócio ou autenticação para login
	
	public Optional<UsuarioLogin> autenticarUsuario(Optional<UsuarioLogin> usuarioLogin) {
      
        // Gera o Objeto de autenticação
		var credenciais = new UsernamePasswordAuthenticationToken(usuarioLogin.get().getUsuario(), usuarioLogin.get().getSenha());
		
        // Autentica o Usuario
		Authentication authentication = authenticationManager.authenticate(credenciais);
        System.out.println("autenticação: " + authentication.isAuthenticated());
        // Se a autenticação foi efetuada com sucesso
		if (authentication.isAuthenticated()) {

            // Busca os dados do usuário
			Optional<Usuario> usuario = usuarioRepository.findByUsuario(usuarioLogin.get().getUsuario());

            // Se o usuário foi encontrado
			if (usuario.isPresent()) {

                // Preenche o Objeto usuarioLogin com os dados encontrados 
				usuarioLogin.get().setId(usuario.get().getId());
                usuarioLogin.get().setNome(usuario.get().getNome());
                usuarioLogin.get().setFoto(usuario.get().getFoto());
                usuarioLogin.get().setToken(gerarToken(usuarioLogin.get().getUsuario()));
                usuarioLogin.get().setSenha("");
				
                 // Retorna o Objeto preenchido
			   return usuarioLogin;
			
			}

        } 
		return Optional.empty();

    }
	
	//método que através da jwtSecurity gera o token do usuário
	private String gerarToken(String usuario) {
		return "Bearer " + jwtService.generateToken(usuario);
	}
}
