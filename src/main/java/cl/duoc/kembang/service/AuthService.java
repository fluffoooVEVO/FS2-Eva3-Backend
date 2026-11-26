package cl.duoc.kembang.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import cl.duoc.kembang.dto.AuthRespuestaDTO;
import cl.duoc.kembang.dto.LoginDTO;
import cl.duoc.kembang.dto.RegistroDTO;
import cl.duoc.kembang.exception.CredencialesInvalidasException;
import cl.duoc.kembang.exception.EmailEnUsoException;
import cl.duoc.kembang.exception.ResourceNotFoundException;
import cl.duoc.kembang.model.Rol;
import cl.duoc.kembang.model.Usuario;
import cl.duoc.kembang.repository.RolRepository;
import cl.duoc.kembang.repository.UsuarioRepository;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;
    private final long minutosExpiracion;

    public AuthService(UsuarioRepository usuarioRepository, RolRepository rolRepository,
        PasswordEncoder passwordEncoder, JwtEncoder jwtEncoder,
        @Value("${app.jwt.expiration-minutes}") long minutosExpiracion) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
        this.minutosExpiracion = minutosExpiracion;
    }

    public AuthRespuestaDTO registrar(RegistroDTO dto) {
        String email = dto.getEmail().toLowerCase();
        if (usuarioRepository.existsByEmail(email)) {
            throw new EmailEnUsoException("Ya existe una cuenta con ese correo");
        }
        Rol cliente = rolRepository.findByNombre("CLIENTE")
                .orElseThrow(() -> new ResourceNotFoundException("No existe el rol CLIENTE"));
        String passwordCifrada = passwordEncoder.encode(dto.getPassword());
        Usuario usuario = usuarioRepository.save(new Usuario(null, dto.getNombre(), email, passwordCifrada, cliente));
        return crearRespuesta(usuario);
    }

    public AuthRespuestaDTO login(LoginDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail().toLowerCase())
                .orElseThrow(() -> new CredencialesInvalidasException("Correo o contraseña incorrectos"));
        if (!passwordEncoder.matches(dto.getPassword(), usuario.getPassword())) {
            throw new CredencialesInvalidasException("Correo o contraseña incorrectos");
        }
        return crearRespuesta(usuario);
    }

    private AuthRespuestaDTO crearRespuesta(Usuario usuario) {
        String rol = usuario.getRol().getNombre();
        JwtClaimsSet datos = JwtClaimsSet.builder()
                .subject(usuario.getEmail())
                .claim("rol", rol)
                .expiresAt(Instant.now().plusSeconds(minutosExpiracion * 60))
                .build();
        JwsHeader cabecera = JwsHeader.with(MacAlgorithm.HS256).build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(cabecera, datos)).getTokenValue();
        return new AuthRespuestaDTO(token, usuario.getNombre(), usuario.getEmail(), rol);
    }
}