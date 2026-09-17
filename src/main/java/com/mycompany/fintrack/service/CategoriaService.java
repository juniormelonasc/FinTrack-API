package com.mycompany.fintrack.service;

import com.mycompany.fintrack.dto.CategoriaDTO;
import com.mycompany.fintrack.exception.ResourceNotFoundException;
import com.mycompany.fintrack.model.Categoria;
import com.mycompany.fintrack.model.Usuario;
import com.mycompany.fintrack.repository.CategoriaRepository;
import com.mycompany.fintrack.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;

    public CategoriaService(CategoriaRepository categoriaRepository,
                            UsuarioRepository usuarioRepository) {
        this.categoriaRepository = categoriaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<CategoriaDTO> listarPorUsuario(Long usuarioId) {
        return categoriaRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public CategoriaDTO criar(Long usuarioId, CategoriaDTO dto) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        Categoria categoria = new Categoria();
        categoria.setNome(dto.getNome());
        categoria.setUsuario(usuario);

        return toDTO(categoriaRepository.save(categoria));
    }

    // NOVO: atualizar categoria
    @Transactional
    public CategoriaDTO atualizar(Long usuarioId, Long categoriaId, CategoriaDTO dto) {
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        if (!categoria.getUsuario().getId().equals(usuarioId)) {
            throw new ResourceNotFoundException("Categoria não pertence a este usuário");
        }

        categoria.setNome(dto.getNome());
        return toDTO(categoriaRepository.save(categoria));
    }

    public void deletar(Long usuarioId, Long categoriaId) {
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        if (!categoria.getUsuario().getId().equals(usuarioId)) {
            throw new ResourceNotFoundException("Categoria não pertence a este usuário");
        }

        categoriaRepository.delete(categoria);
    }

    private CategoriaDTO toDTO(Categoria c) {
        return new CategoriaDTO(c.getId(), c.getNome());
    }
}