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
        categoria.setAtivo(dtoCategoria.getAtivo());  // Acessando diretamente a propriedade ativo
        return repositoryCategoria.save(categoria);
    }

    // LISTAR todas as categorias
    public List<DTOCategoria> listarCategorias() {
        return repositoryCategoria.findAll().stream()
                .map(c -> new DTOCategoria(
                        c.getId(),
                        c.getNome(),
                        c.getDescricao(),
                        c.getAtivo() != null && c.getAtivo()  // Acessando diretamente a propriedade ativo
                ))
                .collect(Collectors.toList());
    }

    // BUSCAR por ID
    public Optional<DTOCategoria> buscarPorId(Long id) {
        return repositoryCategoria.findById(id)
                .map(c -> new DTOCategoria(
                        c.getId(),
                        c.getNome(),
                        c.getDescricao(),
                        c.getAtivo() != null && c.getAtivo()  // Acessando diretamente a propriedade ativo
                ));
    }

    // ATUALIZAR categoria
    public Optional<Categoria> atualizarCategoria(Long id, DTOCategoria dtoCategoria) {
        Optional<Categoria> opt = repositoryCategoria.findById(id);
        if (opt.isPresent()) {
            Categoria categoria = opt.get();
            categoria.setNome(dtoCategoria.getNome());
            categoria.setDescricao(dtoCategoria.getDescricao());
            categoria.setAtivo(dtoCategoria.getAtivo());  // Acessando diretamente a propriedade ativo
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

    // ALTERNAR status de categoria
    public boolean alternarStatusCategoria(Long id) {
        Optional<Categoria> categoriaOptional = repositoryCategoria.findById(id);  // Invocando 'findById' corretamente

        if (!categoriaOptional.isPresent()) {
            return false;  // Se a categoria não for encontrada, retorna 'false'
        }

        Categoria categoria = categoriaOptional.get();

        // Alterna o status de 'ativo' (se for true, coloca false e vice-versa)
        boolean novoStatus = !categoria.getAtivo();  // Corrigido: usando getAtivo() para acessar o valor de 'ativo'
        categoria.setAtivo(novoStatus);  // Define o novo status

        // Salva a categoria com o novo status
        repositoryCategoria.save(categoria);

        return true;  // Retorna 'true' se a alteração for bem-sucedida
    }

}
