package com.namassacompany.petVersoRestFull.service;

import com.namassacompany.petVersoRestFull.dto.UsuarioCadastroDTO;
import com.namassacompany.petVersoRestFull.dto.UsuarioResponseDTO;
import com.namassacompany.petVersoRestFull.exception.EmailJaCadastradoException;
import com.namassacompany.petVersoRestFull.exception.TelefoneJaCadastradoException;
import com.namassacompany.petVersoRestFull.model.Usuario;
import com.namassacompany.petVersoRestFull.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioResponseDTO cadastrar(UsuarioCadastroDTO dto){

        if(usuarioRepository.existsByEmail(dto.email())){
            throw new EmailJaCadastradoException("o email "+ dto.email()+" ja esta cadastrado.");
        }
        if(usuarioRepository.existsByTelefone(dto.telefone())){
            throw new TelefoneJaCadastradoException("o numero de telefone "+ dto.telefone()+ "ja esta cadastrado");
        }
        String senhaHash = passwordEncoder.encode(dto.senha());
        Usuario usuario = new Usuario(
                null,
                dto.nome(),
                dto.email(),
                dto.telefone(),
                senhaHash
        );
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        UsuarioResponseDTO response = new UsuarioResponseDTO(
                usuarioSalvo.getIdUsuario(),
                usuarioSalvo.getNome(),
                usuarioSalvo.getEmail()
        );

        return response;

    }
}
