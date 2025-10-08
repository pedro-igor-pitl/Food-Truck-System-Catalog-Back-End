package com.Back_End_Food_Truck.System_Food_Truck.Service;

import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOCategoria;
import com.Back_End_Food_Truck.System_Food_Truck.Model.Categoria;
import com.Back_End_Food_Truck.System_Food_Truck.Repository.RepositoryCategoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceCategoria {

    @Autowired
    private RepositoryCategoria repositoryCategoria;

    // CRIAR categoria
    public Categoria criarCategoria(DTOCategoria dtoCategoria) {
        Categoria categoria = new Categoria();
        categoria.setNome(dtoCategoria.getNome());
        categoria.setDescricao(dtoCategoria.getDescricao());
        categoria.setAtivo(dtoCategoria.isAtivo());
        return repositoryCategoria.save(categoria);
    }

    // LISTAR todas as categorias
    public List<DTOCategoria> listarCategorias() {
        return repositoryCategoria.findAll().stream()
                .map(c -> new DTOCategoria(
                        c.getNome(),
                        c.getDescricao(),
                        c.getAtivo() != null && c.getAtivo()
                ))
                .collect(Collectors.toList());
    }

    // BUSCAR por ID
    public Optional<DTOCategoria> buscarPorId(Long id) {
        return repositoryCategoria.findById(id)
                .map(c -> new DTOCategoria(
                        c.getNome(),
                        c.getDescricao(),
                        c.getAtivo() != null && c.getAtivo()
                ));
    }

    // ATUALIZAR categoria
    public Optional<Categoria> atualizarCategoria(Long id, DTOCategoria dtoCategoria) {
        Optional<Categoria> opt = repositoryCategoria.findById(id);
        if (opt.isPresent()) {
            Categoria categoria = opt.get();
            categoria.setNome(dtoCategoria.getNome());
            categoria.setDescricao(dtoCategoria.getDescricao());
            categoria.setAtivo(dtoCategoria.isAtivo());
            repositoryCategoria.save(categoria);
            return Optional.of(categoria);
        }
        return Optional.empty();
    }

    // DELETAR categoria
    public boolean deletarCategoria(Long id) {
        if (repositoryCategoria.existsById(id)) {
            repositoryCategoria.deleteById(id);
            return true;
        }
        return false;
    }
}
