package com.BuracosDCApi.core.security;

import java.util.Random;

import org.springframework.security.crypto.bcrypt.BCrypt;

import com.BuracosDCApi.core.model.Usuario;

public class AuthenticationHelper {

    public static boolean verificarSenha(Usuario usuario, String senha) {
        return BCrypt.checkpw(senha, usuario.getSenha());
    }

    public static boolean verificarSenha(String senha, String hash) {
        return BCrypt.checkpw(senha, hash);
    }

    public static String gerarSenha(String senha) {
        return BCrypt.hashpw(senha, BCrypt.gensalt(12));
    }

    private static String gerarNumerosAleatorios() {
        String saida = "";
        for (int i = 0; i < 6; i++) {
            saida += (int) (Math.random() * 10);
        }
        return saida;
    }

    private static String gerarLetrasAleatorias() {
        String saida = "";
        String letrasMinusculas = "abcdefghijklmnopqrstuvwxyz";
        String letrasMaiusculas = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (int i = 0; i < 3; i++) {
            saida += letrasMaiusculas.charAt((int) (Math.random() * letrasMaiusculas.length()));
        }
        for (int i = 3; i < 6; i++) {
            saida += letrasMinusculas.charAt((int) (Math.random() * letrasMinusculas.length()));
        }
        return saida;
    }

    private static String gerarCaracteresEspeciaisAleatorios() {
        String saida = "";
        String listaCaracteres = "#!@$%&*_-+:;=|(){}[],.?\\";
        for (int i = 0; i < 6; i++) {
            saida += listaCaracteres.charAt((int) (Math.random() * listaCaracteres.length()));
        }
        return saida;
    }

    private static String embaralhar(String s) {
        char a[] = s.toCharArray();

        for (int i = 0; i < a.length; i++) {
            int j = (new Random()).nextInt(a.length);
            char temp = a[i];
            a[i] = a[j];
            a[j] = temp;
        }

        return new String(a);
    }

    public static String gerarNovaSenha() {
        String senha = AuthenticationHelper.gerarLetrasAleatorias();
        senha += AuthenticationHelper.gerarNumerosAleatorios();
        senha += AuthenticationHelper.gerarCaracteresEspeciaisAleatorios();
        senha = AuthenticationHelper.embaralhar(senha);
        return senha;
    }
}
