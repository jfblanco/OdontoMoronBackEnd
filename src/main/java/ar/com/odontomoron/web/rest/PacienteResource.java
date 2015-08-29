package ar.com.odontomoron.web.rest;

import com.codahale.metrics.annotation.Timed;
import ar.com.odontomoron.domain.Paciente;
import ar.com.odontomoron.repository.PacienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Paciente.
 */
@RestController
@RequestMapping("/api")
public class PacienteResource {

    private final Logger log = LoggerFactory.getLogger(PacienteResource.class);

    @Inject
    private PacienteRepository pacienteRepository;

    /**
     * POST  /pacientes -> Create a new paciente.
     */
    @RequestMapping(value = "/pacientes",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody Paciente paciente) throws URISyntaxException {
        log.debug("REST request to save Paciente : {}", paciente);
        if (paciente.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new paciente cannot already have an ID").build();
        }
        pacienteRepository.save(paciente);
        return ResponseEntity.created(new URI("/api/pacientes/" + paciente.getId())).build();
    }

    /**
     * PUT  /pacientes -> Updates an existing paciente.
     */
    @RequestMapping(value = "/pacientes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody Paciente paciente) throws URISyntaxException {
        log.debug("REST request to update Paciente : {}", paciente);
        if (paciente.getId() == null) {
            return create(paciente);
        }
        pacienteRepository.save(paciente);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /pacientes -> get all the pacientes.
     */
    @RequestMapping(value = "/pacientes",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Paciente> getAll() {
        log.debug("REST request to get all Pacientes");
        return pacienteRepository.findAll();
    }

    /**
     * GET  /pacientes/:id -> get the "id" paciente.
     */
    @RequestMapping(value = "/pacientes/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Paciente> get(@PathVariable Long id) {
        log.debug("REST request to get Paciente : {}", id);
        return Optional.ofNullable(pacienteRepository.findOne(id))
            .map(paciente -> new ResponseEntity<>(
                paciente,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /pacientes/:id -> delete the "id" paciente.
     */
    @RequestMapping(value = "/pacientes/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Paciente : {}", id);
        pacienteRepository.delete(id);
    }
}
