package cl.duoc.kembang.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import cl.duoc.kembang.dto.LoginDTO;
import cl.duoc.kembang.dto.RegistroDTO;
import cl.duoc.kembang.exception.CredencialesInvalidasException;
import cl.duoc.kembang.exception.EmailEnUsoException;
import cl.duoc.kembang.model.Usuario;
import cl.duoc.kembang.repository.RolRepository;
import cl.duoc.kembang.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtEncoder jwtEncoder;

    private AuthService authService;

    @BeforeEach
    void preparar() {
        authService = new AuthService(usuarioRepository, rolRepository, passwordEncoder, jwtEncoder, 60);
    }

    @Test
    @DisplayName("No permite registrar un email que ya existe")
    void registrarEmailRepetido() {
        RegistroDTO dto = new RegistroDTO();
        dto.setNombre("Cliente Prueba");
        dto.setEmail("cliente@kembang.cl");
        dto.setPassword("123456");
        when(usuarioRepository.existsByEmail("cliente@kembang.cl")).thenReturn(true);

        assertThrows(EmailEnUsoException.class, () -> authService.registrar(dto));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Rechaza el login cuando la contraseña es incorrecta")
    void loginPasswordIncorrecta() {
        Usuario usuario = new Usuario();
        usuario.setEmail("cliente@kembang.cl");
        usuario.setPassword("hashGuardado");
        LoginDTO dto = new LoginDTO();
        dto.setEmail("cliente@kembang.cl");
        dto.setPassword("claveMala");
        when(usuarioRepository.findByEmail("cliente@kembang.cl")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("claveMala", "hashGuardado")).thenReturn(false);

        assertThrows(CredencialesInvalidasException.class, () -> authService.login(dto));
    }
}