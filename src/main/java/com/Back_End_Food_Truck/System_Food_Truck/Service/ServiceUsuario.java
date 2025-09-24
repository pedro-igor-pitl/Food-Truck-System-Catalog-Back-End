package com.Back_End_Food_Truck.System_Food_Truck.Service;

import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOEndereco;
import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOListaUsuario;
import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOUsuario;
import com.Back_End_Food_Truck.System_Food_Truck.Model.Endereco;
import com.Back_End_Food_Truck.System_Food_Truck.Model.Usuario;
import com.Back_End_Food_Truck.System_Food_Truck.Repository.RepositoryEndereco;
import com.Back_End_Food_Truck.System_Food_Truck.Repository.RepositoryAdmin;
import com.Back_End_Food_Truck.System_Food_Truck.Repository.RepositoryUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.Back_End_Food_Truck.System_Food_Truck.Model.TipoUsuario.A;

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
                .filter(u -> u.getTipo() == A);
    }

    // CRIAR usuário
    public Usuario cadastrarUsuario(DTOUsuario dtoUsuario) {
        Endereco endereco = new Endereco();
        DTOEndereco dtoEndereco = dtoUsuario.getEndereco();
        endereco.setRua(dtoEndereco.getRua());
        endereco.setCidade(dtoEndereco.getCidade());
        endereco.setBairro(dtoEndereco.getBairro());
        endereco.setCep(dtoEndereco.getCep());
        endereco.setNumero(dtoEndereco.getNumero());
        endereco.setComplemento(dtoEndereco.getComplemento());

        repositoryEndereco.save(endereco);

        Usuario usuario = new Usuario();
        usuario.setNome(dtoUsuario.getNome());
        usuario.setEmail(dtoUsuario.getEmail());
        usuario.setSenha(dtoUsuario.getSenha());
        usuario.setTelefone(dtoUsuario.getTelefone());
        usuario.setTipo(dtoUsuario.getTipo());
        usuario.setAtivo(true);
        usuario.setEndereco(endereco);

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
        return repositoryUsuario.findById(id).map(this::converterParaDTO);
    }

    // BUSCAR por Email ou Telefone
    public Optional<DTOListaUsuario> buscarPorEmailOuTelefone(String parametro) {
        Optional<Usuario> usuario = repositoryAdmin.findByEmail(parametro);
        if (usuario.isEmpty()) {
            usuario = repositoryAdmin.findByTelefone(parametro);
        }
        return usuario.map(this::converterParaDTO);
    }

    // ATUALIZAR usuário
    public Optional<Usuario> atualizarUsuario(Long id, DTOUsuario dtoUsuario) {
        Optional<Usuario> usuarioOpt = repositoryUsuario.findById(id);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setAtivo(Boolean.valueOf(Boolean.parseBoolean(dtoUsuario.getAtivo()) ? "True" : "False"));
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

            repositoryUsuario.save(usuario);
            return Optional.of(usuario);
        }
        return Optional.empty();
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

