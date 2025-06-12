package br.edu.ufape.sguAuthService.comunicacao.paginacao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

public class PaginadorUtils {
    public static <T> Page<T> paginarLista(List<T> lista, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), lista.size());
        List<T> pagina = (start > lista.size()) ? Collections.emptyList() : lista.subList(start, end);
        return new PageImpl<>(pagina, pageable, lista.size());
    }
}
