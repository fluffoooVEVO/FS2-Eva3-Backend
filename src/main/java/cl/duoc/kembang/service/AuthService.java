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
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
        String email = dto.getEmail().trim().toLowerCase();
        if (usuarioRepository.existsByEmail(email)) {
            throw new EmailEnUsoException("Ya existe una cuenta con ese correo");
        }
        Rol cliente = rolRepository.findByNombre("CLIENTE")
                .orElseThrow(() -> new ResourceNotFoundException("No existe el rol CLIENTE"));
        Usuario usuario = usuarioRepository.save(new Usuario(null, dto.getNombre().trim(), email,
                passwordEncoder.encode(dto.getPassword()), cliente));
        log.info("Usuario registrado: {}", email);
        return generarRespuesta(usuario);
    }

    public AuthRespuestaDTO login(LoginDTO dto) {
        String email = dto.getEmail().trim().toLowerCase();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .filter(u -> passwordEncoder.matches(dto.getPassword(), u.getPassword()))
                .orElseThrow(() -> new CredencialesInvalidasException("Correo o contraseña incorrectos"));
        log.info("Inicio de sesion: {}", email);
        return generarRespuesta(usuario);
    }

    private AuthRespuestaDTO generarRespuesta(Usuario usuario) {
        Instant ahora = Instant.now();
        long segundos = minutosExpiracion * 60;
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("kembang-backend")
                .issuedAt(ahora)
                .expiresAt(ahora.plusSeconds(segundos))
                .subject(usuario.getEmail())
                .claim("rol", usuario.getRol().getNombre())
                .claim("nombre", usuario.getNombre())
                .build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
        return new AuthRespuestaDTO(token, "Bearer", segundos, usuario.getNombre(),
                usuario.getEmail(), usuario.getRol().getNombre());
    }
}