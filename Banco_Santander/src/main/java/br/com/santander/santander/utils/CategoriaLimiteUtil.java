package br.com.santander.santander.utils;

import br.com.santander.santander.enuns.Categoria;
import org.springframework.stereotype.Service;

@Service
public class CategoriaLimiteUtil {

    private CategoriaLimiteUtil() {
    }

    public static double limiteCredito(Categoria categoria) {
        return switch (categoria) {
            case COMUM -> 1000.00;
            case SUPER -> 5000.00;
            case PREMIUM -> 10000.00;
            default -> throw new IllegalArgumentException("Categoria desconhecida: " + categoria);
        };
    }

    public static double limiteDiario(Categoria categoria) {
        return switch (categoria) {
            case COMUM -> 500.00;
            case SUPER -> 1000.00;
            case PREMIUM -> 5000.00;
            default -> throw new IllegalArgumentException("Categoria desconhecida: " + categoria);
        };
    }
}
