package com.Back_End_Food_Truck.System_Food_Truck.Service;

import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOEndereco;
import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOListaUsuario;
import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOUsuario;
import com.Back_End_Food_Truck.System_Food_Truck.Model.Endereco;
import com.Back_End_Food_Truck.System_Food_Truck.Model.TipoUsuario;
import com.Back_End_Food_Truck.System_Food_Truck.Model.Usuario;
import com.Back_End_Food_Truck.System_Food_Truck.Repository.RepositoryEndereco;
import com.Back_End_Food_Truck.System_Food_Truck.Repository.RepositoryAdmin;
import com.Back_End_Food_Truck.System_Food_Truck.Repository.RepositoryUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceUsuario {

    @Autowired
    private RepositoryAdmin repositoryAdmin;

    @Autowired
    private RepositoryUsuario repositoryUsuario;

    @Autowired
    private RepositoryEndereco repositoryEndereco;

    // Autenticação
    public Optional<Usuario> autenticar(String email, String senha) {
        return repositoryAdmin.findByEmailAndSenha(email, senha)
                .filter(u -> u.getTipo() == TipoUsuario.A);
    }

    // CRIAR usuário
    public Usuario cadastrarUsuario(DTOUsuario dtoUsuario) {
        Endereco endereco = null;
        DTOEndereco dtoEndereco = dtoUsuario.getEndereco();
        if (dtoEndereco != null) {
            endereco = new Endereco();
            endereco.setRua(dtoEndereco.getRua());
            endereco.setCidade(dtoEndereco.getCidade());
            endereco.setBairro(dtoEndereco.getBairro());
            endereco.setCep(dtoEndereco.getCep());
            endereco.setNumero(dtoEndereco.getNumero());
            endereco.setComplemento(dtoEndereco.getComplemento());
            repositoryEndereco.save(endereco);
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dtoUsuario.getNome());
        usuario.setEmail(dtoUsuario.getEmail());
        usuario.setTelefone(dtoUsuario.getTelefone());
        usuario.setTipo(dtoUsuario.getTipo());
        usuario.setAtivo(true);
        usuario.setEndereco(endereco);

        // Definir senha conforme tipo
        if (TipoUsuario.A.equals(dtoUsuario.getTipo())) {
            if (dtoUsuario.getSenha() == null || dtoUsuario.getSenha().isEmpty()) {
                throw new IllegalArgumentException("Senha obrigatória para administradores.");
            }
            usuario.setSenha(dtoUsuario.getSenha());
        } else if (TipoUsuario.C.equals(dtoUsuario.getTipo())) {
            usuario.setSenha(dtoUsuario.getSenha() != null ? dtoUsuario.getSenha() : "*");
        }

        return repositoryUsuario.save(usuario);
    }

    // LISTAR usuários
    public List<DTOListaUsuario> listarUsuarios() {
        return repositoryUsuario.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    // BUSCAR por ID
    public Optional<DTOListaUsuario> buscarPorId(Long id) {
        return repositoryUsuario.findById(id)
                .map(this::converterParaDTO);
    }

    // BUSCAR por Email ou Telefone
    public Optional<DTOListaUsuario> buscarPorEmailOuTelefone(String parametro) {
        Optional<Usuario> usuario = repositoryUsuario.findByEmail(parametro);
        if (usuario.isEmpty()) {
            usuario = repositoryUsuario.findByTelefone(parametro);
        }
        return usuario.map(this::converterParaDTO);
    }

    // ATUALIZAR usuário
    public Optional<Usuario> atualizarUsuario(Long id, DTOUsuario dtoUsuario) {
        return repositoryUsuario.findById(id).map(usuario -> {

            usuario.setAtivo("true".equalsIgnoreCase(dtoUsuario.getAtivo()));
            usuario.setNome(dtoUsuario.getNome());
            usuario.setEmail(dtoUsuario.getEmail());
            usuario.setTelefone(dtoUsuario.getTelefone());
            usuario.setTipo(dtoUsuario.getTipo());

            if (dtoUsuario.getEndereco() != null) {
                Endereco endereco = usuario.getEndereco();
                if (endereco == null) endereco = new Endereco();

                DTOEndereco dtoEndereco = dtoUsuario.getEndereco();
                endereco.setRua(dtoEndereco.getRua());
                endereco.setCidade(dtoEndereco.getCidade());
                endereco.setBairro(dtoEndereco.getBairro());
                endereco.setCep(dtoEndereco.getCep());
                endereco.setNumero(dtoEndereco.getNumero());
                endereco.setComplemento(dtoEndereco.getComplemento());

                repositoryEndereco.save(endereco);
                usuario.setEndereco(endereco);
            }

            return repositoryUsuario.save(usuario);
        });
    }

    // DELETAR usuário
    public boolean deletarUsuario(Long id) {
        if (repositoryUsuario.existsById(id)) {
            repositoryUsuario.deleteById(id);
            return true;
        }
        return false;
    }

    //MÉTODOAUXILIAR
    private DTOListaUsuario converterParaDTO(Usuario u) {
        return new DTOListaUsuario(
                u.getAtivo() != null && u.getAtivo() ? "Sim" : "Não",
                u.getNome(),
                u.getEmail(),
                u.getTelefone(),
                u.getTipo() != null ? u.getTipo().name() : "",
                u.getEndereco() != null ? u.getEndereco().getRua() : "",
                u.getEndereco() != null ? u.getEndereco().getBairro() : "",
                u.getEndereco() != null ? u.getEndereco().getCidade() : "",
                u.getEndereco() != null ? u.getEndereco().getCep() : "",
                u.getEndereco() != null ? u.getEndereco().getNumero() : "",
                u.getEndereco() != null ? u.getEndereco().getComplemento() : ""
        );
    }
}
