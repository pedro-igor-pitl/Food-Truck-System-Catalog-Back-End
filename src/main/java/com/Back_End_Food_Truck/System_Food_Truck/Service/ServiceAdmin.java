package com.Back_End_Food_Truck.System_Food_Truck.Service;

import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOEndereco;
import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOListaUsuario;
import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOUsuario;
import com.Back_End_Food_Truck.System_Food_Truck.Model.Endereco;
import com.Back_End_Food_Truck.System_Food_Truck.Model.Usuario;
import com.Back_End_Food_Truck.System_Food_Truck.Repository.EnderecoRepository;
import com.Back_End_Food_Truck.System_Food_Truck.Repository.RepositoryAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.Back_End_Food_Truck.System_Food_Truck.Model.TipoUsuario.A;

@Service
public class ServiceAdmin {


    @Autowired
    private RepositoryAdmin repositoryAdmin;

    @Autowired
    private EnderecoRepository enderecoRepository;

    public Optional<Usuario> autenticar(String email, String senha) {
        return repositoryAdmin.findByEmailAndSenha(email, senha)
                .filter(u -> u.getTipo() == A);
    }

    public List<DTOListaUsuario> listarUsuarios() {
        return repositoryAdmin.findAll()
                .stream()
                .map(u -> {
                    String ativo = u.getAtivo() != null ? u.getAtivo().toString() : "false";
                    String tipo = u.getTipo() != null ? u.getTipo().name() : "";

                    String rua = "";
                    String bairro = "";
                    String cidade = "";
                    String cep = "";
                    String numero = "";
                    String complemento = "";

                    if(u.getEndereco() != null) {
                        rua = u.getEndereco().getRua();
                        bairro = u.getEndereco().getBairro();
                        cidade = u.getEndereco().getCidade();
                        cep = u.getEndereco().getCep();
                        numero = u.getEndereco().getNumero();
                        complemento = u.getEndereco().getComplemento();
                    }

                    return new DTOListaUsuario(
                            ativo,
                            u.getNome(),
                            u.getEmail(),
                            u.getTelefone(),
                            tipo,
                            rua,
                            bairro,
                            cidade,
                            cep,
                            numero,
                            complemento
                    );
                })
                .collect(Collectors.toList());
    }

    public Optional<DTOListaUsuario> buscarPorEmailOuTelefone(String parametro) {
        Optional<Usuario> usuario = repositoryAdmin.findByEmail(parametro);

        if (usuario.isEmpty()) {
            usuario = repositoryAdmin.findByTelefone(parametro);
        }

        // Se encontrou, converte para DTO
        return usuario.map(u -> new DTOListaUsuario(
                u.getAtivo() ? "Sim" : "Não",
                u.getNome(),
                u.getEmail(),
                u.getTelefone(),
                u.getTipo().name(), // se for enum
                u.getEndereco().getRua(),
                u.getEndereco().getBairro(),
                u.getEndereco().getCidade(),
                u.getEndereco().getCep(),
                u.getEndereco().getNumero(),
                u.getEndereco().getComplemento()
        ));
    }



    public Usuario cadastrarUsuario(DTOUsuario dtoUsuario) {
        DTOEndereco dtoEndereco = dtoUsuario.getEndereco();
        Endereco endereco = new Endereco();
        endereco.setRua(dtoEndereco.getRua());
        endereco.setCidade(dtoEndereco.getCidade());
        endereco.setComplemento(dtoEndereco.getComplemento());
        endereco.setCep(dtoEndereco.getCep());
        endereco.setNumero(dtoEndereco.getNumero());
        endereco.setComplemento(dtoEndereco.getComplemento());
        endereco.setBairro(dtoEndereco.getBairro());

        enderecoRepository.save(endereco);

        Usuario usuario = new Usuario();
        usuario.setNome(dtoUsuario.getNome());
        usuario.setEmail(dtoUsuario.getEmail());
        usuario.setTelefone(dtoUsuario.getTelefone());
        usuario.setTipo(dtoUsuario.getTipo());
        usuario.setEndereco(endereco);

        return repositoryAdmin.save(usuario);
    }

    }