package com.mycompany.fintrack.repository;

import java.util.List;
import java.util.Optional;

public interface RepositorioGenerico<T, ID> {
    void adicionar(T entidade);
    void atualizar(T entidade);
    void remover(ID id);
    Optional<T> buscarPorId(ID id);
    List<T> listarTodos();
}