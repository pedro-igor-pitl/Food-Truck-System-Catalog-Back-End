package com.Back_End_Food_Truck.System_Food_Truck.Service;

import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOEndereco;
import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOUsuario;
import com.Back_End_Food_Truck.System_Food_Truck.Model.Endereco;
import com.Back_End_Food_Truck.System_Food_Truck.Model.Usuario;
import com.Back_End_Food_Truck.System_Food_Truck.Repository.EnderecoRepository;
import com.Back_End_Food_Truck.System_Food_Truck.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.Back_End_Food_Truck.System_Food_Truck.Model.TipoUsuario.A;

@Service
public class UsuarioService {

    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    public Optional<Usuario> autenticar(String email, String senha) {
        return usuarioRepository.findByEmailAndSenha(email, senha)
                .filter(u -> u.getTipo() == A);
    }

    public Optional<Usuario> listaUsuario() {
        return usuarioRepository.findByEmailAndSenha(email, senha)
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
        usuario.setSenha(dtoUsuario.getSenha());
        usuario.setTelefone(dtoUsuario.getTelefone());
        usuario.setTipo(dtoUsuario.getTipo());
        usuario.setEndereco(endereco);

        return usuarioRepository.save(usuario);
    }

    }