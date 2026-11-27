package cl.duoc.kembang.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import cl.duoc.kembang.model.Comuna;
import cl.duoc.kembang.repository.ComunaRepository;

@Service
public class ComunaService {

    private final ComunaRepository comunaRepository;

    public ComunaService(ComunaRepository comunaRepository) {
        this.comunaRepository = comunaRepository;
    }

    public List<Comuna> listar() {
        return comunaRepository.findAll(Sort.by("nombre"));
    }
}