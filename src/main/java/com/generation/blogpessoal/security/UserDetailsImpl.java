package com.generation.blogpessoal.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.generation.blogpessoal.model.Usuario;

/*objetivo: informar para o security os dados de acesso da API
 * criação de atributo username = quem é que está tentando fazer o login e a senha
 * */
public class UserDetailsImpl implements UserDetails {
	//informando a versão que está sendo passada - userdetails
	private static final long serialVersionUID = 1L;
	
	private String userName;
	private String password;
	private List<GrantedAuthority> authorities;
	// lista que guarda quais são as autorizações que o usuario tem acesso
	

	public UserDetailsImpl(Usuario user) {
		this.userName = user.getUsuario();
		this.password = user.getSenha();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		// se a conta do usuario nao expirou, ele acessa
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// se a conta não expirar, acessa 
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// se a credencial não estiver expirada, acessa 
		return true;
	}

	@Override
	public boolean isEnabled() {
		// se o usuario está habilitado, acessa
		return true;
	}

	
}
