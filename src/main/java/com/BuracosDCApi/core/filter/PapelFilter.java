package com.BuracosDCApi.core.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.BuracosDCApi.core.model.Papel;
import com.BuracosDCApi.core.service.PapelService;
import com.BuracosDCApi.core.util.ResponseUtil;
import com.BuracosDCApi.util.JSONProcessor;


@Component
@Order(2)
public class PapelFilter implements Filter {

	@Autowired
	PapelService papelService;
	
	List<String> rotasPublica = new ArrayList<String>();
	
	Map<String, String> mapaDeOperacoes = new HashMap<String, String>();

	public PapelFilter() {
		rotasPublica.add("/auth/login");
		rotasPublica.add("/auth/logs");
		rotasPublica.add("/auth/atualizarSessao");

		rotasPublica.add("/api");
		
		mapaDeOperacoes.put("POST", "C");
		mapaDeOperacoes.put("GET", "R");
		mapaDeOperacoes.put("PUT", "U");
		mapaDeOperacoes.put("DELETE", "D");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		// Requisição do tipo OPTIONS são geradas automaticamente pelo navegador
		if (((HttpServletRequest) request).getMethod().equalsIgnoreCase("OPTIONS")) {
			chain.doFilter(request, response);
		} else if (hasAuthority((HttpServletRequest) request)) {
			chain.doFilter(request, response);
		} else {
			Exception acessoNaoAutoriuzado = new Exception("Acesso não autorizado");
			HttpServletResponse resp = (HttpServletResponse) response;
			resp.setStatus(403);
			resp.setContentType("application/json");
			ResponseUtil.write(resp, JSONProcessor.toJSON(acessoNaoAutoriuzado));
		}

	}

	public synchronized boolean hasAuthority(HttpServletRequest request) {
		if(isRotaPublica(request)) return true;

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		Papel p;

		if (auth != null && isAuthenticated()) {
			for (GrantedAuthority authority : auth.getAuthorities()) {
				p = papelService.getMapaDePapeis().get(authority.getAuthority());
				if(hasOperation(request, p)) return true;
			}
		}
		return false;
	}
	
	public boolean hasOperation(HttpServletRequest request, @NotNull Papel papel) {
		if(request.getMethod().contains("OPTION")) return true;
		String verbo = request.getMethod();
		String rota = obterUrlDoController(request);
		
		if(rota == null || papel == null) {
			return false;
		}

		Map<String, String> mapaPapeisUsuarioLogado = papel.getRotas();
		String permissaoRota = null;

		if(mapaPapeisUsuarioLogado != null && mapaPapeisUsuarioLogado.containsKey(rota)) {
			permissaoRota = mapaPapeisUsuarioLogado.get(rota);
		}

		if(permissaoRota != null && permissaoRota.contains(mapaDeOperacoes.get(verbo))) return true;
		
		return false;
	}
	
	public String obterUrlDoController(HttpServletRequest request) {
		String uri = request.getRequestURI();

		String[] chuncks = uri.split("/");
		if (chuncks.length > 3) {
			return "/"+chuncks[2];
		}
		return null;
	}

	public Boolean isRotaPublica(HttpServletRequest request) {
		String uri = request.getRequestURI();
		for(String rota: rotasPublica) {
			if(uri.contains(rota)) return true;
		}
		return false;
	}

	public boolean isAuthenticated() {
		for (GrantedAuthority ga : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
			if (ga.getAuthority() == null || ga.getAuthority().equals("ROLE_ANONYMOUS"))
				return false;
		}
		return true;
	}
}
